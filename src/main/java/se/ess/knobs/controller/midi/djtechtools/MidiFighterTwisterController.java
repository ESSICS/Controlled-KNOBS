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

    private static final Logger LOGGER = Logger.getLogger(MidiFighterTwisterController.class.getName());

    public MidiFighterTwisterController() {
        super(IDENTIFIER);
    }

}
