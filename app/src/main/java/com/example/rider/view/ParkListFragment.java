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
import com.example.rider.MainActivity;
import com.example.rider.Model.Park;
import com.example.rider.R;
import com.example.rider.database.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


public class ParkListFragment extends Fragment {




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View view=inflater.inflate(R.layout.fragment_park_list, container, false);

        getParks((RecyclerView)view.findViewById(R.id.parks_list));
        return view;

    }
    public void getParks(RecyclerView parkList){
        ProgressDialog progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Loading ....");
        progressDoalog.show();
        MainActivity.dbConnection.getData(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws Exception {

                try {
                    progressDoalog.dismiss();
                    LinkedList<Park> listdata=new LinkedList<>();

                    JSONArray parksJSON = result.getJSONArray("data");
                   for(int x=0;x<parksJSON.length();x++){
                       JSONObject park=parksJSON.getJSONObject(x);
                       listdata.add(new Park(park.getString("park_id"),park.getString("park_name"),park.getString("street")+","+park.getString("city")));
                   }
                    RecyclerView recyclerView = (RecyclerView) parkList;
                    ParkAdapter adapter = new ParkAdapter(listdata, getContext());
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
                System.out.println(result);
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }, "/parks", null);



    }
}