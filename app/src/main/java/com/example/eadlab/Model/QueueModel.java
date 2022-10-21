package com.example.eadlab.Model;

import com.google.gson.annotations.SerializedName;

public class QueueModel {

    @SerializedName("id")
    String id;

    @SerializedName("shedName")
    String shedName;

    @SerializedName("date")
    String date;

    @SerializedName("fuelType")
    String fuelType;

    @SerializedName("vehicleCount")
    int vehicleCount;

    public QueueModel() {
    }

    public QueueModel(String id, String shedName, String date, String fuelType, int vehicleCount) {
        this.id = id;
        this.shedName = shedName;
        this.date = date;
        this.fuelType = fuelType;
        this.vehicleCount = vehicleCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }
}
