package com.android_map.androidmap.models;

public class PixelCoordinates {
    private int latitude1;
    private int longitude1;
    private int latitude2;
    private int longitude2;

    public PixelCoordinates(int latitude1, int longitude1, int latitude2, int longitude2) {
        this.latitude1 = latitude1;
        this.longitude1 = longitude1;
        this.latitude2 = latitude2;
        this.longitude2 = longitude2;
    }

    public int getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(int latitude1) {
        this.latitude1 = latitude1;
    }

    public int getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(int longitude1) {
        this.longitude1 = longitude1;
    }

    public int getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(int latitude2) {
        this.latitude2 = latitude2;
    }

    public int getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(int longitude2) {
        this.longitude2 = longitude2;
    }
}
