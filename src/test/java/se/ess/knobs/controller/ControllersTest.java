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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.ess.knobs.controller.midi.djtechtools.MidiFighterTwisterController;
import se.ess.knobs.controller.spi.Controller;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 * @author claudiorosati
 */
public class ControllersTest {

    private static final Logger LOGGER = Logger.getLogger(ControllersTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    public ControllersTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of exists method, of class Controllers.
     */
    @Test
    public void testExists() {
        assertThat(Controllers.get().exists(MidiFighterTwisterController.IDENTIFIER)).isTrue();
        assertThat(Controllers.get().exists("Pippo")).isFalse();
    }

    /**
     * Test of get method, of class Controllers.
     */
    @Test
    public void testGet() {

        Controllers c = Controllers.get();

        assertThat(c)
            .isNotNull()
            .isInstanceOf(Controllers.class);

    }

    /**
     * Test of getController method, of class Controllers.
     */
    @Test
    public void testGetController() {

        Controller controller = Controllers.get().getController(MidiFighterTwisterController.IDENTIFIER);

        assertThat(controller)
            .isNotNull()
            .hasFieldOrPropertyWithValue("identifier", MidiFighterTwisterController.IDENTIFIER);

        controller = Controllers.get().getController("Pippo");

        assertThat(controller)
            .isNull();

    }

    /**
     * Test of getControllers method, of class Controllers.
     */
    @Test
    public void testGetControllers() {

        StringBuilder builder = new StringBuilder(128);
        Collection<Controller> controllers = Controllers.get().getControllers();

        builder.append("Found controllers:");
        controllers.stream().forEach(c -> builder.append("\n\t").append(c.getIdentifier()));
        LOGGER.info(builder.toString());

        assertThat(new ArrayList<>(controllers))
            .hasSize(1)
            .doesNotContainNull();

    }

    /**
     * Test of getIdentifiers method, of class Controllers.
     */
    @Test
    public void testGetIdentifiers() {

        StringBuilder builder = new StringBuilder(128);
        Set<String> identifiers = Controllers.get().getIdentifiers();

        builder.append("Found controllers:");
        identifiers.stream().forEach(id -> builder.append("\n\t").append(id));
        LOGGER.info(builder.toString());

        assertThat(new ArrayList<>(identifiers))
            .hasSize(1)
            .contains(MidiFighterTwisterController.IDENTIFIER);

    }

}
