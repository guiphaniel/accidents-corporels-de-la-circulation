package com.example.projet.model;

import java.io.Serializable;

public class Accident implements Serializable {
    public String num_acc; // id

    private String lat;
    private String lon;
    public String adr;
    public String dep;

    public String jour;
    public String mois;
    public String an;

    public String lum;
    public String atm;
    public String surf;
    public String catv;

    public String grav;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "id='" + num_acc + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
