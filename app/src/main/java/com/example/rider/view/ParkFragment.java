package com.example.rider.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rider.Adapter.ParkAdapter;
import com.example.rider.Adapter.RouteAdapter;
import com.example.rider.MainActivity;
import com.example.rider.Model.Park;
import com.example.rider.Model.Route;
import com.example.rider.R;
import com.example.rider.database.VolleyCallback;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ParkFragment extends Fragment {

    private Park currentPark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            Gson gson = new Gson();
            String json = getArguments().getString("park");
            currentPark = gson.fromJson(json, Park.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.fragment_park, container, false);
        ((TextInputLayout) view.findViewById(R.id.park_name)).getEditText().setText(currentPark.getParkName());
        ((TextInputLayout) view.findViewById(R.id.park_location)).getEditText().setText(currentPark.getParkLocation());
        getRoute(((RecyclerView) view.findViewById(R.id.route_list)));

        return view;
    }

    public void getRoute(RecyclerView routeRV) {
        ProgressDialog progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Loading ....");
        progressDoalog.show();
        MainActivity.dbConnection.getData(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws Exception {

                try {
                    progressDoalog.dismiss();
                    LinkedList<Route> listdata = new LinkedList<>();

                    JSONArray parksJSON = result.getJSONArray("data");
                    for (int x = 0; x < parksJSON.length(); x++) {
                        JSONObject park = parksJSON.getJSONObject(x);
                        listdata.add(new Route(currentPark.getParkID(),park.getString("route_id"), park.getString("dest"),currentPark.getParkLocation().split(",")[1]));
                    }
                    RecyclerView recyclerView = (RecyclerView) routeRV;
                    RouteAdapter adapter = new RouteAdapter(listdata, getContext());
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccessImage(Bitmap bitmap) throws Exception {

            }

            @Override
            public void onError(String result) {
                progressDoalog.dismiss();

                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }, "/park/"+currentPark.getParkID()+"/routes", null);


    }

}