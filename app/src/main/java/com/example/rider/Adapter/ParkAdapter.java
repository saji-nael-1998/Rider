package com.example.rider.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rider.Model.Park;
import com.example.rider.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.LinkedList;

public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ViewHolder> {
    private LinkedList<Park> listdata;
    private Context context;


    // RecyclerView recyclerView;
    public ParkAdapter(LinkedList<Park> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ParkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.park_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkAdapter.ViewHolder holder, int position) {
        final Park myListData = listdata.get(position);
        holder.parkNameTIL.getEditText().setText(myListData.getParkName());
        holder.parkLocationTIL.getEditText().setText(myListData.getParkLocation());
        holder.showParkBTN.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String json = gson.toJson(myListData);
            bundle.putString("park",json);
            NavController navController = Navigation.findNavController(view);
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.action_parkListFragment_to_parkFragment2, true).build();


            navController.navigate(R.id.action_parkListFragment_to_parkFragment2, bundle);


        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout parkNameTIL;
        private TextInputLayout parkLocationTIL;
        private Button showParkBTN;

        public ViewHolder(View itemView) {
            super(itemView);
            parkNameTIL=itemView.findViewById(R.id.park_name);
            parkLocationTIL=itemView.findViewById(R.id.park_location);
            showParkBTN=itemView.findViewById(R.id.show_park);
        }
    }
}
