package com.example.fiddlestick.assignment2;

import java.io.Serializable;

/**
 * Created by fiddlestick on 8/5/2015.
 */
public class EarthQuake implements Serializable{

    private String dateTime;
    private double magnitude;
    private int depth;
    private double lat;
    private double lon;
    private String province;

    @Override
    public String toString() {
        return "EarthQuake{" +
                "dateTime='" + dateTime + '\'' +
                ", magnitude=" + magnitude +
                ", depth=" + depth +
                ", lat=" + lat +
                ", lon=" + lon +
                ", province='" + province + '\'' +
                '}';
    }

    public EarthQuake(String dateTime, double magnitude, int depth, double lat, double lon, String province) {
        this.dateTime = dateTime;
        this.magnitude = magnitude;
        this.depth = depth;
        this.lat = lat;
        this.lon = lon;
        this.province = province;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public int getDepth() {
        return depth;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getProvince() {
        return province;
    }
}
