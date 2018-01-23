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
package se.europeanspallationsource.javafx.control.knobs.controller.midi;


import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import se.europeanspallationsource.javafx.control.knobs.controller.AbstractController;


/**
 * The base class for MIDI controllers.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public abstract class AbstractMIDIController extends AbstractController {

    private static final Logger LOGGER = Logger.getLogger(AbstractMIDIController.class.getName());

    /**
     * This is the MIDI device from which data is coming.
     */
    private MidiDevice fromDevice = null;

    /**
     * This is the MIDI device to which data must be sent.
     */
    private MidiDevice toDevice = null;

    /**
     * The {@link Receiver} to which sending MIDI data.
     */
    private Receiver toReceiver = null;

    /**
     * Create a new instance of this abstract controller.
     *
     * @param identifier The controller's unique identifier.
     */
    public AbstractMIDIController( String identifier ) {
        super(identifier);
        initDevices(identifier);
    }

    @Override
    public void dispose() {
        super.dispose();
        closeDevices();
    }

    /**
     * Default implementation sending MIDI System Reset message to the device.
     * 
     * @return {@code true} on success.
     */
    @Override
    public boolean reset() {
        return send(ShortMessage.SYSTEM_RESET, -1);
    }

    @Override
    @SuppressWarnings( "FinalizeDeclaration" )
    protected void finalize() throws Throwable {
        try {
            closeDevices();
        } finally {
            super.finalize();
        }
    }

    /**
     * Called when a new MIDI message is received from the {@link #fromDevice}.
     *
     * @param message   The MIDI message received.
     * @param timeStamp The timestamp of the received message.
     */
    protected abstract void midiMessageReceived( MidiMessage message, long timeStamp );

    /**
     * Sends a MIDI message and time-stamp to the "to" receiver. If time-stamping
     * is not supported by this receiver, the time-stamp value should be -1.
     *
     * @param message   The MIDI message to be sent.
     * @param timeStamp The timestamp for the message, in microseconds.
     * @return {@code true} on success.
     */
    protected boolean send( MidiMessage message, long timeStamp ) {
        
        if ( toReceiver != null ) {
            toReceiver.send(message, timeStamp);
            return true;
        } else if ( toDevice != null ) {
            LOGGER.fine(MessageFormat.format(
                "MIDI message cannot be sent. Receiver not available for MIDI \"to\" device {0}. {2}",
                toDevice.getClass().getName(),
                deviceInfoForLogger(toDevice)
            ));
        } else {
            LOGGER.fine("MIDI message cannot be sent. MIDI \"to\" device doesn't exist.");
        }

        return false;

    }

    /**
     * Sends a MIDI message and time-stamp to the "to" receiver. If time-stamping
     * is not supported by this receiver, the time-stamp value should be -1.
     *
     * @param midiStatus The MIDI status byte.
     * @param timeStamp  The timestamp for the message, in microseconds.
     * @return {@code true} on success.
     * @see ShortMessage
     */
    protected boolean send( int midiStatus, long timeStamp ) {

        try {
            return send(new ShortMessage(midiStatus), timeStamp);
        } catch ( InvalidMidiDataException ex ) {
            LOGGER.log(Level.WARNING, "This exception should never happens.", ex);
        }

        return false;

    }

    /**
     * Sends a MIDI message and time-stamp to the "to" receiver. If time-stamping
     * is not supported by this receiver, the time-stamp value should be -1.
     *
     * @param midiStatus The MIDI status byte.
     * @param data1      The first data byte.
     * @param data2      The second data byte.
     * @param timeStamp  The timestamp for the message, in microseconds.
     * @return {@code true} on success.
     * @see ShortMessage
     */
    protected boolean send( int midiStatus, int data1, int data2, long timeStamp ) {

        try {
            return send(new ShortMessage(midiStatus, data1, data2), timeStamp);
        } catch ( InvalidMidiDataException ex ) {
            LOGGER.log(Level.WARNING, "This exception should never happens.", ex);
        }

        return false;

    }

    /**
     * Sends a MIDI message and time-stamp to the "to" receiver. If time-stamping
     * is not supported by this receiver, the time-stamp value should be -1.
     *
     * @param midiCommand The MIDI command represented by the message.
     * @param midiChannel The MIDI channel associated with the message.
     * @param data1       The first data byte.
     * @param data2       The second data byte.
     * @param timeStamp   The timestamp for the message, in microseconds.
     * @return {@code true} on success.
     * @see ShortMessage
     */
    protected boolean send( int midiCommand, int midiChannel, int data1, int data2, long timeStamp ) {

        try {
            return send(new ShortMessage(midiCommand, midiChannel, data1, data2), timeStamp);
        } catch ( InvalidMidiDataException ex ) {
            LOGGER.log(Level.WARNING, "This exception should never happens.", ex);
        }

        return false;

    }

    private void closeDevices() {

        if ( fromDevice != null && fromDevice.isOpen() ) {
            fromDevice.close();
        }

        if ( toDevice != null && toDevice.isOpen() ) {
            toDevice.close();
        }

    }

    private String deviceInfoForLogger( MidiDevice device ) {
        return deviceInfoForLogger(device.getDeviceInfo(), device.isOpen() ? "OPEN" : "CLOSED");
    }

    private String deviceInfoForLogger( MidiDevice.Info info ) {
        return deviceInfoForLogger(info, "–");
    }

    private String deviceInfoForLogger( MidiDevice.Info info, String status ) {
        return MessageFormat.format(
            "\n"
            + "\t     Device: {0}\n"
            + "\tDescription: {1}\n"
            + "\t     Vendor: {2}\n"
            + "\t    Version: {3}\n"
            + "\t     Status: {4}",
            info.getName(),
            info.getDescription(),
            info.getVendor(),
            info.getVersion(),
            status
        );
    }

    private void initDevices( String identifier ) {

        MidiDevice.Info[] midiDeviceInfos = MidiSystem.getMidiDeviceInfo();

        for ( MidiDevice.Info info : midiDeviceInfos ) {
            if ( Objects.equals(identifier, info.getName()) ) {
                try {

                    MidiDevice device = MidiSystem.getMidiDevice(info);

                    if ( !( device instanceof Sequencer ) && !( device instanceof Synthesizer ) ) {
                        //  The device is a MIDI port.
                        if ( fromDevice == null && device.getMaxTransmitters() != 0 ) {
                            fromDevice = device;
                        } else if ( toDevice == null && device.getMaxReceivers() != 0 ) {
                            toDevice = device;
                        } else if ( fromDevice != null || toDevice != null ) {
                            LOGGER.fine(MessageFormat.format(
                                "Device \"{0}\" already found, the following is skipped [{1}] {2}",
                                identifier,
                                info.getClass().getName(),
                                deviceInfoForLogger(info)
                            ));
                        } else {
                            LOGGER.fine(MessageFormat.format(
                                "Device \"{0}\" has no transmitters nor receivers [{1}] {2}",
                                identifier,
                                info.getClass().getName(),
                                deviceInfoForLogger(info)
                            ));
                        }
                    } else {
                        LOGGER.fine(MessageFormat.format(
                            "Device \"{0}\" is not a MIDI port [{1}] {2}",
                            identifier,
                            info.getClass().getName(),
                            deviceInfoForLogger(info)
                        ));
                    }

                } catch ( MidiUnavailableException ex ) {
                    LOGGER.fine(MessageFormat.format(
                        "MIDI device {0} is unavailable [{1}] {2}",
                        info.getClass().getName(),
                        ex.getMessage(),
                        deviceInfoForLogger(info)
                    ));
                }
            }
        }

        if ( fromDevice != null ) {
            try {

                fromDevice.open();

                try {

                    Transmitter fromTransmitter = fromDevice.getTransmitter();

                    fromTransmitter.setReceiver(new Receiver() {
                        @Override
                        public void close() {
                        }

                        @Override
                        public void send( MidiMessage message, long timeStamp ) {
                            midiMessageReceived(message, timeStamp);
                        }
                    });

                } catch ( MidiUnavailableException ex ) {
                    LOGGER.fine(MessageFormat.format(
                        "Transmitter not available for MIDI \"from\" device {0} [{1}] {2}",
                        fromDevice.getClass().getName(),
                        ex.getMessage(),
                        deviceInfoForLogger(fromDevice)
                    ));
                }

            } catch ( MidiUnavailableException ex ) {
                LOGGER.fine(MessageFormat.format(
                    "MIDI \"from\" device {0} cannot be opened [{1}] {2}",
                    fromDevice.getClass().getName(),
                    ex.getMessage(),
                    deviceInfoForLogger(fromDevice)
                ));
            }
        } else {
            LOGGER.fine("MIDI \"from\" device doesn't exist.");
        }

        if ( toDevice != null ) {
            try {

                toDevice.open();

                try {
                    toReceiver = toDevice.getReceiver();
                } catch ( MidiUnavailableException ex ) {
                    LOGGER.fine(MessageFormat.format(
                        "Receiver not available for MIDI \"to\" device {0} [{1}] {2}",
                        toDevice.getClass().getName(),
                        ex.getMessage(),
                        deviceInfoForLogger(toDevice)
                    ));
                }

            } catch ( MidiUnavailableException ex ) {
                LOGGER.fine(MessageFormat.format(
                    "MIDI \"to\" device {0} cannot be opened [{1}] {2}",
                    toDevice.getClass().getName(),
                    ex.getMessage(),
                    deviceInfoForLogger(toDevice)
                ));
            }
        } else {
            LOGGER.fine("MIDI \"to\" device doesn't exist.");
        }

    }

}
