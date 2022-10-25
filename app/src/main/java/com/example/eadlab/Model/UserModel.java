package com.example.eadlab.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("vehicleType")
    String vehicleType;

    @SerializedName("vehicleNumber")
    String vehicleNumber;

    @SerializedName("fuelType")
    String fuelType;

    @SerializedName("remainFuelQuota")
    int remainFuelQuota;

    public UserModel() {
    }

    public UserModel(String id, String name, String vehicleType, String vehicleNumber, String fuelType, int remainFuelQuota) {
        this.id = id;
        this.name = name;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.fuelType = fuelType;
        this.remainFuelQuota = remainFuelQuota;
    }

    public int getRemainFuelQuota() {
        return remainFuelQuota;
    }

    public void setRemainFuelQuota(int remainFuelQuota) {
        this.remainFuelQuota = remainFuelQuota;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
