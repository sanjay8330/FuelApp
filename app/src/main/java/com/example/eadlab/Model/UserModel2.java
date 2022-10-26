package com.example.eadlab.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel2 {

    @SerializedName("id")
    String id;

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    @SerializedName("mobile")
    String contact;

    @SerializedName("userrole")
    String role;

    public UserModel2() {
    }

    public UserModel2(String id, String username, String password, String contact, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.contact = contact;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
