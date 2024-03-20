package com.nexis.running.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class RouteTest {
    IRoute route;
    private IRoute createSampleRoute() {
        return new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, new ArrayList<>());
    }
    @Test
    public void testGetTotalDistance() {
        route = createSampleRoute();
        assertNotEquals(0.0, route.getTotalDistance());
    }

    @Test
    public void testGetAverageSpeed() {
        route = createSampleRoute();
        assertNotEquals(0.0, route.getAverageSpeed());
    }

    @Test
    public void testGetElapsedTime() {
        route = createSampleRoute();
        assertNotNull(route.getElapsedTime());
        assertNotEquals("", route.getElapsedTime().trim());
    }


    @Test
    public void testGetDateTime() {
        route = createSampleRoute();
        assertNotNull(route.getDateTime());
        assertNotEquals("", route.getDateTime().trim());
    }


    @Test
    public void testGetCaloriesBurned() {
        IRoute route = createSampleRoute();
        assertNotEquals(0.0, route.getCaloriesBurned());
    }

    @Test
    public void testGetGeoPoints() {
        IRoute route = createSampleRoute();
        ArrayList<GeoPoint> geoPoints = route.getGeoPoints();
        assertNotNull(geoPoints);
        assertEquals(0, geoPoints.size());
    }


    @Test
    public void testGetGeoPointsNegative() {
        IRoute route = createSampleRouteWithNullGeoPoint();
        ArrayList<GeoPoint> geoPoints = route.getGeoPoints();
        assertNotNull(geoPoints);
        assertTrue(geoPoints.contains(null));
    }



    private IRoute createSampleRouteWithNullGeoPoint() {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(0.0, 0.0));
        geoPoints.add(null);
        geoPoints.add(new GeoPoint(1.0, 1.0));
        return new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, geoPoints);
    }

    // 1. Positiver Test: Überprüfen, ob getTotalDistance() die erwartete Gesamtdistanz zurückgibt.
    @Test
    public void testGetTotalDistancePositive() {
        IRoute route = createSampleRouteWithPositiveDistance();
        assertTrue(route.getTotalDistance() > 0.0);
    }

    // 2. Negativer Test: Überprüfen, ob getAverageSpeed() einen negativen Wert zurückgibt.
    @Test
    public void testGetAverageSpeedNegative() {
        IRoute route = createSampleRouteWithNegativeAverageSpeed();
        assertFalse(route.getAverageSpeed() >= 0.0);
    }

    // 3. Negativer Test: Überprüfen, ob getElapsedTime() null oder leer zurückgibt.
    @Test
    public void testGetElapsedTimeNullOrEmpty() {
        IRoute route = createSampleRouteWithNullOrEmptyElapsedTime();
        assertNotNull(route.getElapsedTime());
        assertFalse(route.getElapsedTime().isEmpty());
    }



    private IRoute createSampleRouteWithPositiveDistance() {
        return new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, new ArrayList<>());
    }

    private IRoute createSampleRouteWithNegativeAverageSpeed() {
        return new Route(10.5, -8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, new ArrayList<>());
    }

    private IRoute createSampleRouteWithNullOrEmptyElapsedTime() {
        return new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", 500.0, new ArrayList<>());
    }

    private IRoute createSampleRouteWithNullOrEmptyDateTime() {
        return new Route(10.5, 8.0, "01:30:00", "", 500.0, new ArrayList<>());
    }

    private IRoute createSampleRouteWithNegativeCaloriesBurned() {
        return new Route(10.5, 8.0, "01:30:00", "2023-12-01 14:30:00", -500.0, new ArrayList<>());
    }



}