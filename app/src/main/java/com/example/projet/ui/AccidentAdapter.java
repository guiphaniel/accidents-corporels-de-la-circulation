package com.example.projet.ui;

import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;
import com.example.projet.model.Accident;

import java.util.ArrayList;

//TODO: https://developer.android.com/guide/topics/ui/layout/recyclerview

public abstract class AccidentAdapter extends RecyclerView.Adapter<AccidentAdapter.ViewHolder> {

    private ArrayList<Accident> accidents;

    public AccidentAdapter() {
        accidents = new ArrayList<>();
    }

    public void setAccidents(ArrayList<Accident> accidents) {
        this.accidents = accidents;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLocation;
        public TextView tvDate;
        public ImageView ivGrav;

        public ViewHolder(@NonNull View view, AccidentAdapter adapter) {
            super(view);

            ivGrav = (ImageView) view.findViewById(R.id.ivGrav);
            tvLocation = (TextView) view.findViewById(R.id.tvDate);
            tvDate = (TextView) view.findViewById(R.id.tvLocation);

            itemView.setOnClickListener(view1 -> {adapter.onClick(getAdapterPosition());});
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull AccidentAdapter.ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Accident accident = accidents.get(position);

        holder.tvLocation.setText(accident.adr + ", dpt : " + accident.dep);
        holder.tvDate.setText(String.join("-", accident.jour, accident.mois, accident.an));

        holder.ivGrav.setImageURI(null);
        Uri imgUri;
        String g = accident.grav;
        if(g.contains("Tué")) {
            imgUri=Uri.parse(String.valueOf("android.resource://com.example.projet/" + R.drawable.dead));
            holder.ivGrav.setImageURI(imgUri);
        } else if(g.contains("Blessé")) {
            imgUri=Uri.parse("android.resource://com.example.projet/" + R.drawable.hurt);
            holder.ivGrav.setImageURI(imgUri);
        } else {
            imgUri=Uri.parse("android.resource://com.example.projet/" + R.drawable.unhurt);
            holder.ivGrav.setImageURI(imgUri);
        }
    }

    @Override
    public int getItemCount() {
        return accidents.size();
    }

    abstract public void onClick(int position);
}
