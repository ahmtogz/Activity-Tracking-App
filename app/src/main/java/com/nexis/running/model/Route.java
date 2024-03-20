package com.nexis.running.model;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public class Route implements IRoute{
    private double totalDistance;
    private  double averageSpeed;
    private String elapsedTime;
    private String dateTime;
    private double caloriesBurned;
    private ArrayList<GeoPoint> geoPoints;

    public Route(double totalDistance, double averageSpeed, String elapsedTime, String dateTime, double caloriesBurned, ArrayList<GeoPoint> geoPoints) {
        this.totalDistance = totalDistance;
        this.averageSpeed = averageSpeed;
        this.elapsedTime = elapsedTime;
        this.dateTime = dateTime;
        this.caloriesBurned = caloriesBurned;
        this.geoPoints = geoPoints;
    }


    @Override
    public double getTotalDistance() {
        return totalDistance;
    }

    @Override
    public double getAverageSpeed() {
        return averageSpeed;
    }

    @Override
    public String getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String getDateTime() {
        return dateTime;
    }

    @Override
    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    @Override
    public ArrayList<GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public void setGeoPoints(ArrayList<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }
}
