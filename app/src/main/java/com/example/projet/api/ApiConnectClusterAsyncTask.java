package com.example.projet.api;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.example.projet.model.Cluster;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ApiConnectClusterAsyncTask extends AsyncTask<Object, Void, String> {
    private MutableLiveData<ArrayList<Cluster>> clusters;

    @Override
    protected String doInBackground(Object... objects) {
        String link = (String) objects[0];
        clusters = (MutableLiveData<ArrayList<Cluster>>) objects[1];

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

        ArrayList<Cluster> tmpClusters = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(message);
            JSONArray clusters = object.getJSONArray("clusters");

            for (int i = 0; i < clusters.length(); i++) {
                JSONObject cluster = (JSONObject) clusters.get(i);

                JSONArray center = cluster.getJSONArray("cluster_center");

                tmpClusters.add(new Cluster(new LatLng((double) center.get(0), (double) center.get(1)), cluster.getInt("count")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        clusters.getValue().addAll(tmpClusters);
        clusters.postValue(clusters.getValue());
        return "Success";
    }
}
