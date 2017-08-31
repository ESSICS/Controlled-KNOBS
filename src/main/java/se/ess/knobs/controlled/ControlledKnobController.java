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


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import se.ess.knobs.Knob;
import se.ess.knobs.KnobPropertyEditorFactory;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 23 Aug 2017
 */
public class ControlledKnobController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ControlledKnobController.class.getName());
    private static final Border SELECTED_BORDER = new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM, new Insets(-3)));

    private final Map<Class<?>, String> categories = new HashMap<>(5);
    @FXML private GridPane knobContainer;
    private final List<List<BeanProperty>> properties = new ArrayList<>(4);
    @FXML private PropertySheet propertySheet;
    private int selectedKnob = -1;
    private final Color[] tags = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean[] updateCurrentValue = { false, false, false, false };
    @FXML private Label windowFocusStatus;

    public ControlledKnobController() {
        categories.put(ControlledKnob.class, "\u200BControlledKnob");
        categories.put(          Knob.class, "\u200B\u200BKnob");
        categories.put(        Region.class, "\u200B\u200B\u200BRegion");
        categories.put(        Parent.class, "\u200B\u200B\u200B\u200BParent");
        categories.put(          Node.class, "\u200B\u200B\u200B\u200B\u200BNode");
        categories.put(        Object.class, "\u200B\u200B\u200B\u200B\u200B\u200BObject");
    }

    @Override
    public void initialize( URL location, ResourceBundle resources ) {

        propertySheet.setPropertyEditorFactory(new KnobPropertyEditorFactory());

        ControlledKnob[] knobs = new ControlledKnob[4];
        
        long beforeTime = System.currentTimeMillis();

        for ( int i = 0; i < knobs.length; i++ ) {

            final int index = i;

            knobs[index] = ControlledKnobBuilder.create()
                .channel(index)
                .tagColor(tags[index])
                .onAdjusted(e -> LOGGER.info(MessageFormat.format("Current value [{0}] reached target: {1}", index, ((Knob) e.getSource()).getCurrentValue())))
                .onTargetSet(e -> {
                    LOGGER.info(MessageFormat.format("Target [{0}] changed: {1}", index, ((Knob) e.getSource()).getTargetValue()));
                    updateCurrentValue[index] = true;
                })
                .build();


            try {

                BeanInfo beanInfo = Introspector.getBeanInfo(ControlledKnob.class, Object.class);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                List<BeanProperty> pList = new ArrayList<>(propertyDescriptors.length);

                properties.add(pList);

                for ( PropertyDescriptor p : propertyDescriptors ) {
                    try {
                        if ( p.getReadMethod() != null && p.getWriteMethod() != null ) {
                            p.setValue(BeanProperty.CATEGORY_LABEL_KEY, categories.get(p.getReadMethod().getDeclaringClass()));
                            pList.add(new BeanProperty(knobs[index], p));
                        }
                    } catch ( Exception iex ) {
                        LOGGER.log(Level.SEVERE, MessageFormat.format("Unable to handle property \"{0}\" [{1}].", p.getName(), iex.getMessage()));
                    }
                }

            } catch ( IntrospectionException ex ) {
                LOGGER.log(Level.SEVERE, "Unable to initialize the controller.", ex);
            }

            knobs[index].setOnMouseClicked(e -> {

                if ( selectedKnob != -1 ) {
                    knobs[selectedKnob].setBorder(Border.EMPTY);
                    propertySheet.getItems().clear();
                }

                selectedKnob = index;

                knobs[selectedKnob].setBorder(SELECTED_BORDER);
                propertySheet.getItems().addAll(properties.get(selectedKnob));

            });

            timer.scheduleAtFixedRate(() -> {
                if ( updateCurrentValue[index] ) {

                    double step = ( knobs[index].getMaxValue() - knobs[index].getMinValue() ) / 234;
                    double cValue = knobs[index].getCurrentValue();
                    double tValue = knobs[index].getTargetValue();

                    if ( cValue < tValue ) {
                        Platform.runLater(() -> {
                            if ( ( tValue - cValue ) > step ) {
                                knobs[index].setCurrentValue(cValue + step);
                            } else {
                                knobs[index].setCurrentValue(tValue);
                                updateCurrentValue[index] = false;
                            }
                        });
                    } else if ( cValue > tValue ) {
                        Platform.runLater(() -> {
                            if ( ( cValue - tValue ) > step ) {
                                knobs[index].setCurrentValue(cValue - step);
                            } else {
                                knobs[index].setCurrentValue(tValue);
                                updateCurrentValue[index] = false;
                            }
                        });
                    }

                }},
                5000,
                200,
                TimeUnit.MILLISECONDS
            );

        }

        long afterTime = System.currentTimeMillis();

        LOGGER.log(Level.INFO, "Construction time: {0,number,#########0}ms", afterTime - beforeTime);

        knobContainer.add(knobs[0], 0, 0);
        knobContainer.add(knobs[1], 1, 0);
        knobContainer.add(knobs[2], 0, 1);
        knobContainer.add(knobs[3], 1, 1);

        propertySheet.setMode(PropertySheet.Mode.CATEGORY);

    }

    public void setWindowFocusStatus( String status ) {
        windowFocusStatus.setText(status);
    }

}
