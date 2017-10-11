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


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import se.ess.knobs.KnobEvent;
import se.ess.knobs.controller.Controllable;


/**
 * @author Claudio Rosati, European Spallation Source ERIC
 * @version 1.0.0 23 Aug 2017
 */
@SuppressWarnings( "ClassWithoutLogger" )
public class ControlledKnobBuilder {

    public static ControlledKnobBuilder create() {
        return new ControlledKnobBuilder();
    }

    final Map<String, Object> properties = new HashMap<>(4);

    protected ControlledKnobBuilder() {
    }

    public final ControlledKnobBuilder backgroundColor( final Color color ) {

        properties.put("backgroundColor", color);

        return this;

    }

    @SuppressWarnings( "unchecked" )
    public ControlledKnob build() {

        final ControlledKnob knob = new ControlledKnob();

        //  Inter-dependent properties.
        if ( properties.containsKey("minValue") ) {
            knob.setMinValue((double) properties.get("minValue"));
        }
        if ( properties.containsKey("maxValue") ) {
            knob.setMaxValue((double) properties.get("maxValue"));
        }
        if ( properties.containsKey("currentValue") ) {
            knob.setCurrentValue((double) properties.get("currentValue"));
        }

        //  All other properties.
        if ( properties.containsKey("backgroundColor") ) {
            knob.setBackgroundColor((Color) properties.get("backgroundColor"));
        }
        if ( properties.containsKey("channel") ) {
            knob.setChannel((int) properties.get("channel"));
        }
        if ( properties.containsKey("coarseIncrement") ) {
            knob.setCoarseIncrement((double) properties.get("coarseIncrement"));
        }
        if ( properties.containsKey("color") ) {
            knob.setColor((Color) properties.get("color"));
        }
        if ( properties.containsKey("currentValueColor") ) {
            knob.setCurrentValueColor((Color) properties.get("currentValueColor"));
        }
        if ( properties.containsKey("decimals") ) {
            knob.setDecimals((int) properties.get("decimals"));
        }
        if ( properties.containsKey("dragDisabled") ) {
            knob.setDragDisabled((boolean) properties.get("dragDisabled"));
        }
        if ( properties.containsKey("extremaVisible") ) {
            knob.setExtremaVisible((boolean) properties.get("extremaVisible"));
        }
        if ( properties.containsKey("fineIncrement") ) {
            knob.setFineIncrement((double) properties.get("fineIncrement"));
        }
        if ( properties.containsKey("gradientStops") ) {
            knob.setGradientStops((List<Stop>) properties.get("gradientStops"));
        }
        if ( properties.containsKey("id") ) {
            knob.setId((String) properties.get("id"));
        }
        if ( properties.containsKey("indicatorColor") ) {
            knob.setIndicatorColor((Color) properties.get("indicatorColor"));
        }
        if ( properties.containsKey("layoutX") ) {
            knob.setLayoutX((double) properties.get("layoutX"));
        }
        if ( properties.containsKey("layoutY") ) {
            knob.setLayoutY((double) properties.get("layoutY"));
        }
        if ( properties.containsKey("maxHeight") ) {
            knob.setMaxHeight((double) properties.get("maxHeight"));
        }
        if ( properties.containsKey("maxSize") ) {

            Dimension2D maxSize = (Dimension2D) properties.get("maxSize");

            knob.setMaxSize(maxSize.getWidth(), maxSize.getHeight());

        }
        if ( properties.containsKey("maxWidth") ) {
            knob.setMaxWidth((double) properties.get("maxWidth"));
        }
        if ( properties.containsKey("minHeight") ) {
            knob.setMinHeight((double) properties.get("minHeight"));
        }
        if ( properties.containsKey("minSize") ) {

            Dimension2D minSize = (Dimension2D) properties.get("minSize");

            knob.setMinSize(minSize.getWidth(), minSize.getHeight());

        }
        if ( properties.containsKey("minWidth") ) {
            knob.setMinWidth((double) properties.get("minWidth"));
        }
        if ( properties.containsKey("onAdjusted") ) {
            knob.setOnAdjusted((EventHandler<KnobEvent>) properties.get("onAdjusted"));
        }
        if ( properties.containsKey("onAdjusting") ) {
            knob.setOnAdjusting((EventHandler<KnobEvent>) properties.get("onAdjusting"));
        }
        if ( properties.containsKey("onTargetSet") ) {
            knob.setOnTargetSet((EventHandler<KnobEvent>) properties.get("onTargetSet"));
        }
        if ( properties.containsKey("opacity") ) {
            knob.setOpacity((double) properties.get("opacity"));
        }
        if ( properties.containsKey("operatingMode") ) {
            knob.setOperatingMode((Controllable.OperatingMode) properties.get("operatingMode"));
        }
        if ( properties.containsKey("padding") ) {
            knob.setPadding((Insets) properties.get("padding"));
        }
        if ( properties.containsKey("prefHeight") ) {
            knob.setPrefHeight((double) properties.get("prefHeight"));
        }
        if ( properties.containsKey("prefSize") ) {

            Dimension2D size = (Dimension2D) properties.get("prefSize");

            knob.setPrefSize(size.getWidth(), size.getHeight());

        }
        if ( properties.containsKey("prefWidth") ) {
            knob.setPrefWidth((double) properties.get("prefWidth"));
        }
        if ( properties.containsKey("scaleX") ) {
            knob.setScaleX((double) properties.get("scaleX"));
        }
        if ( properties.containsKey("scaleY") ) {
            knob.setScaleY((double) properties.get("scaleY"));
        }
        if ( properties.containsKey("selected") ) {
            knob.setSelected((boolean) properties.get("selected"));
        }
        if ( properties.containsKey("selectionColor") ) {
            knob.setSelectionColor((Color) properties.get("selectionColor"));
        }
        if ( properties.containsKey("tagColor") ) {
            knob.setTagColor((Color) properties.get("tagColor"));
        }
        if ( properties.containsKey("targetValue") ) {
            knob.setTargetValue((double) properties.get("targetValue"));
        }
        if ( properties.containsKey("targetValueAlwaysVisible") ) {
            knob.setTargetValueAlwaysVisible((boolean) properties.get("targetValueAlwaysVisible"));
        }
        if ( properties.containsKey("textColor") ) {
            knob.setTextColor((Color) properties.get("textColor"));
        }
        if ( properties.containsKey("translateX") ) {
            knob.setTranslateX((double) properties.get("translateX"));
        }
        if ( properties.containsKey("translateY") ) {
            knob.setTranslateY((double) properties.get("translateY"));
        }
        if ( properties.containsKey("unit") ) {
            knob.setUnit((String) properties.get("unit"));
        }
        if ( properties.containsKey("zeroDetentEnabled") ) {
            knob.setZeroDetentEnabled((boolean) properties.get("zeroDetentEnabled"));
        }

        //  The following must be the last one(s).
        if ( properties.containsKey("controller") ) {
            knob.setController((String) properties.get("controller"));
        }

        return knob;

    }

