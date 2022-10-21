package com.example.eadlab.Endpoints;

public class EndpointURL {
    public static final String endpointAddress = "192.168.1.30:5000";

    public static final String GET_ALL_CUSTOMERS = "http://" + endpointAddress + "/api/Customer";
    public static final String GET_CUSTOMER_BY_ID = "http://" + endpointAddress + "/api/Customer/";
    public static final String GET_ALL_SHEDS = "http://" + endpointAddress +"/api/Shed";
    public static final String GET_SHEDS_FOR_LOCATION = "http://" + endpointAddress +"/location/";
    public static final String GET_SHED_BY_ID = "http://" + endpointAddress + "/api/Shed/";
    public static final String GET_FUEL_BY_SHEDNAME_FUELTYPE = "http://" + endpointAddress + "/api/Fuel/";

}
