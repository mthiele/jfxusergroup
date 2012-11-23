package de.saxsys.fxarmville.model.fruits;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * 1.
 * 
 * @author Michael
 * 
 */
public abstract class Anbaubar {

	protected IntegerProperty wachsdauerProperty = new SimpleIntegerProperty();
	protected DoubleProperty reifegradProperty = new SimpleDoubleProperty();
	protected BooleanProperty istReifProperty = new SimpleBooleanProperty();
	protected BooleanProperty istFauligProperty = new SimpleBooleanProperty();

	private LebensZyklus lebensZyklus = new LebensZyklus();

	public abstract Image getBild();

	public Anbaubar() {

	}

	public void baueAn() {
		// Wenn reifegrad ist groesser als wachsdauer ist frucht reif
		istReifProperty.bind(Bindings.greaterThanOrEqual(reifegradProperty,
				wachsdauerProperty).and(Bindings.not(istFauligProperty)));
		lebensZyklus.wachse();
	}

	public void ernten() {
		lebensZyklus.ernten();
	}

	/*
	 * WACHSDAUER
	 */
	public ReadOnlyIntegerProperty wachsdauerProperty() {
		return wachsdauerProperty;
	}

	public int getWachsdauer() {
		return wachsdauerProperty.get();
	}

	/*
	 * REIFEGRAD
	 */
	public double getReifegrad() {
		return reifegradProperty.get();
	}

	public ReadOnlyDoubleProperty reifegradProperty() {
		return reifegradProperty;
	}

	/*
	 * AKTUELLER ZUSTAND
	 */

	public ReadOnlyBooleanProperty istReifProperty() {
		return istReifProperty;
	}

	public ReadOnlyBooleanProperty istFauligProperty() {
		return istFauligProperty;
	}

	/**
	 * Private Klasse welche den Lebenszyklus einer Frucht abbildet.
	 * 
	 * @author sialcasa
	 * 
	 */
	private class LebensZyklus {

		private SequentialTransition lebensZyklus;

		public void wachse() {

			// Wachstum
			Timeline reifung = TimelineBuilder
					.create()
					.keyFrames(
							new KeyFrame(Duration.seconds(getWachsdauer()),
									new KeyValue(reifegradProperty,
											getWachsdauer()))).build();

			// Zeit wo die Frucht reif ist
			PauseTransition istReif = new PauseTransition();
			double reifeZeit = new Random().nextDouble() * 4 + 1.0;
			istReif.setDuration(Duration.seconds(reifeZeit));

			// Leben starten
			lebensZyklus = SequentialTransitionBuilder.create()
					.children(reifung, istReif).build();

			lebensZyklus.setOnFinished(fruchtGehtEinEvent());
			lebensZyklus.play();
		}

		public EventHandler<ActionEvent> fruchtGehtEinEvent() {
			return new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					istFauligProperty.set(true);
				}
			};
		}

		public void ernten() {
			lebensZyklus.stop();
		}
	}
}
