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
package se.ess.knobs.controller.midi;


import se.ess.knobs.controller.AbstractController;


/**
 * The base class for MIDI controllers.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public abstract class AbstractMIDIController extends AbstractController {

    /**
     * Create a new instance of this abstract controller.
     *
     * @param identifier The controller's unique identifier.
     */
    public AbstractMIDIController( String identifier ) {

        super(identifier);

        //  DORO: CR: acquire MIDI devices

    }

}
