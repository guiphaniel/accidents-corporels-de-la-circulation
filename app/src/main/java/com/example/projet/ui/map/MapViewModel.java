package com.example.projet.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet.api.ApiConnectClusterAsyncTask;
import com.example.projet.api.ApiConnectAccidentsAsyncTask;
import com.example.projet.model.Accident;
import com.example.projet.model.Cluster;

import java.util.ArrayList;

public class MapViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Cluster>> clusters;
    private final MutableLiveData<ArrayList<Accident>> accidents;

    public MapViewModel() {
        clusters = new MutableLiveData<>();
        clusters.setValue(new ArrayList<>());

        accidents = new MutableLiveData<>();
        accidents.setValue(new ArrayList< Accident >());
    }

    public MutableLiveData<ArrayList<Cluster>> getClusters() {
        return clusters;
    }

    public MutableLiveData<ArrayList<Accident>> getAccidents() {
        return accidents;
    }

    public void loadClusters(String link) {
        new ApiConnectClusterAsyncTask().execute(link, clusters);
    }

    public void loadAccidents(String link) {
        new ApiConnectAccidentsAsyncTask().execute(link, accidents);
    }
}