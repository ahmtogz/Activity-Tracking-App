package com.nexis.running;

import static org.junit.Assert.*;
import android.content.Context;
import android.util.Log;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.nexis.running.model.Route;
import com.nexis.running.model.RouteDbHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class RouteDbHelperTest {

    private RouteDbHelper routeDbHelper;
    Context context;


    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        routeDbHelper = new RouteDbHelper(context,"Test");
    }



    @After
    public void tearDown() {
        try {
            // Aufräumen nach jedem Test (abhängig von der konkreten Implementierung)
            routeDbHelper.removeAll();
        } catch (Exception e) {
            // Protokollieren der Ausnahme mit Logcat
            Log.e("RouteDBHelperTest", "Error during cleanup: " + e.getMessage());
        }
    }


    /**
     * Überprüft, ob alle Routen aus der Datenbank auf einmal geladen werden können.
     */
    @Test
    public void testLoadAllRoutes() {
        routeDbHelper.save(createSampleRoute());

        ArrayList<Route> loadedRoutes = routeDbHelper.loadAll();

        // Überprüfung
        assertFalse(loadedRoutes.isEmpty());
        assertEquals(1, loadedRoutes.size());

    }
    /**
     * Überprüft, ob mehrere Routen erfolgreich gespeichert und geladen werden können.
     */
    @Test
    public void testSaveAndLoadMultipleRoutes() {
        // Annahme: Es gibt mehrere Routen zum Speichern
        ArrayList<Route> routesToSave = createSampleRoutesList();

        // Durchführung: Speichern
        for (Route route : routesToSave) {
            routeDbHelper.save(route);
        }

        // Durchführung: Laden
        ArrayList<Route> loadedRoutes = routeDbHelper.loadAll();

        // Überprüfung
        assertEquals(routesToSave.size(), loadedRoutes.size());

    }
    /**
     * Überprüft, ob eine Route erfolgreich gespeichert und anhand des Schlüssels geladen werden kann.
     */
    @Test
    public void testSaveAndLoadSingleRouteByKey() {
        // Annahme: Es gibt eine Route zum Speichern
        Route routeToSave = createSampleRoute();
        String routeKey = routeToSave.getDateTime();


        // Durchführung: Speichern
        routeDbHelper.save(routeToSave);

        // Durchführung: Laden anhand des Schlüssels
        Route loadedRoute = routeDbHelper.load(routeKey);

        // Überprüfung
        assertNotNull(loadedRoute);
        assertEquals(routeToSave.getDateTime(), loadedRoute.getDateTime());

    }

    /**
     * Überprüft, ob eine Route erfolgreich anhand des Schlüssels gelöscht werden kann.
     */
    @Test
    public void testRemoveRouteByKey() {
        // Annahme: Es gibt eine Route zum Löschen
        Route routeToRemove = createSampleRoute();
        ArrayList<Route> routesInDatabase = new ArrayList<>();
        routesInDatabase.add(routeToRemove);


        // Durchführung: Löschen
        routeDbHelper.remove(routeToRemove.getDateTime());

        // Überprüfung
        assertNull(routeDbHelper.load(routeToRemove.getDateTime()));

    }
    /**
     * Überprüft, ob alle Routen erfolgreich aus der Datenbank gelöscht werden können.
     */
    @Test
    public void testRemoveAllRoutes() {
        // Annahme: Es gibt Routen zum Löschen
        ArrayList<Route> routesToRemove = createSampleRoutesList();

        // Durchführung: Löschen aller Routen
        routeDbHelper.removeAll();

        // Überprüfung
        assertTrue(routeDbHelper.loadAll().isEmpty());

    }

    /**
     * Überprüft, ob alle Routen geladen werden können, wenn die Datenbank leer ist.
     */
    @Test
    public void testLoadAllRoutesWhenEmpty() {

        // Durchführung: Laden aller Routen
        ArrayList<Route> loadedRoutes = routeDbHelper.loadAll();

        // Überprüfung
        assertTrue(loadedRoutes.isEmpty());

    }
    /**
     * Überprüft, ob die korrekte Route gelöscht wird, wenn sich mehrere Routen in der Datenbank befinden.
     */
    @Test
    public void testRemoveCorrectRouteWithMultipleRoutes() {
        // Annahme: Es gibt mehrere Routen in der Datenbank
        ArrayList<Route> routesInDatabase = createSampleRoutesList();
        String routeKeyToRemove = routesInDatabase.get(1).getDateTime();

        // Durchführung: Löschen der Route
        routeDbHelper.remove(routeKeyToRemove);

        // Überprüfung
        assertNull(routeDbHelper.load(routeKeyToRemove));

    }
    /**
     * Überprüft, ob man einen nicht existenten Schlüssel erfolgreich laden kann.
     */
    @Test
    public void testLoadNonExistentKey() {
        // Annahme: Es gibt keinen Eintrag mit diesem Schlüssel in der Datenbank
        String nonExistentKey = "non_existent_key";

        // Durchführung: Laden anhand des nicht existenten Schlüssels
        Route loadedRoute = routeDbHelper.load(nonExistentKey);

        // Überprüfung
        assertNull(loadedRoute);

    }
    private ArrayList<Route> createSampleRoutesList() {
        ArrayList<Route> sampleRoutes = new ArrayList<>();

        // Erstellen von drei Beispielrouten
        sampleRoutes.add(new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, createSampleGeoPoints()));
        sampleRoutes.add(new Route(8.2, 7.5, "01:15:00", "2023-12-02 12:45:00", 400.0, createSampleGeoPoints()));
        sampleRoutes.add(new Route(12.0, 9.0, "02:00:00", "2023-12-03 16:00:00", 600.0, createSampleGeoPoints()));

        return sampleRoutes;
    }
    private Route createSampleRoute() {

        Route route = new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, createSampleGeoPoints());

        return route;
    }

    private ArrayList<GeoPoint> createSampleGeoPoints() {
        ArrayList<GeoPoint> sampleGeoPoints = new ArrayList<>();

        // Erstellen von drei Beispiel-GeoPoints
        sampleGeoPoints.add(new GeoPoint(0.0, 0.0));
        sampleGeoPoints.add(new GeoPoint(1.0, 1.0));
        sampleGeoPoints.add(new GeoPoint(2.0, 2.0));

        return sampleGeoPoints;
    }

}
