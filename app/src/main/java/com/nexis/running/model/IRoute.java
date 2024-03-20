package com.nexis.running.model;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Diese Schnittstelle repräsentiert die Struktur einer Route in der Laufanwendung.
 * Sie definiert Methoden zum Abrufen verschiedener routenbezogener Informationen.
 */
public interface IRoute {

    /**
     * Gibt die Gesamtdistanz der Route zurück.
     * @return Die Gesamtdistanz der Route.
     */
    double getTotalDistance();

    /**
     * Gibt die durchschnittliche Geschwindigkeit während der Aktivität zurück.
     * @return Die durchschnittliche Geschwindigkeit während der Aktivität.
     */
    double getAverageSpeed();

    /**
     * Gibt die verstrichene Zeit der Aktivität zurück.
     * @return Die verstrichene Zeit der Aktivität.
     */
    String getElapsedTime();

    /**
     * Gibt das Datum und die Uhrzeit der Aufzeichnung zurück.
     * @return Das Datum und die Uhrzeit der Aufzeichnung.
     */
    String getDateTime();

    /**
     * Gibt die verbrauchten Kalorien basierend auf der Aktivität zurück.
     * @return Die verbrauchten Kalorien basierend auf der Aktivität.
     */
    double getCaloriesBurned();

    /**
     * Gibt die Liste der GeoPoints zurück, die die Route repräsentieren.
     * @return Die Liste der GeoPoints, die die Route repräsentieren.
     */
    ArrayList<GeoPoint> getGeoPoints();
}
