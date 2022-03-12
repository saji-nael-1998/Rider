package com.example.rider.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rider.MainActivity;
import com.example.rider.Model.Driver;
import com.example.rider.R;
import com.example.rider.database.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {
    private LinkedList<Driver> listdata;
    private Context context;


    // RecyclerView recyclerView;
    public DriverAdapter(LinkedList<Driver> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.driver_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Driver myListData = listdata.get(position);
        HashMap<String, String> data = new HashMap<String, String>();

        data.put("userID", myListData.getDriverID());
        MainActivity.dbConnection.getData(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws Exception {

                try {

                    JSONObject driverJSON = result.getJSONObject("driver");

                    holder.nameTV.setText(driverJSON.getString("FName") + " " + driverJSON.getString("LName"));
                    String imagePath=driverJSON.getString("imagePath");
                    //set taxi
                    JSONObject taxiJSON = driverJSON.getJSONObject("taxi");
                    holder.taxiTV.setText(taxiJSON.getString("plate_no"));
                    holder.currentRiderTV.setText(String.valueOf(myListData.getCurrentRider()));
                holder.distanceTV.setText(String.valueOf(myListData.getDistance()));
                    uploadImage( imagePath,holder);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSuccessImage(Bitmap bitmap) throws Exception {

            }

            @Override
            public void onError(String result) {

                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            }
        }, "/driver?userID="+myListData.getDriverID(), data);


    }

    public  void  uploadImage(String imagePath, ViewHolder holder){
        MainActivity.dbConnection.uploadImage(new VolleyCallback() {


            @Override
            public void onSuccess(JSONObject result) throws Exception {

            }

            @Override
            public void onSuccessImage(Bitmap bitmap) throws Exception {
               holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onError(String result) {

            }


        },"upload", imagePath);
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTV;
        public TextView taxiTV;
        public TextView currentRiderTV;
        public TextView distanceTV;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.driver_photo);
            this.taxiTV = (TextView) itemView.findViewById(R.id.driver_taxi);
            this.nameTV = (TextView) itemView.findViewById(R.id.driver_name);
            this.currentRiderTV = (TextView) itemView.findViewById(R.id.current_rider);
            this.distanceTV = (TextView) itemView.findViewById(R.id.distance);

        }
    }
}
