package com.example.rider.Model;

public class Driver implements Comparable<Driver> {
    private String driverID;

    private int currentRider;
    private double distance;

    public Driver(String driverID, int currentRider, double distance) {
        this.driverID = driverID;
        this.currentRider = currentRider;
        this.distance = distance;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public int getCurrentRider() {
        return currentRider;
    }

    public void setCurrentRider(int currentRider) {
        this.currentRider = currentRider;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Driver driver) {
        if (this.distance<driver.distance){
            return 1;
        }else if (this.distance>driver.distance){
            return -1;
        }
        return 0;
    }
}
