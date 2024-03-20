package com.nexis.running.activitys;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;
import com.nexis.running.R;
import com.nexis.running.model.Route;
import com.nexis.running.model.RouteDbHelper;
import com.nexis.running.model.User;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {
    private Context context;
    private RoadManager roadManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final long INTERVAL_MILLIS = 5000; //Intervall, in dem die Location abgefragt wird in Millisekunden
    private MapView mapView;
    private Button playButton;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean isPlaying = false;
    private CountDownTimer countDownTimer;
    private long elapsedTimeMillis = 0;
    private TextView timeCounter;
    private TextView caloriesBurned;
    private TextView distanceKm;
    private TextView kmPerHour;
    private Button centerButton;
    private Location startLocation; // To store the starting location
    private Location lastLocation;  // To store the last known location
    private  ArrayList<GeoPoint> geoPoints = new ArrayList<>();
    public static final double USERSPEEDKMPERHOUR = 8.0;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));

        context = getActivity();

        String userAsString = getArguments().getString("user");
        if (userAsString!=null){
            Gson gson = new Gson();

            user = gson.fromJson(userAsString, User.class);

        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mapView = view.findViewById(R.id.mapView);
        playButton = view.findViewById(R.id.playButton);
        timeCounter = view.findViewById(R.id.timeCounter);
        caloriesBurned = view.findViewById(R.id.caloriesBurned);
        distanceKm = view.findViewById(R.id.distanceKm);
        //kmPerHour = view.findViewById(R.id.kmPerHour);

        /*

        centerButton = view.findViewById(R.id.centerButton);
        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                centerMapOnUserLocation();
            }
        });

         */

        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        //fügen wir Standard-Zoomschaltflächen und die Möglichkeit zum Zoomen mit zwei Fingern (Multi-Touch) hinzu.
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Add the following lines to initialize the road manager
        roadManager = new OSRMRoadManager(context, "TEST_AGENT");
        ((OSRMRoadManager) roadManager).setMean(OSRMRoadManager.MEAN_BY_FOOT);


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setUpLocationDetails();
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), this::updateMapWithUserLocation);
            startLocationUpdates();
        } else {
            GeoPoint defaultLocation = new GeoPoint(52.520008, 13.404954);
            mapView.getController().setCenter(defaultLocation);

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        mapView.getController().setZoom(17);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    // Stop button is pressed
                    stopTimer();
                    stopLocationUpdates();
                    elapsedTimeMillis = 0;

                    playButton.setBackgroundResource(R.drawable.ic_play_foreground);// Set button  "Play"

                } else {
                    // Play button is pressed
                    startLocationUpdates();
                    startTimer();
                    playButton.setBackgroundResource(R.drawable.ic_stop_foreground); // Set button  "Stop"
                }

                isPlaying = !isPlaying; // Toggle the play state
            }
        });


        return view;
    }
    /*
    private void centerMapOnUserLocation() {
        if (lastLocation != null) {
            GeoPoint userLocation = new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude());
            mapView.getController().animateTo(userLocation);
        }
    }
     */

    private void addMarkerOnMap(Location location) {
        if (location == null) {
            Log.e("HomeFragment", "Location is null");
            return;
        }

        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        Log.d("MapApp", "Geopoint Lon: " + geoPoint.getLongitude() + " Lat: " + geoPoint.getLatitude());

        Marker marker = new Marker(mapView);

        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle("Start point");
        marker.setIcon(ActivityCompat.getDrawable(context, R.drawable.location_marker_foreground));
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    private void updateMapWithUserLocation(Location location) {
        Log.d("MapApp", "Updating map with user location");
        if (location != null) {
            lastLocation = location;  // Update the last known location
            GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapView.getController().animateTo(userLocation);
            geoPoints.add(userLocation);
            addPolylineToMap(location);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null) {
            stopLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    private void setUpLocationDetails() {
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, INTERVAL_MILLIS)
                .setWaitForAccurateLocation(false)
                .setMaxUpdateDelayMillis(1000) //Zeile ist für die Gruppierung von Locations da
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {

                    for (Location location : locationResult.getLocations()) {
                        updateMapWithUserLocation(location);
                        updateDistanceUI(location); // Update distance in real-time
                    }

                }
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }


    private void startTimer() {

        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTimeMillis += 1000;
                updateTimerUI(elapsedTimeMillis);
            }

            @Override
            public void onFinish() {

                // Timer finished
                stopLocationUpdates();


            }
        }.start();

        // Add a marker to the map when the timer starts if lastLocation is not null
        /*
        if (lastLocation != null) {
            addMarkerOnMap(lastLocation);
        }

         */

        /*
        In der startTimer-Methode startLocation = lastLocation; wird verwendet, um den letzten bekannten Standort zu speichern,
        wenn der Timer startet. Dies geschieht, um den Ausgangspunkt der Aktivität des Benutzers zu verfolgen,
        was für die Berechnung der während der Aktivität zurückgelegten Distanz wichtig ist.
         */
        startLocation = lastLocation;
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Save route only when the user explicitly stops the activity
        saveRouteToDatabase(createRouteObject());

    }


    private void updateDistanceUI(Location newLocation) {
        if (startLocation != null) {
            // Calculate distance covered in meters
            float distanceInMeters = newLocation.distanceTo(startLocation);
            double totalDistanceInKm = distanceInMeters / 1000.0; // Convert meters to kilometers
            // Update UI with the distance information
            String distanceText = String.format("km: %.2f", totalDistanceInKm);
            // Assuming you have a TextView named distanceTextView
            distanceKm.setText(distanceText);
        }
    }

    private void updateTimerUI(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        String time = String.format("%02d:%02d:%02d", hours % 60, minutes % 60, seconds % 60);
        timeCounter.setText(time);



        double caloriesBurnedResult = calculateCaloriesBurned(user.getWeight(), USERSPEEDKMPERHOUR, millis);
        String caloriesText = String.format("kcal: %.2f", caloriesBurnedResult);
        caloriesBurned.setText(caloriesText);

    }

    private double calculateCaloriesBurned(double weightKg, double speedKmPerHour, long durationMillis) {
        // MET (Metabolic Equivalent of Task) values for running and walking
        double metRunning = 9.8;
        double metWalking = 3.9;

        // Calculate MET based on speed (assuming 6 km/h threshold for running)
        double met = (speedKmPerHour >= 6) ? metRunning : metWalking;

        // Calculate calories burned using the formula: Calories = MET * weight (kg) * time (hours)
        double caloriesPerMinute = met * weightKg / 60.0;
        double caloriesBurned = caloriesPerMinute * durationMillis / (60 * 1000);

        return caloriesBurned;
    }

    private Route createRouteObject() {
        // Calculate additional information for the route
        double caloriesBurnedResult = calculateCaloriesBurned(user.getWeight(), USERSPEEDKMPERHOUR, elapsedTimeMillis);
        // Format caloriesBurnedResult to two decimal places
        String formattedCaloriesBurned = String.format("%.2f", caloriesBurnedResult);

        // Calculate additional information for the route
        double totalDistanceInKm = lastLocation.distanceTo(startLocation) / 1000.0; // Convert meters to kilometers
        // Format totalDistanceInKm to two decimal places and round to 0.00
        String formattedTotalDistance = String.format("%.2f", totalDistanceInKm);


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String dateTime = formatter.format(date);



        return new Route(
                Double.parseDouble(formattedTotalDistance),     // total distance
                USERSPEEDKMPERHOUR,                             // average speed
                formatElapsedTime(elapsedTimeMillis),           // elapsed time
                dateTime,                                       // date and time
                Double.parseDouble(formattedCaloriesBurned),    // calories burned
                geoPoints
        );
    }
    private void saveRouteToDatabase(Route route) {

        RouteDbHelper dbHelper = new RouteDbHelper(getActivity());
        dbHelper.save(route);

        geoPoints.clear();
        mapView.getOverlays().clear();
        mapView.invalidate();
    }
    private String formatElapsedTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return String.format("%02d:%02d:%02d", hours % 60, minutes % 60, seconds % 60);
    }


    /**
     * Erweitert die aufgezeichnete Route basierend auf dem Standort.
     * @param location Der Standort, der zur Erweiterung der Route verwendet wird.
     */

    private void addPolylineToMap(Location location) {
        new AsyncTask<Location, Void, Road>() {
            @Override
            protected Road doInBackground(Location... locations) {
                if (roadManager != null) {
                    geoPoints.add(new GeoPoint(locations[0]));
                    return roadManager.getRoad(geoPoints);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Road road) {
                if (road != null) {
                    // Clear existing overlays on the map
                    mapView.getOverlays().clear();

                    // Add a Polyline to represent the road on the map
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road, Color.BLUE, 10);
                    mapView.getOverlays().add(roadOverlay);

                    addMarkerOnMap(startLocation);
                    addMarkerOnMap(location);

                    // Invalidate the map to refresh the display
                    mapView.invalidate();
                } else {
                    Log.e("HomeFragment", "RoadManager is null");
                }
            }
        }.execute(location);
    }

}