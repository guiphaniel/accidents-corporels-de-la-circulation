package com.example.projet.ui.home;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet.api.ApiConnectAsyncTask;
import com.example.projet.model.Accident;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Accident>> accidents;

    public HomeViewModel() {
        accidents = new MutableLiveData<>();
    }

    public void loadAccidents(String link) {
        new ApiConnectAsyncTask().execute(link, accidents);
    }

    public MutableLiveData<ArrayList<Accident>> getAccidents() {
        return accidents;
    }
}