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
//  TODO: CR: TBD
    }

    private static class MFTControllableWrapper extends AbstractControllableWrapper {

        MFTControllableWrapper ( Controllable controllable, MidiFighterTwisterController controller ) {
            super(controllable, controller);
            initChannel();
        }

        @Override
        protected void channelChanged( int oldValue, int newValue ) {
            resetChannel(oldValue);
            getMFTController().send(ShortMessage.CONTROL_CHANGE, 0, newValue, midiValue(getCurrentValue()), -1);
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
