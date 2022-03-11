package com.example.projet.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet.R;
import com.example.projet.model.Accident;

import java.util.ArrayList;

//TODO: https://developer.android.com/guide/topics/ui/layout/recyclerview

public class AccidentAdapter extends RecyclerView.Adapter<AccidentAdapter.ViewHolder> {

    private ArrayList<Accident> accidents;

    public AccidentAdapter() {
        accidents = new ArrayList<>();
    }

    public void setAccidents(ArrayList<Accident> accidents) {
        this.accidents = accidents;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ViewHolder(@NonNull View view) {
            super(view);

            tv = (TextView) view.findViewById(R.id.textView);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull AccidentAdapter.ViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.tv.setText(accidents.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return accidents.size();
    }
}
