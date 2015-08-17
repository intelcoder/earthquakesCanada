package com.example.fiddlestick.assignment2;

/**
 * This interface contain all the information for database, which used in many class
 *
 * Created by fiddlestick on 2015-08-16.
 */
public interface DbData {
    String DB_NAME = "earthquekes.db";
    String CREATION_TIME_COL = "created_time";
    String NUMBER_RESULT_COL = "num_result";
    String TIME_COL = "time";
    String MAGNITUDE_COL = "magnitude";
    String DEPTH_COL = "depth";
    String LATITUDE_COL = "lat";
    String LONGDITUDE_COL = "lon";
    String LOCATION_COL = "location";
    String[] tableName = {"earth_quake", "metadata"};
    String CREATE_TABLE_EARTHQUAKE = "create table " + tableName[0] + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, time date  , magnitude DECIMAL(3,1), depth DECIMAL(4,2), lat DOUBLE , lon DOUBLE, location TEXT);";
    String CREATE_TABLE_MATADATA = "create table metadata (created_time DATETIME, num_result INTEGER);";
    String JSON_DATA_ADDRESS = "http://www.earthquakescanada.nrcan.gc.ca/api/earthquakes/latest/30d.json";
}
