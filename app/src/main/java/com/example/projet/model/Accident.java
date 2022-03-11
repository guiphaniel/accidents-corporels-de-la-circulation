package com.example.projet.model;

public class Accident {
    private String id;

    private String lat;
    private String lon;
    private String dep;

    private String datetime;

    private String lum;
    private String atm;
    private String surf;
    private String catv;

    private String grav;

    public Accident(String id, String lat, String lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLum() {
        return lum;
    }

    public void setLum(String lum) {
        this.lum = lum;
    }

    public String getAtm() {
        return atm;
    }

    public void setAtm(String atm) {
        this.atm = atm;
    }

    public String getSurf() {
        return surf;
    }

    public void setSurf(String surf) {
        this.surf = surf;
    }

    public String getCatv() {
        return catv;
    }

    public void setCatv(String catv) {
        this.catv = catv;
    }

    public String getGrav() {
        return grav;
    }

    public void setGrav(String grav) {
        this.grav = grav;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}
