package io.github.opendonationassistant.widget.model;

import io.github.opendonationassistant.widget.model.donationgoal.DonationGoalLabelProperty;
import io.github.opendonationassistant.widget.model.donationgoal.DonationGoalProperty;
import io.github.opendonationassistant.widget.model.paymentalert.PaymentAlertProperty;
import io.github.opendonationassistant.widget.model.properties.AlignmentProperty;
import io.github.opendonationassistant.widget.model.properties.BackgroundColorProperty;
import io.github.opendonationassistant.widget.model.properties.BackgroundImageProperty;
import io.github.opendonationassistant.widget.model.properties.BooleanProperty;
import io.github.opendonationassistant.widget.model.properties.BorderProperty;
import io.github.opendonationassistant.widget.model.properties.BoxShadowProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import io.github.opendonationassistant.widget.model.properties.HeightProperty;
import io.github.opendonationassistant.widget.model.properties.PaddingProperty;
import io.github.opendonationassistant.widget.model.properties.RoundingProperty;
import io.github.opendonationassistant.widget.model.properties.SingleChoiceProperty;
import io.github.opendonationassistant.widget.model.properties.WidthProperty;

public class WidgetProperty<T> {

  private String name;
  private T value;

  public WidgetProperty(String name, T value) {
    this.name = name;
    this.value = value;
  }

  public String name() {
    return name;
  }

  public T value() {
    return value;
  }

  public static WidgetProperty<?> of(String name, Object value) {
    return switch (name) {
      case "alerts" -> new PaymentAlertProperty(name, value);
      case "goal" -> new DonationGoalProperty(name, value);
      case "labelTemplate" -> new DonationGoalLabelProperty(name, value);

      case "headerFont" -> new FontProperty(name, value);
      case "font" -> new FontProperty(name, value);
      case "descriptionFont" -> new FontProperty(name, value);
      case "amountFont" -> new FontProperty(name, value);

      case "width" -> new WidthProperty(name, value);
      case "height" -> new HeightProperty(name, value);
      case "outerHeight" -> new HeightProperty(name, value);
      case "filledHeight" -> new HeightProperty(name, value);

      case "widgetBackgroundColor" -> new BackgroundColorProperty(name, value);
      case "titleBackgroundColor" -> new BackgroundColorProperty(name, value);
      case "backgroundColor" -> new BackgroundColorProperty(name, value);
      case "filledColor" -> new BackgroundColorProperty(name, value);

      case "backgroundImage" -> new BackgroundImageProperty(name, value);
      case "titleBackgroundImage" -> new BackgroundImageProperty(name, value);
      case "outerImage" -> new BackgroundImageProperty(name, value);
      case "innerImage" -> new BackgroundImageProperty(name, value);

      case "border" -> new BorderProperty(name, value);
      case "titleBorder" -> new BorderProperty(name, value);
      case "outerBorder" -> new BorderProperty(name, value);
      case "innerBorder" -> new BorderProperty(name, value);

      case "padding" -> new PaddingProperty(name, value);
      case "titlePadding" -> new PaddingProperty(name, value);
      case "barPadding" -> new PaddingProperty(name, value);
      case "innerPadding" -> new PaddingProperty(name, value);

      case "rounding" -> new RoundingProperty(name, value);
      case "titleRounding" -> new RoundingProperty(name, value);
      case "outerRounding" -> new RoundingProperty(name, value);
      case "innerRounding" -> new RoundingProperty(name, value);

      case "boxShadow" -> new BoxShadowProperty(name, value);
      case "titleBoxShadow" -> new BoxShadowProperty(name, value);
      case "outerBoxShadow" -> new BoxShadowProperty(name, value);
      case "innerBoxShadow" -> new BoxShadowProperty(name, value);

      case "showTitle" -> new BooleanProperty(name, value);
      case "showLabel" -> new BooleanProperty(name, value);

      case "titleAlignment" -> new AlignmentProperty(name, value);
      case "filledTextAlign" -> new AlignmentProperty(name, value);

      case "filledTextPlacement" -> new SingleChoiceProperty(name, value);

      default -> new WidgetProperty<>(name, value);
    };
  }
}
