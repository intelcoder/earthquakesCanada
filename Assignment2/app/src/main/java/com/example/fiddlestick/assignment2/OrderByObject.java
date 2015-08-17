package com.example.fiddlestick.assignment2;

import java.io.Serializable;

/**
 * Created by fiddlestick on 2015-08-09.
 */
public class OrderByObject implements Serializable {
    private double magnitude = 0.0;
    private String toDate = "oldest";
    private String fromDate = "lastest";
    private String location = "CANADA";
    private String orderBy = "magnitude"; // =MAGNITUDE_COL


    /**
     * This method initialize class field with passed argument
     * @param magnitude
     * @param toDate
     * @param fromDate
     * @param location
     * @param order
     */
    public OrderByObject(double magnitude, String toDate, String fromDate, String location, String order) {

        this.magnitude = magnitude;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.location = location;
        this.orderBy = order;

    }

    /**
     * It return magnitude
     * @return
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * It return date
     * @return
     */
    public String getToDate() {
        return toDate;
    }
    /**
     * It return date
     * @return
     */
    public String getFromDate() {
        return fromDate;
    }
    /**
     * It return location
     * @return
     */
    public String getLocation() {
        return location;
    }
    /**
     * It return orderby information
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public String toString() {
        return "OrderByObject{" +
                "magnitude=" + magnitude +
                ", toDate='" + toDate + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", location='" + location + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}

