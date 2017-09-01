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


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import se.ess.knobs.controller.spi.Controller;


/**
 * The base class for MIDI controllers.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 30 Aug 2017
 */
public abstract class AbstractController implements Controller {

    private static final Executor EXECUTOR = Executors.newFixedThreadPool(8);

    private final Map<Controllable, AbstractControllableWrapper> controllableMap = Collections.synchronizedMap(new HashMap<>(16));
    private final String identifier;

    /**
     * Create a new instance of this abstract controller.
     *
     * @param identifier The controller's unique identifier.
     */
    public AbstractController( String identifier ) {
        this.identifier = identifier;
    }

    @Override
    public void add( Controllable controllable ) {
        controllableMap.put(controllable, createWrapper(controllable));
    }

    @Override
    public void dispose() {

        Set<Controllable> controllables = new HashSet<>(getControllables());

        controllables.stream().forEach(c -> remove(c));

    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void remove( Controllable controllable ) {
        controllableMap.remove(controllable).dispose();
    }

    /**
     * Factory method used to create a new instance of 
     * {@link AbstractControllableWrapper}.
     *
     * @param controllable The {@link Controllable} to be wrapped.
     * @return An instance of {@link AbstractControllableWrapper}.
     */
    protected abstract AbstractControllableWrapper createWrapper( Controllable controllable );

    protected Set<Controllable> getControllables() {
        return Collections.unmodifiableSet(controllableMap.keySet());
    }

    protected Collection<AbstractControllableWrapper> getWrappers() {
        return Collections.unmodifiableCollection(controllableMap.values());
    }

    @SuppressWarnings( "ProtectedInnerClass" )
    protected static abstract class AbstractControllableWrapper {


        /*
         * ---- controllable ---------------------------------------------------
         */
        private final Controllable controllable;

        public Controllable getControllable() {
            return controllable;
        }


        /*
         * ---- channel --------------------------------------------------------
         */
        private volatile int channel;
        private final ChangeListener<? super Number> channelListener = ( observable, oldValue, newValue ) -> {
            channel = newValue.intValue();
            EXECUTOR.execute(() -> channelChanged(oldValue.intValue(), channel));
        };

        public int getChannel() {
            return channel;
        }

        /**
         * Invoked when the {@link Controllable}'s channel number changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void channelChanged( int oldValue, int newValue );


        /*
         * ---- coarseIncrement ------------------------------------------------
         */
        private volatile double coarseIncrement;
        private final ChangeListener<? super Number> coarseIncrementListener = ( observable, oldValue, newValue ) -> {
            coarseIncrement = newValue.doubleValue();
        };

        public double getCoarseIncrement() {
            return coarseIncrement;
        }


        /*
         * ---- coarseIncrement ------------------------------------------------
         */
        private final AbstractController controller;

        protected AbstractController getController() {
            return controller;
        }


        /*
         * ---- currentValue ---------------------------------------------------
         */
        private volatile double currentValue;
        private final ChangeListener<? super Number> currentValueListener = ( observable, oldValue, newValue ) -> {
            currentValue = newValue.doubleValue();
            EXECUTOR.execute(() -> currentValueChanged(oldValue.doubleValue(), currentValue));
        };

        public double getCurrentValue() {
            return currentValue;
        }

        /**
         * Invoked when the {@link Controllable}'s current value changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void currentValueChanged( double oldValue, double newValue );


        /*
         * ---- disable --------------------------------------------------------
         * Tell the physical device that this controllable is disabled.
         */
        private volatile boolean disabled;
        private final ChangeListener<? super Boolean> disabledListener = ( observable, oldValue, newValue ) -> {
            disabled = newValue;
            EXECUTOR.execute(() -> disabledChanged(oldValue, disabled));
        };

        public boolean isDisabled() {
            return disabled;
        }

        /**
         * Invoked when the {@link Controllable}'s disable value changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void disabledChanged( boolean oldValue, boolean newValue );


        /*
         * ---- fineIncrement --------------------------------------------------
         */
        private volatile double fineIncrement;
        private final ChangeListener<? super Number> fineIncrementListener = ( observable, oldValue, newValue ) -> {
            fineIncrement = newValue.doubleValue();
        };

        public double getFineIncrement() {
            return fineIncrement;
        }


        /*
         * ---- fineResolution -------------------------------------------------
         */
        private boolean fineResolution = false;

        public boolean isFineResolution() {
            return fineResolution;
        }

        public void setFineResolution( boolean fineResolution ) {

            this.fineResolution = fineResolution;

            Platform.runLater(() -> controllable.fineResolutionProperty().set(this.fineResolution));

        }


        /*
         * ---- maxValue -------------------------------------------------------
         */
        private volatile double maxValue;
        private final ChangeListener<? super Number> maxValueListener = ( observable, oldValue, newValue ) -> {
            maxValue = newValue.doubleValue();
            EXECUTOR.execute(() -> maxValueChanged(oldValue.doubleValue(), maxValue));
        };

        public double getMaxValue() {
            return maxValue;
        }

        /**
         * Invoked when the {@link Controllable}'s maximum value changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void maxValueChanged( double oldValue, double newValue );


        /*
         * ---- minValue -------------------------------------------------------
         */
        private volatile double minValue;
        private final ChangeListener<? super Number> minValueListener = ( observable, oldValue, newValue ) -> {
            minValue = newValue.doubleValue();
            EXECUTOR.execute(() -> minValueChanged(oldValue.doubleValue(), minValue));
        };

        public double getMinValue() {
            return minValue;
        }

        /**
         * Invoked when the {@link Controllable}'s minimum value changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void minValueChanged( double oldValue, double newValue );


        /*
         * ---- operatingMode --------------------------------------------------
         */
        private volatile Controllable.OperatingMode operatingMode;
        private final ChangeListener<? super Controllable.OperatingMode> operatingModeListener = ( observable, oldValue, newValue ) -> {
            operatingMode = newValue;
            EXECUTOR.execute(() -> operatingModeChanged(oldValue, operatingMode));
        };

        public Controllable.OperatingMode getOperatingMode() {
            return operatingMode;
        }

        /**
         * Invoked when the {@link Controllable}'s operating mode changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void operatingModeChanged( Controllable.OperatingMode oldValue, Controllable.OperatingMode newValue );


        /*
         * ---- tagColor -------------------------------------------------------
         */
        private volatile boolean updatingTegColor = false;
        private volatile Color tagColor;
        private final ChangeListener<? super Color> tagColorListener = ( observable, oldValue, newValue ) -> {

            tagColor = newValue;

            if ( ! updatingTegColor ) {
                EXECUTOR.execute(() -> tagColorChanged(oldValue, tagColor));
            }

        };

        public Color getTagColor() {
            return tagColor;
        }

        public void setTagColor( Color tagColor ) {

            this.updatingTegColor = true;
            this.tagColor = tagColor;

            Platform.runLater(() -> {
                controllable.tagColorProperty().set(this.tagColor);
                updatingTegColor = false;
            });

        }

        /**
         * Invoked when the {@link Controllable}'s tag color changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void tagColorChanged( Color oldValue, Color newValue );


        /*
         * ---- targetValue ---------------------------------------------------
         */
        private volatile double targetValue;
        private final ChangeListener<? super Number> targetValueListener = ( observable, oldValue, newValue ) -> {
            targetValue = newValue.doubleValue();
            EXECUTOR.execute(() -> targetValueChanged(oldValue.doubleValue(), targetValue));
        };

        public double getTargetValue() {
            return targetValue;
        }

        public void setTargetValue( double targetValue ) {

            this.targetValue = targetValue;

            Platform.runLater(() -> controllable.targetValueProperty().set(this.targetValue));

        }

        /**
         * Invoked when the {@link Controllable}'s target value changed.
         *
         * @param oldValue The old value.
         * @param newValue The new value.
         */
        protected abstract void targetValueChanged( double oldValue, double newValue );


        /*
         * ---------------------------------------------------------------------
         */
        protected AbstractControllableWrapper( Controllable controllable, AbstractController controller ) {

            this.controllable = controllable;
            this.controller = controller;

            init();

        }

        /**
         * Called when this wrapper is disposed to implement specific resource
         * disposal.
         * <P>
         * <B>Note:</B> always call {@code super.dispose()}.
         */
        protected void dispose() {
            controllable.targetValueProperty().removeListener(targetValueListener);
            controllable.tagColorProperty().removeListener(tagColorListener);
            controllable.operatingModeProperty().removeListener(operatingModeListener);
            controllable.minValueProperty().removeListener(minValueListener);
            controllable.maxValueProperty().removeListener(maxValueListener);
            controllable.fineIncrementProperty().removeListener(fineIncrementListener);
            controllable.disabledProperty().removeListener(disabledListener);
            controllable.currentValueProperty().removeListener(currentValueListener);
            controllable.coarseIncrementProperty().removeListener(coarseIncrementListener);
            controllable.channelProperty().removeListener(channelListener);
        }

        private void init() {

            this.channel = controllable.channelProperty().get();
            this.coarseIncrement = controllable.coarseIncrementProperty().get();
            this.currentValue = controllable.currentValueProperty().get();
            this.disabled = controllable.disabledProperty().get();
            this.fineIncrement = controllable.fineIncrementProperty().get();
            this.maxValue = controllable.maxValueProperty().get();
            this.minValue = controllable.minValueProperty().get();
            this.operatingMode = controllable.operatingModeProperty().get();
            this.tagColor = controllable.tagColorProperty().get();
            this.targetValue = controllable.targetValueProperty().get();

            controllable.channelProperty().addListener(channelListener);
            controllable.coarseIncrementProperty().addListener(coarseIncrementListener);
            controllable.currentValueProperty().addListener(currentValueListener);
            controllable.disabledProperty().addListener(disabledListener);
            controllable.fineIncrementProperty().addListener(fineIncrementListener);
            controllable.maxValueProperty().addListener(maxValueListener);
            controllable.minValueProperty().addListener(minValueListener);
            controllable.operatingModeProperty().addListener(operatingModeListener);
            controllable.tagColorProperty().addListener(tagColorListener);
            controllable.targetValueProperty().addListener(targetValueListener);

            setFineResolution(false);

        }
        

    }

}
