package com.example.projet.api;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projet.model.Accident;
import com.example.projet.ui.AccidentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ApiConnectListAsyncTask extends AsyncTask<Object, Void, String> {
    private MutableLiveData<ArrayList<Accident>> accidents;
    private AccidentAdapter adapter;

    @Override
    protected String doInBackground(Object... objects) {
        String link = (String) objects[0];
        accidents = (MutableLiveData<ArrayList<Accident>>) objects[1];

        String message = "";

        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream() ) );
                message = in.readLine(); // ou boucle tant qu’il y a des lignes
                in.close(); // et on ferme le flux
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.disconnect();

        if (message.isEmpty())
            return "Error";

        ArrayList<Accident> tmpAccidents = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(message);
            JSONArray records = object.getJSONArray("records");

            for (int i = 0; i < records.length(); i++) {
                JSONObject record = (JSONObject) records.get(i);
                JSONObject fields = record.getJSONObject("fields");

                // if some fields are empty, catch
                try {
                    Accident accident = new Accident();

                    for(Field f : accident.getClass().getFields()) {
                        f.setAccessible(true);
                        String name = f.getName();
                        if (fields.has(name)) {
                            if(f.getName() != "lon")  // checking for lon is mandatory, as long is already used as a type
                                f.set(accident, fields.getString(f.getName()));
                            else
                                f.set(accident, fields.getString("long"));
                        } else {
                            f.set(accident, "Inconnu(e)");
                        }
                    }

                    tmpAccidents.add(accident);
                } catch (JSONException | IllegalAccessException e) {
                    Log.e("json_parsing", e.getMessage());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        accidents.getValue().addAll(tmpAccidents);
        accidents.postValue(accidents.getValue());
        return "Success";
    }
}
