package com.nexis.running.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nexis.running.R;

import java.util.List;

public class RouteListAdapter extends ArrayAdapter<Route> {

    private List<Route> routeList;

    public RouteListAdapter(@NonNull Context context, List<Route> routeList) {
        super(context, 0, routeList);
        this.routeList = routeList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);
        }

        Route currentRoute = routeList.get(position);



        TextView textViewDate = listItemView.findViewById(R.id.textViewDate_);
        TextView textViewElapsedTime = listItemView.findViewById(R.id.textViewElapsedTime_);
        TextView textViewKalorie = listItemView.findViewById(R.id.textViewKalorie_);
        TextView textViewDistance = listItemView.findViewById(R.id.textViewDistance_);

        textViewDate.setText(currentRoute.getDateTime());
        textViewElapsedTime.setText(" Time: " + currentRoute.getElapsedTime());
        textViewKalorie.setText(" Kcal: " + currentRoute.getCaloriesBurned());
        textViewDistance.setText(" Dist: " + currentRoute.getTotalDistance() + " km");


        return listItemView;
    }


}