    public final ControlledKnobBuilder channel( final int channel ) {

        properties.put("channel", channel);

        return this;

    }

    public final ControlledKnobBuilder coarseIncrement( final double value ) {

        properties.put("coarseIncrement", value);

        return this;

    }

    public final ControlledKnobBuilder color( final Color color ) {

        properties.put("color", color);

        return this;

    }

    public final ControlledKnobBuilder controller( final String controller ) {

        properties.put("controller", controller);

        return this;

    }

    public final ControlledKnobBuilder currentValue( final double value ) {

        properties.put("currentValue", value);

        return this;

    }

    public final ControlledKnobBuilder currentValueColor( final Color color ) {

        properties.put("currentValueColor", color);

        return this;

    }

    public final ControlledKnobBuilder decimals( final int decimals ) {

        properties.put("decimals", decimals);

        return this;

    }

    public final ControlledKnobBuilder dragDisabled( final boolean value ) {

        properties.put("dragDisabled", value);

        return this;

    }

    public final ControlledKnobBuilder extremaVisible( final boolean value ) {

        properties.put("extremaVisible", value);

        return this;

    }

    public final ControlledKnobBuilder fineIncrement( final double value ) {

        properties.put("fineIncrement", value);

        return this;

    }

    public final ControlledKnobBuilder gradientStops( final Stop... stops ) {
        return gradientStops(Arrays.asList(stops));
    }

    public final ControlledKnobBuilder gradientStops( final List<Stop> stops ) {

        properties.put("gradientStops", stops);

        return this;

    }

    public final ControlledKnobBuilder id( final String unit ) {

        properties.put("id", unit);

        return this;

    }

    public final ControlledKnobBuilder indicatorColor( final Color color ) {

        properties.put("indicatorColor", color);

        return this;

    }

