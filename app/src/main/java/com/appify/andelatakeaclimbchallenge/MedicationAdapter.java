package com.appify.andelatakeaclimbchallenge;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yinka Ige on 02/04/2018.
 */

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    ArrayList<Medication> medicationArrayList;

    MedicationAdapter(ArrayList<Medication> medicationArrayList) {
        this.medicationArrayList = medicationArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.medication_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medication med = medicationArrayList.get(position);
        holder.nameTv.setText(med.getName());
        holder.numberTv.setText(String.valueOf(med.getInterval()));
    }

    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView numberTv, nameTv;
        public ViewHolder(View itemView) {
            super(itemView);
            numberTv = (TextView)itemView.findViewById(R.id.numberTv);
            nameTv = (TextView)itemView.findViewById(R.id.nameTv);
        }
    }
}
