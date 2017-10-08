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
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import se.ess.knobs.Knob;
import se.ess.knobs.controller.Controllable;
import se.ess.knobs.controller.Controllers;
import se.ess.knobs.controller.spi.Controller;

import static se.ess.knobs.controller.Controllable.OperatingMode.CONTINUOUS;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 23 Aug 2017
 */
@SuppressWarnings( "ClassWithoutLogger" )
public class ControlledKnob extends Knob implements Controllable {

    public static final String CONTROLLER_NONE = "NONE";
    public static final Color DEFAULT_CURRENT_VALUE_COLOR = Color.WHITE.deriveColor(0, 1, 1, 0.95);

    private static final Image IMG_CLIC_SET_AND_RELEASE = new Image(ControlledKnob.class.getResource("images/CLIC_SET_AND_RELEASE.png").toExternalForm());
    private static final Image IMG_CONTINUOUS = new Image(ControlledKnob.class.getResource("images/CONTINUOUS.png").toExternalForm());
    private static final Image IMG_SET_AND_CLICK = new Image(ControlledKnob.class.getResource("images/SET_AND_CLICK.png").toExternalForm());

    private ImageView operatinModeView;

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
     * ---- controller ---------------------------------------------------------
     */
    private final StringProperty controller = new SimpleStringProperty(this, "controller", CONTROLLER_NONE) {
        @Override
        protected void invalidated() {

            String val = get();

            if ( val == null || !Controllers.get().getIdentifiers().contains(val) ) {
                set(CONTROLLER_NONE);
            }

        }

    };
    private final ChangeListener<? super String> controllerListener = ( observable, oldValue, newValue ) -> {

        if ( !CONTROLLER_NONE.equals(oldValue) ) {
            Controllers.get().getController(oldValue).remove(this);
        }

        if ( !CONTROLLER_NONE.equals(newValue) ) {
            Controllers.get().getController(newValue).add(this);
        }

    };

    public StringProperty controllerProperty() {
        return controller;
    }

    public String getController() {
        return controller.get();
    }

    public void setController( String controller ) {
        this.controller.set(controller);
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

            switch ( get() ) {
                case CLIC_SET_AND_RELEASE:
                    operatinModeView.setImage(IMG_CLIC_SET_AND_RELEASE);
                    break;
                case CONTINUOUS:
                    operatinModeView.setImage(IMG_CONTINUOUS);
                    break;
                case SET_AND_CLICK:
                    operatinModeView.setImage(IMG_SET_AND_CLICK);
                    break;
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

    public void setOperatingMode( OperatingMode operatingMode ) {
        this.operatingMode.set(operatingMode);
    }

    /*
     * ---- tagVisible ---------------------------------------------------------
     */
    private BooleanProperty roTagVisible = null;

    @Override
    public BooleanProperty tagVisibleProperty() {

        if ( roTagVisible == null ) {

            roTagVisible = new ReadOnlyBooleanWrapper(this, "tagVisible");

            roTagVisible.bind(super.tagVisibleProperty());

        }

        return roTagVisible;

    }

    /*
     * -------------------------------------------------------------------------
     */
    public void dispose() {

        String c = getController();

        if ( !CONTROLLER_NONE.equals(c) ) {

            Controller cntrl = Controllers.get().getController(c);
            if ( cntrl != null ) {
                cntrl.remove(this);
            }

        }

    }

    @Override
    @SuppressWarnings( "FinalizeDeclaration" )
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    @Override
    protected void initComponents() {

        super.initComponents();

        operatinModeView = new ImageView();

        operatinModeView.setImage(IMG_CONTINUOUS);
        operatinModeView.setBlendMode(BlendMode.MULTIPLY);
        operatinModeView.setSmooth(true);

        pane.getChildren().add(operatinModeView);

    }

    @Override
    protected void resize() {
        
        super.resize();

        double width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        double height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        size = width < height ? width : height;

        if ( size > 0 ) {

            double imgSize = size / 11;

            operatinModeView.setFitWidth(imgSize);
            operatinModeView.setFitHeight(imgSize);

            operatinModeView.relocate(( size - imgSize) * 0.5, size * 0.815);

        }

    }

    private void init() {
        super.setTagVisible(true);
        super.setCurrentValueColor(DEFAULT_CURRENT_VALUE_COLOR);
        controllerProperty().addListener(controllerListener);
    }

}
