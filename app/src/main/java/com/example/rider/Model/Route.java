package com.example.rider.Model;

public class Route {
    private String parkID;
    private String RouteID;
    private String dest;
    private String src;

    public Route(String parkID, String routeID, String dest, String src) {
        this.parkID = parkID;
        RouteID = routeID;
        this.dest = dest;
        this.src = src;
    }

    public String getParkID() {
        return parkID;
    }

    public void setParkID(String parkID) {
        this.parkID = parkID;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