    public final ControlledKnobBuilder layoutX( final double scale ) {

        properties.put("layoutX", scale);

        return this;

    }

    public final ControlledKnobBuilder layoutY( final double scale ) {

        properties.put("layoutY", scale);

        return this;

    }

    public final ControlledKnobBuilder maxHeight( final double height ) {

        properties.put("maxHeight", height);

        return this;

    }

    public final ControlledKnobBuilder maxSize( final double width, final double height ) {

        properties.put("maxSize", new Dimension2D(width, height));

        return this;

    }

    public final ControlledKnobBuilder maxValue( final double value ) {

        properties.put("maxValue", value);

        return this;

    }

    public final ControlledKnobBuilder maxWidth( final double width ) {

        properties.put("maxWidth", width);

        return this;

    }

    public final ControlledKnobBuilder minHeight( final double height ) {

        properties.put("minHeight", height);

        return this;

    }

    public final ControlledKnobBuilder minSize( final double width, final double height ) {

        properties.put("minSize", new Dimension2D(width, height));

        return this;

    }

    public final ControlledKnobBuilder minValue( final double value ) {

        properties.put("minValue", value);

        return this;

    }

    public final ControlledKnobBuilder minWidth( final double width ) {

        properties.put("minWidth", width);

        return this;

    }

    public final ControlledKnobBuilder onAdjusted( final EventHandler<KnobEvent> handler ) {

        properties.put("onAdjusted", handler);

        return this;

    }

    public final ControlledKnobBuilder onAdjusting( final EventHandler<KnobEvent> handler ) {

        properties.put("onAdjusting", handler);

        return this;

    }

    public final ControlledKnobBuilder onTargetSet( final EventHandler<KnobEvent> handler ) {

        properties.put("onTargetSet", handler);

        return this;

    }

    public final ControlledKnobBuilder opacity( final double opacity ) {

        properties.put("opacity", opacity);

        return this;

    }

    public final ControlledKnobBuilder operatingMode( final Controllable.OperatingMode operatingMode ) {

        properties.put("operatingMode", operatingMode);

        return this;

    }

    public final ControlledKnobBuilder padding( final double topRightBottomLeft ) {
        return padding(new Insets(topRightBottomLeft));
    }

    public final ControlledKnobBuilder padding( final double top, final double right, final double bottom, final double left ) {
        return padding(new Insets(top, right, bottom, left));
    }

    public final ControlledKnobBuilder padding( final Insets insets ) {

        properties.put("padding", insets);

        return this;

    }

    public final ControlledKnobBuilder prefHeight( final double height ) {

        properties.put("prefHeight", height);

        return this;

    }

    public final ControlledKnobBuilder prefSize( final double width, final double height ) {

        properties.put("prefSize", new Dimension2D(width, height));

        return this;

    }

    public final ControlledKnobBuilder prefWidth( final double width ) {

        properties.put("prefWidth", width);

        return this;

    }

    public final ControlledKnobBuilder scaleX( final double scale ) {

        properties.put("scaleX", scale);

        return this;

    }

    public final ControlledKnobBuilder scaleY( final double scale ) {

        properties.put("scaleY", scale);

        return this;

    }

    public final ControlledKnobBuilder selected( final boolean value ) {

        properties.put("selected", value);

        return this;

    }

    public final ControlledKnobBuilder selectionColor( final Color color ) {

        properties.put("selectionColor", color);

        return this;

    }

    public final ControlledKnobBuilder tagColor( final Color color ) {

        properties.put("tagColor", color);

        return this;

    }

    public final ControlledKnobBuilder targetValue( final double value ) {

        properties.put("targetValue", value);

        return this;

    }

    public final ControlledKnobBuilder targetValueAlwaysVisible( final boolean value ) {

        properties.put("targetValueAlwaysVisible", value);

        return this;

    }

    public final ControlledKnobBuilder textColor( final Color color ) {

        properties.put("textColor", color);

        return this;

    }

    public final ControlledKnobBuilder translateX( final double scale ) {

        properties.put("translateX", scale);

        return this;

    }

    public final ControlledKnobBuilder translateY( final double scale ) {

        properties.put("translateY", scale);

        return this;

    }

    public final ControlledKnobBuilder unit( final String unit ) {

        properties.put("unit", unit);

        return this;

    }

    public final ControlledKnobBuilder zeroDetentEnabled( final boolean value ) {

        properties.put("zeroDetentEnabled", value);

        return this;

    }

}
