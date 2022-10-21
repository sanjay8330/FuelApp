package com.example.eadlab.Wrapper;

public class SpinnerWrapper {
    private String id;
    private String name;

    @Override
    public String toString(){
        return name;
    }

    public SpinnerWrapper(){
        this.id = "";
        this.name = "";
    }

    public SpinnerWrapper(String id, String name) {
        this.id = id;
        this.name = name;
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
}
