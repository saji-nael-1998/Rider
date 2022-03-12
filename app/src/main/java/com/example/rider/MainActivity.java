package com.example.rider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rider.Tools.LocationFinder;
import com.example.rider.database.DBConnection;

public class MainActivity extends AppCompatActivity {
    public static DBConnection dbConnection;
    public static LocationFinder locationFinder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbConnection = new DBConnection(this);
        locationFinder=new LocationFinder(this);


    }
}