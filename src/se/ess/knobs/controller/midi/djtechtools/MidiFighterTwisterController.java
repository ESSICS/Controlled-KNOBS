/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * Copyright (C) 2017 by European Spallation Source ERIC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.ess.knobs.controller.midi.djtechtools;


import java.text.MessageFormat;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import se.ess.knobs.controller.Controllable;
import se.ess.knobs.controller.midi.AbstractMIDIController;
import se.ess.knobs.controller.spi.Controller;


/**
 * {@link Controller} implementation for the DJTECHTOOLS Midi Fighter Twister.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public class MidiFighterTwisterController extends AbstractMIDIController {

    public static final String IDENTIFIER = "Midi Fighter Twister";

    private static final int CHANNELS = 64;

    private static final Logger LOGGER = Logger.getLogger(MidiFighterTwisterController.class.getName());

    public MidiFighterTwisterController() {
        super(IDENTIFIER);
    }

    @Override
    public void reset() {

        super.reset();

        for ( int c = 0; c < CHANNELS; c++ ) {
            for ( int v = 127; v >= 0; v-- ) {
                send(ShortMessage.CONTROL_CHANGE, 0, c, v, -1);
                send(ShortMessage.CONTROL_CHANGE, 1, c, v, -1);
            }
        }

    }

    @Override
    protected AbstractControllableWrapper createWrapper( Controllable controllable ) {
        return new MFTControllableWrapper(controllable, this);
    }

    @Override
    protected void midiMessageReceived( MidiMessage message, long timeStamp ) {

        if ( message instanceof ShortMessage ) {

            ShortMessage smsg = (ShortMessage) message;
            int midiChannel = smsg.getChannel();

            if ( midiChannel < 2 ) {

                int wrapperChannel = smsg.getData1();

                getWrappers()
                    .stream()
                    .filter(w -> w.getChannel() == wrapperChannel)
                    .forEach(w -> ((MFTControllableWrapper) w).handleReceivedMessage(midiChannel, smsg.getData2()));

            }

        }

    }

    private static class MFTControllableWrapper extends AbstractControllableWrapper {

        private static final long CLICK_DETECTION_TIME = 300L;  //  ms
        private static final long TIME_OUT_DETECTION_TIME = 5000L;  //  ms

        private volatile double pressedTargetValue = 0;
        private volatile long pressedTime = 0L;
        private volatile boolean stopTimeOutThread = false;
        private volatile long timeOutLastOperation = 0L;
        private volatile double timeOutTargetValue = 0;
        private volatile Thread timeOutThread = null;

        MFTControllableWrapper ( Controllable controllable, MidiFighterTwisterController controller ) {
            super(controllable, controller);
            initChannel();
        }

        @Override
        protected void channelChanged( int oldValue, int newValue ) {
            resetChannel(oldValue);
            initChannel();
        }

        @Override
        protected void currentValueChanged( double oldValue, double newValue ) {
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 0, getChannel(), midiValue(newValue), -1);
        }

        @Override
        protected void disabledChanged( boolean oldValue, boolean newValue ) {
            //  Nothing to be done.
        }

        @Override
        protected void dispose() {
            super.dispose();
            resetChannel();
        }

        @Override
        protected void maxValueChanged( double oldValue, double newValue ) {
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 0, getChannel(), midiValue(getCurrentValue()), -1);
        }

        @Override
        protected void minValueChanged( double oldValue, double newValue ) {
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 0, getChannel(), midiValue(getCurrentValue()), -1);
        }

        @Override
        protected void operatingModeChanged( Controllable.OperatingMode oldValue, Controllable.OperatingMode newValue ) {
            //  Nothing to be done.
        }

        @Override
        protected void tagColorChanged( Color oldValue, Color newValue ) {

            double hue = newValue.getHue();
            double mftHue = 360.0 + ( ( 360.0 - hue ) - 120.0 );
            int mftHueInteger = (int) mftHue;
            double mftHueResidual = mftHue - mftHueInteger;
            
            mftHueInteger %= 360;
            
            int midiValue = 1 + (int) Math.round( 125 * ( mftHueInteger + mftHueResidual ) / 360.0);

            getMFTController().send(ShortMessage.CONTROL_CHANGE, 1, getChannel(), midiValue, -1);

            setTagColor(Color.hsb(hue, 1.0, 1.0));

        }

        @Override
        protected void targetValueChanged( double oldValue, double newValue ) {
            //  Nothing to be done.
        }

        private MidiFighterTwisterController getMFTController() {
            return (MidiFighterTwisterController) getController();
        }

        private void handleReceivedMessage( int midiChannel, int value ) {
            switch ( midiChannel ) {
                case 0:
                    handleReceivedValueChange(value);
                    break;
                case 1:
                    handleReceivedPressureChange(value);
                    break;
                default:
                    LOGGER.warning(MessageFormat.format("Unexpected MIDI channe: {0,number,##0}", midiChannel));
                    break;
            }
        }

        private void handleReceivedPressureChange( int value ) {
            switch ( getOperatingMode() ) {
                case CONTINUOUS:
                    switch ( value ) {
                        case 0x00:  //  Button released.
                            setFineResolution(false);
                            break;
                        case 0x7F:  //  Button pressed.
                            setFineResolution(true);
                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }
                    break;
                case SET_AND_CLICK:

                    timeOutLastOperation = System.currentTimeMillis();

                    switch ( value ) {
                        case 0x00:  //  Button released.

                            if ( System.currentTimeMillis() - pressedTime < CLICK_DETECTION_TIME ) {

                                stopTimeOutThread = true;
                                timeOutThread = null;

                                setTargetValue(pressedTargetValue);
                                getControllable().fireTargeValueSet();

                            }

                            setFineResolution(false);

                            break;
                        case 0x7F:  //  Button pressed.

                            pressedTime = System.currentTimeMillis();
                            pressedTargetValue = getTargetValue();

                            setFineResolution(true);

                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }

                    break;
                case CLIC_SET_AND_RELEASE:
                    switch ( value ) {
                        case 0x00:  //  Button released.
                            setFineResolution(false);
                            getControllable().fireTargeValueSet();
                            break;
                        case 0x7F:  //  Button pressed.
                            setFineResolution(true);
                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }
                    break;
                default:
                    LOGGER.warning("This case should neve happen!");
                    break;
            }
        }

        @SuppressWarnings( "CallToThreadYield" )
        private void handleReceivedValueChange( int value ) {
            switch ( getOperatingMode() ) {
                case CONTINUOUS:

                    switch ( value ) {
                        case 0x3F:  //  Counterclockwise.
                            setTargetValue(getTargetValue() - ( isFineResolution() ? getFineIncrement() : getCoarseIncrement() ));
                            break;
                        case 0x41:  //  Clockwise.
                            setTargetValue(getTargetValue() + ( isFineResolution() ? getFineIncrement() : getCoarseIncrement() ));
                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }

                    getControllable().fireTargeValueSet();

                    break;
                case SET_AND_CLICK:
                    
                    timeOutLastOperation = System.currentTimeMillis();

                    if ( timeOutThread == null ) {

                        stopTimeOutThread = false;
                        timeOutTargetValue = getCurrentValue();
                        timeOutThread =  new Thread(() -> {
                            try {

                                while ( System.currentTimeMillis() - timeOutLastOperation < TIME_OUT_DETECTION_TIME ) {

                                    if ( stopTimeOutThread ) {
                                        return;
                                    }

                                    Thread.yield();

                                }
                                
                                setTargetValue(timeOutTargetValue);
                                
                            } finally {
                                timeOutThread = null;
                            }
                        });

                        timeOutThread.setDaemon(true);
                        timeOutThread.setName("time-out thread – " + getClass().getSimpleName());
                        timeOutThread.setPriority(Thread.currentThread().getPriority() - 2);
                        timeOutThread.start();

                    }

                    switch ( value ) {
                        case 0x3F:  //  Counterclockwise.
                            setTargetValue(getTargetValue() - ( isFineResolution() ? getFineIncrement() : getCoarseIncrement() ));
                            break;
                        case 0x41:  //  Clockwise.
                            setTargetValue(getTargetValue() + ( isFineResolution() ? getFineIncrement() : getCoarseIncrement() ));
                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }

                    break;
                case CLIC_SET_AND_RELEASE:
                    switch ( value ) {
                        case 0x3F:  //  Counterclockwise.
                            if ( isFineResolution() ) {
                                setTargetValue(getTargetValue() - getCoarseIncrement());
                            }
                            break;
                        case 0x41:  //  Clockwise.
                            if ( isFineResolution() ) {
                                setTargetValue(getTargetValue() + getCoarseIncrement());
                            }
                            break;
                        default:
                            LOGGER.warning(MessageFormat.format("This case should neve happen! [{0}]", Integer.toHexString(value)));
                            break;
                    }
                    break;
                default:
                    LOGGER.warning("This case should neve happen!");
                    break;
            }
        }

        private void initChannel() {
            currentValueChanged(0, getCurrentValue());
            tagColorChanged(Color.BLACK, getTagColor());
        }

        private int midiValue ( double value ) {

            double min = getMinValue();
            double max = getMaxValue();

            return (int) Math.round(127.0 * ( value - min ) / ( max - min ));

        }

        private void resetChannel() {
            resetChannel(getChannel());
        }

        private void resetChannel( int channel ) {
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 0, channel, 0, -1);
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 1, channel, 0, -1);
        }

    }

}
