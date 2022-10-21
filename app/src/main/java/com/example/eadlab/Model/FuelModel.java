package com.example.eadlab.Model;

import com.google.gson.annotations.SerializedName;

public class FuelModel {

    @SerializedName("id")
    String id;

    @SerializedName("fuelType")
    String fuelType;

    @SerializedName("shedName")
    String shedName;

    @SerializedName("date")
    String date;

    @SerializedName("arrivalTime")
    String arrivalTime;

    @SerializedName("arrivedLitres")
    String arrivedLitres;

    @SerializedName("remainLitres")
    String remainLitres;

    public FuelModel() {
    }

    public FuelModel(String id, String fuelType, String shedName, String date, String arrivalTime, String arrivedLitres, String remainLitres) {
        this.id = id;
        this.fuelType = fuelType;
        this.shedName = shedName;
        this.date = date;
        this.arrivalTime = arrivalTime;
        this.arrivedLitres = arrivedLitres;
        this.remainLitres = remainLitres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getShedName() {
        return shedName;
    }

    public void setShedName(String shedName) {
        this.shedName = shedName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivedLitres() {
        return arrivedLitres;
    }

    public void setArrivedLitres(String arrivedLitres) {
        this.arrivedLitres = arrivedLitres;
    }

    public String getRemainLitres() {
        return remainLitres;
    }

    public void setRemainLitres(String remainLitres) {
        this.remainLitres = remainLitres;
    }
}
