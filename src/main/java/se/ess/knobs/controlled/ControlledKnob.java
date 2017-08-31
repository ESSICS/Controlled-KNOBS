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


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import se.ess.knobs.Knob;
import se.ess.knobs.controller.Controllable;

import static se.ess.knobs.controller.Controllable.OperatingMode.CONTINUOUS;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 23 Aug 2017
 */
@SuppressWarnings( "ClassWithoutLogger" )
public class ControlledKnob extends Knob implements Controllable {
    
    public ControlledKnob() {
        init();
    }

    /*
     * ---- channel ------------------------------------------------------------
     */
    private final IntegerProperty channel = new SimpleIntegerProperty(this, "channel", 0);

    @Override
    public IntegerProperty channelProperty() {
        return channel;
    }

    public int getChannel() {
        return channel.get();
    }

    public void setChannel( int channel ) {
        this.channel.set(channel);
    }

    /*
     * ---- coarseIncrement ----------------------------------------------------
     */
    private final DoubleProperty coarseIncrement = new SimpleDoubleProperty(this, "coarseIncrement", 1);

    @Override
    public DoubleProperty coarseIncrementProperty() {
        return coarseIncrement;
    }

    public double getCoarseIncrement() {
        return coarseIncrement.get();
    }

    public void setCoarseIncrement( double coarseIncrement ) {
        this.coarseIncrement.set(coarseIncrement);
    }

    /*
     * ---- fineIncrement ------------------------------------------------------
     */
    private final DoubleProperty fineIncrement = new SimpleDoubleProperty(this, "fineIncrement", 0.05);

    @Override
    public DoubleProperty fineIncrementProperty() {
        return fineIncrement;
    }

    public double getFineIncrement() {
        return fineIncrement.get();
    }

    public void setFineIncrement( double fineIncrement ) {
        this.fineIncrement.set(fineIncrement);
    }

    /*
     * ---- fineResolution -----------------------------------------------------
     */
    @Override
    public BooleanProperty fineResolutionProperty() {
        return selectedProperty();
    }

    /*
     * ---- operatingMode ------------------------------------------------------
     */
    private final ObjectProperty<OperatingMode> operatingMode = new SimpleObjectProperty<OperatingMode>(this, "operatingMode", CONTINUOUS) {
        @Override
        protected void invalidated() {
            if ( get() == null ) {
                set(CONTINUOUS);
            }
        }
    };

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
    private void init() {
        setTagVisible(true);
    }

}
