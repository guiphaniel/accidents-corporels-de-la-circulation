package com.example.projet.ui.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projet.api.ApiConnectAsyncTask;
import com.example.projet.model.Accident;

import java.util.ArrayList;

public class ListViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Accident>> accidents;

    public ListViewModel() {
        accidents = new MutableLiveData<>();
        accidents.setValue(new ArrayList< Accident >());
    }

    public void loadAccidents(String link) {
        new ApiConnectAsyncTask().execute(link, accidents);
    }

    public MutableLiveData<ArrayList<Accident>> getAccidents() {
        return accidents;
    }
}