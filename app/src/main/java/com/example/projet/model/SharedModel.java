package com.example.projet.model;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet.api.ApiConnectAsyncTask;

import java.util.ArrayList;

public class SharedModel extends ViewModel {

    private MutableLiveData<Location> location;
    private MutableLiveData<Integer> radius;

    public SharedModel() {
        location = new MutableLiveData<>();
        radius = new MutableLiveData<>();
        radius.setValue(new Integer(1000));
    }
    public MutableLiveData<Location> getLocation() {
        return location;
    }

    public MutableLiveData<Integer> getRadius() {
        return radius;
    }
}