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
import io.github.opendonationassistant.widget.model.properties.NumberProperty;
import io.github.opendonationassistant.widget.model.properties.PaddingProperty;
import io.github.opendonationassistant.widget.model.properties.RoundingProperty;
import io.github.opendonationassistant.widget.model.properties.SingleChoiceProperty;
import io.github.opendonationassistant.widget.model.properties.TextProperty;
import io.github.opendonationassistant.widget.model.properties.WidthProperty;
import io.github.opendonationassistant.widget.model.toplist.DonatersTopListCarouselProperty;
import io.github.opendonationassistant.widget.model.toplist.DonatersTopListLayoutProperty;

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
      // case "layout" -> new DonatersTopListLayoutProperty(name, value); //toplist but conflicts with paymentalerts
      case "carousel" -> new DonatersTopListCarouselProperty(name, value);

      case "headerFont" -> new FontProperty(name, value); //goal
      case "font" -> new FontProperty(name, value); //goal
      case "descriptionFont" -> new FontProperty(name, value); //goal
      case "amountFont" -> new FontProperty(name, value); //goal
      case "messageFont" -> new FontProperty(name, value); //toplist

      case "width" -> new WidthProperty(name, value); //goal
      case "headerWidth" -> new WidthProperty(name, value); //toplist
      case "listWidth" -> new WidthProperty(name, value); //toplist

      case "height" -> new HeightProperty(name, value); //goal
      case "headerHeight" -> new HeightProperty(name, value); //toplist
      case "listHeight" -> new HeightProperty(name, value); //toplist
      case "outerHeight" -> new HeightProperty(name, value); //goal
      case "filledHeight" -> new HeightProperty(name, value); //goal

      case "widgetBackgroundColor" -> new BackgroundColorProperty(name, value); //goal
      case "titleBackgroundColor" -> new BackgroundColorProperty(name, value); //goal
      case "listBackgroundColor" -> new BackgroundColorProperty(name, value); //toplist
      case "backgroundColor" -> new BackgroundColorProperty(name, value); //goal
      case "filledColor" -> new BackgroundColorProperty(name, value); //goal
      case "itemBackgroundColor" -> new BackgroundColorProperty(name, value); //toplist

      case "backgroundImage" -> new BackgroundImageProperty(name, value); //goal
      case "titleBackgroundImage" -> new BackgroundImageProperty(name, value); //goal
      case "headerBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist
      case "outerImage" -> new BackgroundImageProperty(name, value); //goal
      case "innerImage" -> new BackgroundImageProperty(name, value); //goal
      case "listBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist
      case "itemBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist

      case "border" -> new BorderProperty(name, value); //goal
      case "titleBorder" -> new BorderProperty(name, value); //goal
      case "itemBorder" -> new BorderProperty(name, value); //toplist
      case "headerBorder" -> new BorderProperty(name, value); //toplist
      case "outerBorder" -> new BorderProperty(name, value); //goal
      case "innerBorder" -> new BorderProperty(name, value); //goal
      case "widgetBorder" -> new BorderProperty(name, value); //toplist
      case "listBorder" -> new BorderProperty(name, value); //toplist

      case "padding" -> new PaddingProperty(name, value); //goal
      case "titlePadding" -> new PaddingProperty(name, value); //goal
      case "itemPadding" -> new PaddingProperty(name, value); //toplist
      case "headerPadding" -> new PaddingProperty(name, value); //toplist
      case "barPadding" -> new PaddingProperty(name, value); //goal
      case "innerPadding" -> new PaddingProperty(name, value); //goal
      case "listPadding" -> new PaddingProperty(name, value); //goal

      case "rounding" -> new RoundingProperty(name, value); //goal
      case "titleRounding" -> new RoundingProperty(name, value); //goal
      case "itemRounding" -> new RoundingProperty(name, value); //toplist
      case "headerRounding" -> new RoundingProperty(name, value); //toplist
      case "outerRounding" -> new RoundingProperty(name, value); //goal
      case "innerRounding" -> new RoundingProperty(name, value); //goal
      case "listRounding" -> new RoundingProperty(name, value); //goal

      case "boxShadow" -> new BoxShadowProperty(name, value);  //goal
      case "titleBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "headerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "outerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "innerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "listBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "itemBoxShadow" -> new BoxShadowProperty(name, value); // toplist

      case "showTitle" -> new BooleanProperty(name, value); //goal
      case "showLabel" -> new BooleanProperty(name, value); //goal
      case "showHeader" -> new BooleanProperty(name, value); //toplist
      case "hideEmpty" -> new BooleanProperty(name, value); //toplist

      case "titleAlignment" -> new AlignmentProperty(name, value); //goal
      case "filledTextAlign" -> new AlignmentProperty(name, value); //goal
      case "headerAlignment" -> new AlignmentProperty(name, value); //toplist
      case "listAlignment" -> new AlignmentProperty(name, value); //toplist

      case "filledTextPlacement" -> new SingleChoiceProperty(name, value); // goal
      case "type" -> new SingleChoiceProperty(name, value); // toplist
      case "period" -> new SingleChoiceProperty(name, value); // toplist

      case "topsize" -> new NumberProperty(name, value); // toplist
      case "gap" -> new NumberProperty(name, value); // toplist

      case "title" -> new TextProperty(name, value);  //toplist

      default -> new WidgetProperty<>(name, value);
    };
  }
}
