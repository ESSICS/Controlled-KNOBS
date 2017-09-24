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
package se.ess.knobs.controller.spi;


import se.ess.knobs.controller.Controllable;


/**
 * The interface defining the behavior of a controller.
 *
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 28 Aug 2017
 */
public interface Controller {

    /**
     * Add the given {@link Controllable} to the list of controlled objects.
     *
     * @param controllable The object to be controlled by this controller.
     */
    public void add( Controllable controllable );

    /**
     * Called to perform specific resource disposal.
     * <P>
     * <B>Note:</B> always call {@code super.dispose()}.
     */
    public void dispose();

    /**
     * @return The unique identifier of this controller.
     */
    public String getIdentifier();

    /**
     * Remove the given {@link Controllable} from the list of controlled objects.
     *
     * @param controllable The object controlled by this controller.
     */
    public void remove( Controllable controllable );

    /**
     * Put the controller in its initial, well-known state.
     */
    public void reset();

}
