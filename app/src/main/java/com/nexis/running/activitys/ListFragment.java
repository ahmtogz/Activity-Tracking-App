package com.nexis.running.activitys;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.nexis.running.R;
import com.nexis.running.model.Route;
import com.nexis.running.model.RouteDbHelper;
import com.nexis.running.model.RouteListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    public ListView dataListView;
    public RouteDbHelper dbHelper;
    private BarChart chart;

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        dataListView = view.findViewById(R.id.dataListView);


        List<Route> routeListFromDatabase = loadRoutesFromDatabase();
        RouteListAdapter adapter = new RouteListAdapter(getContext(), routeListFromDatabase);
        dataListView.setAdapter(adapter);

        dataListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int which_item = i;

                new AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this item")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Route routeToRemove = routeListFromDatabase.get(which_item);
                                routeListFromDatabase.remove(which_item);
                                removeRouteFromDatabase(routeToRemove);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

        chart = view.findViewById(R.id.chartView);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        setData(7, 100);

        return view;

    }

    public List<Route> loadRoutesFromDatabase() {

        List<Route> routeList = new ArrayList<>();

        Context context = getContext();

        if (context != null) {
            dbHelper = new RouteDbHelper(context);
            routeList = dbHelper.loadAll();
            Log.d("ListFragmentTest", "Loaded routes: " + routeList.size());
        } else {
            Log.e("ListFragmentTest", "Context is null, unable to load routes");
        }

        return routeList;
    }
    public void removeRouteFromDatabase(Route route) {
        RouteDbHelper dbHelper = new RouteDbHelper(requireContext());
        dbHelper.remove(route.getDateTime());
        Log.d("MapApp", "Route removed from the database: " + route.getDateTime());    }



    private void setData(int count, float range) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            float val = (float) (Math.random() * range);

            values.add(new BarEntry(i, val));
        }

        BarDataSet set1 = new BarDataSet(values, "This Week");
        set1.setDrawIcons(false);

        int startColor1 = ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light);
        int endColor1 = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark);

        List<GradientColor> gradientFills = new ArrayList<>();
        gradientFills.add(new GradientColor(startColor1, endColor1));
        set1.setGradientColors(gradientFills);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.5f);

        String[] days = new String[]{"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Sammstag", "Sonntag"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return days[(int) value - 1];
            }
        });

        chart.setData(data);
        chart.invalidate();
    }

}
