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
package se.ess.knobs.controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;


/**
 * The interface defining the behavior of a controllable object.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public interface Controllable {


    public enum OperatingMode {

        /**
         * This controller should immediately react to changes in target value
         * property.
         */
        CONTINUOUS,

        /**
         * This controller should update the visual representation of the
         * target when the target value property changes. Validation of the
         * target value will happen upon the call of {@link #fireTargeValueSet()}.
         */
        SET_AND_CLICK,

        /**
         * This controller should update the visual representation of the
         * target only when high resolution value is {@code true} and the target
         * value property changes. Validation of the target value will happen
         * upon the call of {@link #fireTargeValueSet()}.
         */
        CLIC_SET_AND_RELEASE

    }


    /*
     * ---- currentValue -------------------------------------------------------
     * The current value of this controllable. It should be displayed somehow in
     * the physical device.
     */
    public DoubleProperty currentValueProperty();

    /*
     * ---- disable ------------------------------------------------------------
     * Tell the physical device that this controllable is disabled.
     */
    public BooleanProperty disableProperty();

    /*
     * ---- highResolution -----------------------------------------------------
     * Tell if the physical device is in hig-resolution mode or not.
     */
    public BooleanProperty highResolutionProperty();

    /*
     * ---- maxValue -----------------------------------------------------------
     * The maximum amount of the current and target values.
     */
    public DoubleProperty maxValueProperty();

    /*
     * ---- minValue -----------------------------------------------------------
     * The minimum amount of the current and target values.
     */
    public DoubleProperty minValueProperty();

    /*
     * ---- operatingMode ------------------------------------------------------
     * Tell this controller how target mode is set and validated.
     */
    public ObjectProperty<OperatingMode> operatingModeProperty();

    /*
     * ---- tagColor -----------------------------------------------------------
     * The color identifying this controllable.
     */
    public ObjectProperty<Color> tagColorProperty();

    /*
     * ---- targetValue --------------------------------------------------------
     * The target value of this controllable. It is sent by the physical device
     * when operated.
     */
    public DoubleProperty targetValueProperty();


    /**
     * Inform this controllable that the target value property was set.
     */
    public void fireTargeValueSet();


}
