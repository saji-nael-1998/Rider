package com.example.rider.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rider.Adapter.DriverAdapter;
import com.example.rider.MainActivity;
import com.example.rider.Model.Driver;
import com.example.rider.Model.Route;
import com.example.rider.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


public class RouteFragment extends Fragment {


    private Route currentRoute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            String json = getArguments().getString("route");
            currentRoute = gson.fromJson(json, Route.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.driver_list);
        LinkedList<Driver> listdata = new LinkedList<Driver>();
        DriverAdapter adapter = new DriverAdapter(listdata, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        ToggleButton inParkTB = view.findViewById(R.id.in_park_TB);
        ToggleButton currentDestTB = view.findViewById(R.id.current_dest_TB);
        currentDestTB.setTextOff(currentRoute.getSrc());
        currentDestTB.setTextOn(currentRoute.getDest());
        currentDestTB.setChecked(true);
        ((TextInputLayout) view.findViewById(R.id.dest)).getEditText().setText(currentRoute.getDest());
        inParkTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getDrivers(isChecked, currentDestTB.isChecked(), listdata, adapter);
                } else {
                    getDrivers(isChecked, currentDestTB.isChecked(), listdata, adapter);
                }
            }
        });
        currentDestTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getDrivers(inParkTB.isChecked(), isChecked, listdata, adapter);
                } else {
                    getDrivers(inParkTB.isChecked(), isChecked, listdata, adapter);
                }
            }
        });
        return view;
    }

    public void getDrivers(boolean inPark, boolean currentDest, LinkedList<Driver> listdata, DriverAdapter adapter) {
        listdata.clear();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                DatabaseReference databaseReference = firebaseDatabase.getReference("days/" + getCurrentDate() + "/" + currentRoute.getParkID() + "/trips/" + currentRoute.getRouteID() + "/");

                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot list) {

                        if (list.getValue() != null) {
                            if (inPark) {
                                if (currentDest) {
                                    LinkedList<Driver> tempList = new LinkedList<>();

                                    for (DataSnapshot driver : list.getChildren()) {
                                        if(driver.child("inPark").getValue().equals("yes")){
                                            if(driver.child("currentDest").getValue().equals(currentRoute.getDest())){
                                                if(MainActivity.locationFinder.getLocation()!=null){
                                                    double lat1=MainActivity.locationFinder.getLocation().getLatitude();
                                                    double lat2=Double.parseDouble(driver.child("lat").getValue().toString());
                                                    double long1=MainActivity.locationFinder.getLocation().getLongitude();
                                                    double long2=Double.parseDouble(driver.child("long").getValue().toString());
                                                    Driver listItem = new Driver(driver.getKey(), Integer.parseInt(driver.child("currentRider").getValue().toString()), calculateDistance(lat1,lat2,long1,long2));
                                                    tempList.add(listItem);
                                                }else{
                                                    Driver listItem = new Driver(driver.getKey(), Integer.parseInt(driver.child("currentRider").getValue().toString()), 0);
                                                    tempList.add(listItem);
                                                }

                                            }
                                        }

                                    }
                                    for (int x = 0; x < tempList.size(); x++) {
                                        if (!listdata.contains(tempList.get(x))) {
                                            listdata.addLast(tempList.get(x));
                                        }
                                    }
                                    LinkedList<Driver> tempListData = listdata;
                                    for (int x = 0; x < tempListData.size(); x++) {
                                        if (!tempList.contains(tempListData.get(x))) {
                                            listdata.remove(x);
                                        }
                                    }
                                } else {
                                    LinkedList<Driver> tempList = new LinkedList<>();

                                    for (DataSnapshot driver : list.getChildren()) {
                                        if(driver.child("inPark").getValue().equals("yes")){
                                            if(driver.child("currentDest").getValue().equals(currentRoute.getSrc())){
                                                Driver listItem = new Driver(driver.getKey(), Integer.parseInt(driver.child("currentRider").getValue().toString()), 0);
                                                tempList.add(listItem);
                                            }
                                        }

                                    }
                                    for (int x = 0; x < tempList.size(); x++) {
                                        if (!listdata.contains(tempList.get(x))) {
                                            listdata.addLast(tempList.get(x));
                                        }
                                    }
                                    LinkedList<Driver> tempListData = listdata;
                                    for (int x = 0; x < tempListData.size(); x++) {
                                        if (!tempList.contains(tempListData.get(x))) {
                                            listdata.remove(x);
                                        }
                                    }
                                }
                            } else {
                                if (currentDest) {
                                    LinkedList<Driver> tempList = new LinkedList<>();

                                    for (DataSnapshot driver : list.getChildren()) {
                                        if(driver.child("inPark").getValue().equals("no")){
                                            if(driver.child("currentDest").getValue().equals(currentRoute.getDest())){
                                                Driver listItem = new Driver(driver.getKey(), Integer.parseInt(driver.child("currentRider").getValue().toString()), 0);
                                                tempList.add(listItem);
                                            }
                                        }

                                    }
                                    for (int x = 0; x < tempList.size(); x++) {
                                        if (!listdata.contains(tempList.get(x))) {
                                            listdata.addLast(tempList.get(x));
                                        }
                                    }
                                    LinkedList<Driver> tempListData = listdata;
                                    for (int x = 0; x < tempListData.size(); x++) {
                                        if (!tempList.contains(tempListData.get(x))) {
                                            listdata.remove(x);
                                        }
                                    }
                                } else {
                                    LinkedList<Driver> tempList = new LinkedList<>();

                                    for (DataSnapshot driver : list.getChildren()) {
                                        if(driver.child("inPark").getValue().equals("no")){
                                            if(driver.child("currentDest").getValue().equals(currentRoute.getSrc())){
                                                Driver listItem = new Driver(driver.getKey(), Integer.parseInt(driver.child("currentRider").getValue().toString()), 0);
                                                tempList.add(listItem);
                                            }
                                        }

                                    }
                                    for (int x = 0; x < tempList.size(); x++) {
                                        if (!listdata.contains(tempList.get(x))) {
                                            listdata.addLast(tempList.get(x));
                                        }
                                    }
                                    LinkedList<Driver> tempListData = listdata;
                                    for (int x = 0; x < tempListData.size(); x++) {
                                        if (!tempList.contains(tempListData.get(x))) {
                                            listdata.remove(x);
                                        }
                                    }
                                }
                            }



                        } else {
                            listdata.clear();
                        }
                        adapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(getContext(), databaseError.toException().toString(), Toast.LENGTH_SHORT).show();

                    }
                };

                databaseReference.addValueEventListener(postListener);
            }
        });
        //start thread
        thread.start();
        if (inPark) {
            if (currentDest) {

            } else {

            }
        } else {

        }
    }

    public String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        // displaying date
        Format f = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = f.format(new Date());
        return strDate;
    }
    public double calculateDistance(double lat1,
                                    double lat2, double lon1,
                                    double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }
}