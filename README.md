# Controlled-KNOBS

[![Maven Central](https://img.shields.io/maven-central/v/se.europeanspallationsource/javafx.control.controlled-knobs.svg)](https://repo1.maven.org/maven2/se/europeanspallationsource/javafx.control.controlled-knobs)
[![javadoc](https://www.javadoc.io/badge/se.europeanspallationsource/javafx.control.controlled-knobs.svg)](https://www.javadoc.io/doc/se.europeanspallationsource/javafx.control.controlled-knobs)
[![Apache License](https://img.shields.io/badge/license-Apache%20License%202.0-yellow.svg)](http://www.apache.org/licenses/LICENSE-2.0)

A MIDI controlled version of [KNOBS](https://github.com/ESSICS/KNOBS).

![ControlledKnobEvaluator1](https://github.com/ESSICS/Controlled-KNOBS/blob/master/doc/architecture.png)
![ControlledKnobEvaluator1](https://github.com/ESSICS/Controlled-KNOBS/blob/master/doc/ControlledKnobEvaluator1.png)
![ControlledKnobEvaluator2](https://github.com/ESSICS/Controlled-KNOBS/blob/master/doc/ControlledKnobEvaluator2.jpg)

Controlled-KNOBS can be controlled by a [Midi Fighter Twister](https://store.djtechtools.com/products/midi-fighter-twister).

![Midi Fighter Twister](https://d16rm6ap8dyyo6.cloudfront.net/product_images/images/000/001/491/medium/Black_34_zoomed.jpg?1398722121)

Before the Midi Fighter Twister can be used with this library, the `Controlled-KNOBS.mfs` file must be imported into the device by means of the Midi Fighter Utility application ([MacOS X](https://s3.amazonaws.com/djtt-utility/mf_utility_installers/Midi_Fighter_Utility_OSX.dmg), [Windows](https://s3.amazonaws.com/djtt-utility/mf_utility_installers/Midi+Fighter+Utility+Win.exe)).

## Maven

To add a dependency on THUMBWHEEL using Maven, use the following:

```xml
<dependency>
    <groupId>se.europeanspallationsource</groupId>
    <artifactId>javafx.control.controlled-knobs</artifactId>
    <version>1.0.6</version>
    <scope>compile</scope>
</dependency>
```
