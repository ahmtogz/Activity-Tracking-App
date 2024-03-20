package com.nexis.running.model;

public interface IUser {

    /**
     * Gibt den Namen des Benutzers zurück.
     * @return Der Name des Benutzers.
     */
    String getName();

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     * @return Die E-Mail-Adresse des Benutzers.
     */
    String getEmail();

    /**
     * Gibt das Passwort des Benutzers zurück.
     * @return Das Passwort des Benutzers.
     */
    String getPassword();

    /**
     * Gibt das Geschlecht des Benutzers zurück.
     * @return Das Geschlecht des Benutzers.
     */
    String getGender();

    /**
     * Gibt das Gewicht des Benutzers zurück.
     * @return Das Gewicht des Benutzers.
     */
    int getWeight();

    /**
     * Gibt das Alter des Benutzers zurück.
     * @return Das Alter des Benutzers.
     */
    int getAge();
}

