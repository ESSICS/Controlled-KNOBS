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


import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.ess.knobs.controller.spi.Controller;


/**
 * Factory class to get access to {@link Controller}'s instances.
 *
 * @author claudiorosati
 */
public class Controllers {

    private static final Map<String, Controller> CONTROLLERS = new HashMap<>(1);
    private static final Logger LOGGER = Logger.getLogger(Controllers.class.getName());

    public static Controllers get() {
        return ControllersHolder.INSTANCE;
    }

    private Controllers() {

        try {

            ServiceLoader<Controller> loader = ServiceLoader.load(Controller.class);

            for ( Controller c : loader ) {

                String id = c.getIdentifier();

                if ( id != null && !id.isEmpty() ) {

                    if ( !CONTROLLERS.containsKey(id) ) {
                        CONTROLLERS.put(id, c);
                    } else {
                        LOGGER.warning(MessageFormat.format(
                            "Controller \"{0}\" already exists.\n{1} implementation is skipped",
                            id,
                            c.getClass().getName()
                        ));
                    }

                    LOGGER.info(MessageFormat.format("Resetting controller \"{0}\"…", id));

                    if ( !c.reset() ) {
                        LOGGER.warning(MessageFormat.format("Resetting controller \"{0}\" failed!", id));
                    } else {
                        LOGGER.info(MessageFormat.format("Successful reset of controller \"{0}\"!", id));
                    }


                }

            }

        } catch ( Throwable ex ) {
            LOGGER.log(Level.SEVERE, "Unable to load controllers.", ex);
        }

    }

    /**
     * Check the existence of a {@link Controller} whose identifier is the given
     * one.
     *
     * @param identifier The identifier of the controller to be checked.
     * @return {@code true} if the given {@code identifier} refers to an
     *         existing {@link Controller}, {@code false} otherwise.
     */
    public boolean exists( String identifier ) {
        return CONTROLLERS.containsKey(identifier);
    }

    /**
     * Return the {@link Controller} matching the given {@code identifier}.
     *
     * @param identifier The identifier of the {@link Controller} to be returned.
     * @return The {@link Controller} matching the given {@code identifier}, or
     *         {@code null} if no such controller exists.
     */
    public Controller getController( String identifier ) {
        return CONTROLLERS.get(identifier);
    }

    /**
     * @return A {@link Collection} of discovered {@link Controller}s.
     */
    public Collection<Controller> getControllers() {
        return Collections.unmodifiableCollection(CONTROLLERS.values());
    }

    /**
     * @return The {@link Set} containing the identifiers of discovered
     *         {@link Controller}s.
     */
    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(CONTROLLERS.keySet());
    }

    @SuppressWarnings( "UtilityClassWithoutPrivateConstructor" )
    private static class ControllersHolder {

        private static final Controllers INSTANCE = new Controllers();

    }

}
