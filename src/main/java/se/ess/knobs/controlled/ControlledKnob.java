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
package se.ess.knobs.controlled;


import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import se.ess.knobs.Knob;
import se.ess.knobs.controller.Controllable;

import static se.ess.knobs.controller.Controllable.OperatingMode.CONTINUOUS;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 23 Aug 2017
 */
public class ControlledKnob extends Knob implements Controllable {
    
    private static final Logger LOGGER = Logger.getLogger(ControlledKnob.class.getName());

    /*
     * ---- highResolution -----------------------------------------------------
     */
    private final BooleanProperty highResolution = new SimpleBooleanProperty(this, "highResolution", false);

    public BooleanProperty highResolutionProperty() {
        return highResolution;
    }

    public boolean isHighResolution() {
        return highResolution.get();
    }

    public void setHighResolution( boolean highResolution ) {
        this.highResolution.set(highResolution);
    }

    /*
     * ---- highResolution -----------------------------------------------------
     */
    private final ObjectProperty<OperatingMode> operatingMode = new SimpleObjectProperty<>(this, "operatingMode", CONTINUOUS);

    @Override
    public ObjectProperty<OperatingMode> operatingModeProperty() {
        return operatingMode;
    }

    public OperatingMode getOperatingMode() {
        return operatingMode.get();
    }

    public void setOperatingMode ( OperatingMode operatingMode ) {
        this.operatingMode.set(operatingMode);
    }

    /*
     * -------------------------------------------------------------------------
     */

}
