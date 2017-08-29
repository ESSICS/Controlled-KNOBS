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


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import se.ess.knobs.controller.Controllable;
import se.ess.knobs.controller.spi.Controller;


/**
 * The base class for MIDI controllers.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public abstract class AbstractMIDIController implements Controller {

    private final Map<Controllable, ControllableWrapper> controllableMap = Collections.synchronizedMap(new HashMap<>(16));
    private final String identifier;

    /**
     * Create a new instance of this abstract controller.
     *
     * @param identifier The controller's unique identifier.
     */
    public AbstractMIDIController( String identifier ) {
        this.identifier = identifier;
    }

    @Override
    public void add( Controllable controllable ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void remove( Controllable controllable ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected Collection<ControllableWrapper> getWrappers() {
        return Collections.unmodifiableCollection(controllableMap.values());
    }

    @SuppressWarnings( "ProtectedInnerClass" )
    protected static class ControllableWrapper {

        private final Controllable controllable;
        private volatile double currentValue;

        private ControllableWrapper( Controllable controllable ) {

            this.controllable = controllable;

            controllable.currentValueProperty().addListener(( observable, oldValue, newValue ) -> {
                synchronized ( ControllableWrapper.this ) {
                    currentValue = newValue.doubleValue();
                }
            });

        }

        public Controllable getControllable() {
            return controllable;
        }

        public synchronized double getCurrentValue() {
            return currentValue;
        }

    }

}
