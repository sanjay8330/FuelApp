package com.example.eadlab.Endpoints;

public class EndpointURL {
    //public static final String endpointAddress = "192.168.1.30:5000";//Change the IP address here (Network and settings -> Wifi properties -> Find the IPv4)
    public static final String endpointAddress = "192.168.8.104:5000";//Change the IP address here (Network and settings -> Wifi properties -> Find the IPv4)


    public static final String GET_ALL_CUSTOMERS = "http://" + endpointAddress + "/api/Customer";
    public static final String GET_CUSTOMER_BY_ID = "http://" + endpointAddress + "/api/Customer/";
    public static final String GET_ALL_SHEDS = "http://" + endpointAddress +"/api/Shed";
    public static final String GET_SHEDS_FOR_LOCATION = "http://" + endpointAddress +"/location/";
    public static final String GET_SHED_BY_ID = "http://" + endpointAddress + "/api/Shed/";
    public static final String GET_FUEL_BY_SHEDNAME_FUELTYPE = "http://" + endpointAddress + "/api/Fuel/";
    public static final String GET_QUEUE_BY_SHEDNAME_FUELTYPE = "http://" + endpointAddress + "/api/Queue/";
    public static final String GET_QUEUE_BY_ID = "http://" + endpointAddress + "/api/Queue/";
    public static final String UPDATE_QUEUE_BY_ID = "http://" + endpointAddress + "/api/Queue/";
    public static final String UPDATE_FUEL_BY_ID = "http://" + endpointAddress + "/api/Fuel/";
    public static final String UPDATE_CUSTOMER_BY_ID = "http://" + endpointAddress + "/api/Customer/";

    public static final String ADD_USER = "http://" + endpointAddress + "/api/Users";
    public static final String ADD_CUSTOMER = "http://" + endpointAddress + "/api/Customer";
    public static final String ADD_SHED = "http://" + endpointAddress + "/api/Shed";

}
