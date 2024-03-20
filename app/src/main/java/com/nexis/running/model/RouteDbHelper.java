package com.nexis.running.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class RouteDbHelper {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String preference;


    public RouteDbHelper(Context context) {
        this.context = context;
        this.preference = "application";
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }

    public RouteDbHelper(Context context, String preference) {
        this.context = context;
        this.preference = preference;
        sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }


    public void save(Route route) {
        Gson gson = new Gson();
        String myRouteAsJson = gson.toJson(route);
        Log.d("MapApp", "save: " + myRouteAsJson);

        String routeKeyGenerate = route.getDateTime();
        Log.d("MapApp", "save: " + routeKeyGenerate);

        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(routeKeyGenerate, myRouteAsJson);
        editor.apply();
    }


    public Route load(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        String routeDataAsString = sharedPreferences.getString(key, null);
        Gson gson = new Gson();
        Route routeData = gson.fromJson(routeDataAsString, Route.class);

        return routeData;
    }



    public ArrayList<Route> loadAll() {
        ArrayList<Route> routeArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry : allEntries.keySet()) {
            routeArrayList.add(load(entry));
        }

        return routeArrayList;
    }



    public void removeAll() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (String entry : allEntries.keySet()) {
            remove(entry);
        }
    }


    public void remove(String key) {
        Log.d("MapApp", "Removing route with key: " + key);
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        boolean commitSuccess = editor.commit();
        Log.d("MapApp", "Remove operation success: " + commitSuccess);
    }

}

