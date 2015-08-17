package com.example.fiddlestick.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * This class handles Sqlite data base from open, insert, create and so on
 * Created by fiddlestick on 8/5/2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper implements DbData {
    private Context context;
    //EARTHQUAKE table

    //MATADATA table

    private final String DB_PATH;
    private String DB_NAME;
    private SQLiteDatabase sqliteDb;

    /**
     * This class initialize database anme, path, and context with passed argument
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        DB_NAME = name;
        DB_PATH = "/data/data/" + context.getPackageName();
        Log.d("ddd", DB_PATH);
        this.context = context;

    }

    //onCreate is called when getWritableDatabase() or getReadableDatabase();

    /**
     * This method drops all the table if it is exist and create new table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBOPEN", "In oncreate");
        //  Toast.makeText(context,"CREATE", Toast.LENGTH_LONG).show();
        db.execSQL("DROP TABLE IF EXISTS " + tableName[0]);
        db.execSQL("DROP TABLE IF EXISTS " + tableName[1]);
        db.execSQL(CREATE_TABLE_EARTHQUAKE);
        db.execSQL(CREATE_TABLE_MATADATA);

    }

    //If database exit, it onUpgrade will be called
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
        Log.d("DBOPEN", "In onUpgrade");
    }

    //Need to store

    /**
     * This method insert metadata information into metadata table
     * @param date
     * @param numResult
     * @return If passed argument is valid, it returns true. Otherwise, it returns false.
     */
    public boolean insertMetadata(String date, int numResult) {
        if (date != null && numResult > 0) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Log.d("INSERTMeta", "In insermata");
            ContentValues contentValues = new ContentValues();
            contentValues.put(CREATION_TIME_COL, date);
            contentValues.put(NUMBER_RESULT_COL, numResult);
            sqLiteDatabase.insert(tableName[1], null, contentValues);
            return true;
        }
        return false;
    }

    /**
     * This method insert metadata information into earthquake table
     * @param date
     * @param magnitude
     * @param depth
     * @param lat
     * @param lon
     * @param location
     */
    public void insertEarthquake(String date, double magnitude, double depth, double lat, double lon, String location) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_COL, date);
        contentValues.put(MAGNITUDE_COL, magnitude);
        contentValues.put(DEPTH_COL, depth);
        contentValues.put(LATITUDE_COL, lat);
        contentValues.put(LONGDITUDE_COL, lon);
        contentValues.put(LOCATION_COL, location);

        //  Log.d("Inserte", date + "  " + magnitude);
        this.getWritableDatabase().insert(tableName[0], null, contentValues);
        //   Log.d("INSERTEarth", "In insermata");
    }


    /**
     * This method will drop all the tables and recreate for new update
     *
     * @return
     * @throws SQLException
     */
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + "/" + DB_NAME;
        Log.d("DBOPEN", mPath);
        sqliteDb = this.getWritableDatabase();
        sqliteDb.execSQL("DROP TABLE IF EXISTS " + tableName[0]);
        sqliteDb.execSQL("DROP TABLE IF EXISTS " + tableName[1]);
        sqliteDb.execSQL(CREATE_TABLE_EARTHQUAKE);
        sqliteDb.execSQL(CREATE_TABLE_MATADATA);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return sqliteDb != null;
    }

    /**
     * This method returns all the rows in metadata table
     * @return
     */
    public Cursor getAllMatadata() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "select * from " + tableName[1] + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null); //if db get closed, cursor will be invalid
        return cursor;
    }

   /* public Cursor getAllEarthquake() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "select _id, " +
                TIME_COL + ", "
                + MAGNITUDE_COL + ", "
                + DEPTH_COL + ", "
                + LATITUDE_COL + " , "
                + LONGDITUDE_COL + " , "
                + LOCATION_COL + " from "
                + tableName[0]
                + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }*/

    /**
     * This method execute query with information that OrderByObject  contains and returns cursor
     * @param obo
     * @return
     */
    public Cursor getFiltered(OrderByObject obo) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String whereQuery = "";
        ArrayList<String> compareValue = new ArrayList<>();
        if (obo.getMagnitude() != 0.0) {
            whereQuery += MAGNITUDE_COL + "= ? and ";
            compareValue.add(String.valueOf(obo.getMagnitude()));
        }
        whereQuery += TIME_COL + " between ? and ";

        compareValue.add(obo.getToDate());
        whereQuery += " ?  ";

        compareValue.add(obo.getFromDate());
        if (obo.getLocation().compareTo("ALL") != 0) {
            whereQuery += " and " + LOCATION_COL + " =? ";
            compareValue.add(String.valueOf(obo.getLocation()));
        }
        String[] colume = new String[compareValue.size()];
        for (int i = 0; i < compareValue.size(); i++) {
            colume[i] = compareValue.get(i);
        }
        Cursor cursor = sqLiteDatabase.query(tableName[0],
                new String[]{"_ID", TIME_COL, MAGNITUDE_COL, DEPTH_COL, LATITUDE_COL, LONGDITUDE_COL, LOCATION_COL}, whereQuery, colume, null, null, obo.getOrderBy() + " desc ", null);

        return cursor;
    }

}

