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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;
import se.ess.knobs.KnobPropertyEditorFactory;
import se.ess.knobs.controller.Controllers;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 01 Sep 2017
 */
@SuppressWarnings( "ClassWithoutLogger" )
public class ControlledKnobPropertyEditorFactory extends KnobPropertyEditorFactory {

    private static Collection<String> controllers() {

        List<String> controllers = new ArrayList<>(Controllers.get().getIdentifiers());

        Collections.sort(controllers);

        controllers.add(0, ControlledKnob.CONTROLLER_NONE);

        return controllers;

    }

    @Override
    public PropertyEditor<?> call( PropertySheet.Item item ) {

        if ( "controller".equals(item.getName()) ) {
            return Editors.createChoiceEditor(item, controllers());
        } else {
            return super.call(item);
        }

    }

}
