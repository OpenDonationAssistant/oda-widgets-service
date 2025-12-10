package io.github.opendonationassistant.widget.model;

import io.github.opendonationassistant.widget.model.donationgoal.DonationGoalLabelProperty;
import io.github.opendonationassistant.widget.model.donationgoal.DonationGoalProperty;
import io.github.opendonationassistant.widget.model.donaton.DonatonPriceProperty;
import io.github.opendonationassistant.widget.model.paymentalert.PaymentAlertProperty;
import io.github.opendonationassistant.widget.model.properties.AlignmentProperty;
import io.github.opendonationassistant.widget.model.properties.BackgroundColorProperty;
import io.github.opendonationassistant.widget.model.properties.BackgroundImageProperty;
import io.github.opendonationassistant.widget.model.properties.BooleanProperty;
import io.github.opendonationassistant.widget.model.properties.BorderProperty;
import io.github.opendonationassistant.widget.model.properties.BoxShadowProperty;
import io.github.opendonationassistant.widget.model.properties.DateTimeProperty;
import io.github.opendonationassistant.widget.model.properties.FontProperty;
import io.github.opendonationassistant.widget.model.properties.HeightProperty;
import io.github.opendonationassistant.widget.model.properties.NumberProperty;
import io.github.opendonationassistant.widget.model.properties.PaddingProperty;
import io.github.opendonationassistant.widget.model.properties.RoundingProperty;
import io.github.opendonationassistant.widget.model.properties.SingleChoiceProperty;
import io.github.opendonationassistant.widget.model.properties.TextProperty;
import io.github.opendonationassistant.widget.model.properties.WidthProperty;
import io.github.opendonationassistant.widget.model.reel.ReelItemBackgroundProperty;
import io.github.opendonationassistant.widget.model.reel.ReelItemListProperty;
import io.github.opendonationassistant.widget.model.reel.ReelWinningEffectProperty;
import io.github.opendonationassistant.widget.model.roulette.RouletteItemsProperty;
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
      case "price" -> new DonatonPriceProperty(name, value);
      case "reelWinningEffect" -> new ReelWinningEffectProperty(name, value);
      case "optionList" -> new ReelItemListProperty(name, value);
      case "ReelItemBackgroundProperty" -> new ReelItemBackgroundProperty(name, value);
      case "roulette-items" -> new RouletteItemsProperty(name, value);

      case "headerFont" -> new FontProperty(name, value); //goal
      case "font" -> new FontProperty(name, value); //goal
      case "titleFont" -> new FontProperty(name, value); //goal
      case "descriptionFont" -> new FontProperty(name, value); //goal
      case "amountFont" -> new FontProperty(name, value); //goal
      case "messageFont" -> new FontProperty(name, value); //toplist
      case "eventsNicknameFont" -> new FontProperty(name, value); //horizontalevents
      case "eventsMessageFont" -> new FontProperty(name, value); //horizontalevents
      case "eventsAmountFont" -> new FontProperty(name, value); //horizontalevents
      case "requesterFont" -> new FontProperty(name, value); //player-info
      case "queueFont" -> new FontProperty(name, value); //player-info

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
      case "titleBackground" -> new BackgroundColorProperty(name, value); //player-info
      case "listBackgroundColor" -> new BackgroundColorProperty(name, value); //toplist
      case "backgroundColor" -> new BackgroundColorProperty(name, value); //goal
      case "filledColor" -> new BackgroundColorProperty(name, value); //goal
      case "itemBackgroundColor" -> new BackgroundColorProperty(name, value); //toplist
      case "headerBackgroundColor" -> new BackgroundColorProperty(name, value); //toplist
      case "eventsBackgroundColor" -> new BackgroundColorProperty(name, value); //horizontalevents
      case "lineBackgroundColor" -> new BackgroundColorProperty(name, value); //horizontalevents
      case "selectionColor" -> new BackgroundColorProperty(name, value); //horizontalevents
      case "background" -> new BackgroundColorProperty(name, value); //horizontalevents
      case "requesterBackground" -> new BackgroundColorProperty(name, value); //player-info
      case "queueBackground" -> new BackgroundColorProperty(name, value); //player-info

      case "backgroundImage" -> new BackgroundImageProperty(name, value); //goal
      case "titleBackgroundImage" -> new BackgroundImageProperty(name, value); //goal
      case "headerBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist
      case "outerImage" -> new BackgroundImageProperty(name, value); //goal
      case "innerImage" -> new BackgroundImageProperty(name, value); //goal
      case "listBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist
      case "itemBackgroundImage" -> new BackgroundImageProperty(name, value); //toplist
      case "eventsBackgroundImage" -> new BackgroundImageProperty(name, value); //horizontalevents
      case "lineBackgroundImage" -> new BackgroundImageProperty(name, value); //horizontalevents

      case "border" -> new BorderProperty(name, value); //goal
      case "titleBorder" -> new BorderProperty(name, value); //goal
      case "itemBorder" -> new BorderProperty(name, value); //toplist
      case "headerBorder" -> new BorderProperty(name, value); //toplist
      case "outerBorder" -> new BorderProperty(name, value); //goal
      case "innerBorder" -> new BorderProperty(name, value); //goal
      case "widgetBorder" -> new BorderProperty(name, value); //toplist
      case "listBorder" -> new BorderProperty(name, value); //toplist
      case "eventsBorder" -> new BorderProperty(name, value); //toplist
      case "lineBorder" -> new BorderProperty(name, value); //horizontalevents
      case "cardBorder" -> new BorderProperty(name, value); //reel
      case "requesterBorder" -> new BorderProperty(name, value); //player-info
      case "queueBorder" -> new BorderProperty(name, value); //player-info

      case "padding" -> new PaddingProperty(name, value); //goal
      case "titlePadding" -> new PaddingProperty(name, value); //goal
      case "itemPadding" -> new PaddingProperty(name, value); //toplist
      case "headerPadding" -> new PaddingProperty(name, value); //toplist
      case "barPadding" -> new PaddingProperty(name, value); //goal
      case "innerPadding" -> new PaddingProperty(name, value); //goal
      case "listPadding" -> new PaddingProperty(name, value); //goal
      case "eventsPadding" -> new PaddingProperty(name, value); //horizontalevents
      case "linePadding" -> new PaddingProperty(name, value); //horizontalevents
      case "requesterPadding" -> new PaddingProperty(name, value); //player-info
      case "queuePadding" -> new PaddingProperty(name, value); //player-info

      case "rounding" -> new RoundingProperty(name, value); //goal
      case "titleRounding" -> new RoundingProperty(name, value); //goal
      case "itemRounding" -> new RoundingProperty(name, value); //toplist
      case "headerRounding" -> new RoundingProperty(name, value); //toplist
      case "outerRounding" -> new RoundingProperty(name, value); //goal
      case "innerRounding" -> new RoundingProperty(name, value); //goal
      case "listRounding" -> new RoundingProperty(name, value); //goal
      case "eventsRounding" -> new RoundingProperty(name, value); //horizontalevents
      case "lineRounding" -> new RoundingProperty(name, value); //horizontalevents
      case "requesterRounding" -> new RoundingProperty(name, value); //player-info
      case "queueRounding" -> new RoundingProperty(name, value); //player-info

      case "boxShadow" -> new BoxShadowProperty(name, value);  //goal
      case "titleBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "headerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "outerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "innerBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "listBoxShadow" -> new BoxShadowProperty(name, value); // goal
      case "itemBoxShadow" -> new BoxShadowProperty(name, value); // toplist
      case "headerShadow" -> new BoxShadowProperty(name, value); // horizontalevents
      case "eventsShadow" -> new BoxShadowProperty(name, value); // horizontalevents
      case "lineShadow" -> new BoxShadowProperty(name, value); // horizontalevents

      case "showTitle" -> new BooleanProperty(name, value); //goal
      case "showLabel" -> new BooleanProperty(name, value); //goal
      case "showHeader" -> new BooleanProperty(name, value); //toplist
      case "showNickname" -> new BooleanProperty(name, value); //horizontalevents
      case "showMessage" -> new BooleanProperty(name, value); //horizontalevents
      case "showAmount" -> new BooleanProperty(name, value); //horizontalevents
      case "hideEmpty" -> new BooleanProperty(name, value); //toplist
      case "resetOnLoad" -> new BooleanProperty(name, value); //donationtimer
      case "showRequester" -> new BooleanProperty(name, value); //player-info
      case "showQueueSize" -> new BooleanProperty(name, value); //player-info
      case "audioOnly" -> new BooleanProperty(name, value); //player-info

      case "titleAlignment" -> new AlignmentProperty(name, value); //goal
      case "titleTextAlign" -> new AlignmentProperty(name, value); //goal
      case "filledTextAlign" -> new AlignmentProperty(name, value); //goal
      case "headerAlignment" -> new AlignmentProperty(name, value); //toplist
      case "listAlignment" -> new AlignmentProperty(name, value); //toplist

      case "filledTextPlacement" -> new SingleChoiceProperty(name, value); // goal
      case "type" -> new SingleChoiceProperty(name, value); // toplist
      case "period" -> new SingleChoiceProperty(name, value); // toplist
      case "widgetType" -> new SingleChoiceProperty(name, value); // player-info

      case "topsize" -> new NumberProperty(name, value); // toplist
      case "gap" -> new NumberProperty(name, value); // toplist
      case "eventGap" -> new NumberProperty(name, value); // horizontalevents
      case "speed" -> new NumberProperty(name, value); // horizontal events
      case "requiredAmount" -> new NumberProperty(name, value); // reel
      case "perView" -> new NumberProperty(name, value); // reel
      case "time" -> new NumberProperty(name, value); // reel

      case "title" -> new TextProperty(name, value);  //toplist
      case "text" -> new TextProperty(name, value);  //donationtimer
      case "headerText" -> new TextProperty(name, value);  //horizontal events
                                                     //
      case "timer-end" -> new DateTimeProperty(name, value); //donaton

      default -> new WidgetProperty<>(name, value);
    };
  }
}
