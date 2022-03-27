package com.example.projet.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class Cluster {
    LatLng location;
    int count;

    public LatLng getLocation() {
        return location;
    }

    public int getCount() {
        return count;
    }

    public Cluster(LatLng location, int count) {
        this.location = location;
        this.count = count;
    }
}
