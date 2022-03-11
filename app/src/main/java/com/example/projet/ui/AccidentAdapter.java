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
    private Context context;

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public ViewHolder(@NonNull View view) {
            super(view);

            tv = view.findViewById(R.id.textView);
        }
    }

    public AccidentAdapter(Context context, ArrayList<Accident> users) {
        this.context = context;
        this.accidents = users;
    }

    @NonNull
    @Override
    public AccidentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AccidentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Accident accident = accidents.get(position);

        ConstraintLayout layoutItem;
        LayoutInflater mInflater = LayoutInflater.from(context);
        //(1) : Réutilisation du layout
        if (convertView == null) {
            layoutItem = (ConstraintLayout) mInflater.inflate(R.layout.item_layout, parent, false);
        } else {
            layoutItem = (ConstraintLayout) convertView;
        }

        ViewHolder viewHolder = (ViewHolder) layoutItem.getTag();

        viewHolder.tv.setText(accident.toString());

        //On retourne l'item créé.
        return layoutItem;
    }
}
