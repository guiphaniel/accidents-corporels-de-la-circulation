package com.example.projet.ui.home;

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
        accidents.setValue(new ArrayList<>());
        loadAccidents("https://data.opendatasoft.com//api/records/1.0/search/?dataset=accidents-corporels-de-la-circulation-millesime%40public&q=&facet=Num_Acc&facet=jour&facet=mois&facet=an&facet=lum&facet=dep&facet=atm&facet=col&facet=lat&facet=long&facet=surf&facet=catv&facet=obs&facet=obsm&facet=grav");
    }

    public void loadAccidents(String link) {
        new ApiConnectAsyncTask().execute(link, accidents);
    }

    public LiveData<ArrayList<Accident>> getAccidents() {
        return accidents;
    }
}