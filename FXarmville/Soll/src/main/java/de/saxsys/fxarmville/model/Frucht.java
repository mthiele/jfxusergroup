package de.saxsys.fxarmville.model;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;
import de.saxsys.fxarmville.model.util.FruchtBildLader;

public class Frucht {

	// wie lange ist eine Frucht reif
	private static final double REIFEDAUER = 0.05;

	// Zeit für Reifung / Faulen
	private final DoubleProperty lebenszeit = new SimpleDoubleProperty();

	// Aktueller Reifegrad [0..1]
	private final DoubleProperty aktuelleLebenszeit = new SimpleDoubleProperty();

	/*
	 * Lebensabschnitte
	 */
	private final BooleanProperty istReif = new SimpleBooleanProperty();
	private final BooleanProperty istFaulig = new SimpleBooleanProperty();
	private final BooleanProperty istEingegangen = new SimpleBooleanProperty();
	private final BooleanProperty istGeerntetWorden = new SimpleBooleanProperty();

	private final LebensZyklus lebensZyklus = new LebensZyklus();
	private final String bildName;

	public Frucht(final String bildName, final double lebenszeit) {
		this.bildName = bildName;
		this.lebenszeit.set(lebenszeit);
	}

	public void baueAn() {
		final double haelfteDerLebenszeit = 0.5;
		istReif.bind(aktuelleLebenszeit.greaterThan(
				haelfteDerLebenszeit - REIFEDAUER).and(
				aktuelleLebenszeit.lessThan(haelfteDerLebenszeit + REIFEDAUER)));

		istEingegangen.bind(aktuelleLebenszeit.greaterThanOrEqualTo(1.0).and(
				istGeerntetWorden.not()));

		istFaulig.bind(aktuelleLebenszeit.greaterThan(haelfteDerLebenszeit)
				.and(istReif.not()));

		lebensZyklus.wachsen();
	}

	/**
	 * Private Klasse welche den Lebenszyklus einer Frucht abbildet.
	 * 
	 * @author sialcasa
	 * 
	 */
	private class LebensZyklus {

		private Timeline lebensZyklus;

		public void wachsen() {
			// **** BEGIN LIVE CODING ****

			lebensZyklus = TimelineBuilder
					.create()
					.delay(Duration.seconds(new Random().nextInt(10)))
					.keyFrames(
							new KeyFrame(Duration.seconds(getLebenszeit()),
									new KeyValue(aktuelleLebenszeit, 1.0)))
					.build();

			lebensZyklus.play();

			// **** END LIVE CODING ****
		}

		public void ernten() {
			lebensZyklus.stop();
		}
	}

	public void ernten() {
		lebensZyklus.ernten();
		istGeerntetWorden.set(true);
	}

	public Image getBild() {
		return FruchtBildLader.getInstance().getBild(bildName);
	}

	/*
	 * WACHSDAUER
	 */
	public ReadOnlyDoubleProperty lebenszeitProperty() {
		return lebenszeit;
	}

	public double getLebenszeit() {
		return lebenszeit.get();
	}

	/*
	 * REIFEGRAD
	 */
	public double getAktuelleLebenszeit() {
		return aktuelleLebenszeit.get();
	}

	public DoubleProperty aktuelleLebenszeitProperty() {
		return aktuelleLebenszeit;
	}

	/*
	 * LEBENSABSCHNITTE
	 */

	public ReadOnlyBooleanProperty istReifProperty() {
		return istReif;
	}

	public ReadOnlyBooleanProperty istFauligProperty() {
		return istFaulig;
	}

	public ReadOnlyBooleanProperty istEingegangenProperty() {
		return istEingegangen;
	}

	public ReadOnlyBooleanProperty istGeerntetWordenProperty() {
		return istGeerntetWorden;
	}
}
