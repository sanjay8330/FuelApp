package com.example.eadlab.Model;

import com.google.gson.annotations.SerializedName;

public class ShedModel {

    @SerializedName("id")
    String id;

    @SerializedName("shedName")
    String shedName;

    @SerializedName("location")
    String location;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
