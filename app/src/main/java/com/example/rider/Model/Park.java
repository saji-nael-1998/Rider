package com.example.rider.Model;

import java.util.LinkedList;

public class Park {
    private String parkID;

    private String parkName;
    private String parkLocation;
    private LinkedList<Route> routes;

    public Park(String parkID, String parkName, String parkLocation) {
        this.parkID = parkID;
        this.parkName = parkName;
        this.parkLocation = parkLocation;
        routes =new LinkedList<>();
    }

    public String getParkID() {
        return parkID;
    }

    public void setParkID(String parkID) {
        this.parkID = parkID;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkLocation() {
        return parkLocation;
    }

    public void setParkLocation(String parkLocation) {
        this.parkLocation = parkLocation;
    }

    public LinkedList<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route route){
        routes.add(route);
    }
}
