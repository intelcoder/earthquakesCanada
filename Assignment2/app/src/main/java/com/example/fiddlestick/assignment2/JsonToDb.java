package com.example.fiddlestick.assignment2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by fiddlestick on 8/5/2015.
 */
public class JsonToDb implements DbData {
    static DbOpenHelper dbOpenHelper;
    private String DB_PATH;
    private boolean updateFlag = true;
    private int updatedNRow = 0;

    /**
     * This constructor read json string and create object and array out of it.
     * It also insert all the json object into sqlite database
     * Finally, it checks if json file has been update or not.
     *
     * @param json
     * @param con
     */
    public JsonToDb(String json, Context con) {

        try {
            JSONObject obj = new JSONObject(json);
            Iterator<?> keys = obj.keys();
            //get metadata object then request object from inside of metadatal
            JSONObject job = (JSONObject) obj.getJSONObject("metadata").getJSONObject("request");
            File file = new File("/data/data/" + "com.example.fiddlestick.assignment2" + "/databases/+" + DB_NAME);
            if (file.exists()) {
                dbOpenHelper = new DbOpenHelper(con, DB_NAME, null, 1);
                Cursor creation = dbOpenHelper.getAllMatadata();
                if (creation != null && creation.getCount() >= 1) {
                    creation.moveToFirst();
                    if (creation.getString(0).compareTo((String) job.get("dateModified")) != 0) {
                        updatedNRow = job.getInt("resultCount") - creation.getInt(1);
                        file.delete();
                    }
                }
                dbOpenHelper.close();
            }
            if (updateFlag) {
                dbOpenHelper = new DbOpenHelper(con, DB_NAME, null, 1);
                dbOpenHelper.openDataBase(); //drop the table for update
                dbOpenHelper.insertMetadata(job.getString("dateModified"), job.getInt("resultCount"));
                keys.next();//Skip the matadata part
                while (keys.hasNext()) {
                    JSONObject tmp = obj.getJSONObject((String) keys.next());
                    JSONObject geo = (JSONObject) tmp.get("geoJSON");
                    JSONArray coordi = geo.getJSONArray("coordinates");
                    JSONObject location = (JSONObject) tmp.get("location");
                    String[] tString = location.get("en").toString().split(", ");
                    dbOpenHelper.insertEarthquake(tmp.getString("origin_time"), tmp.getDouble("magnitude")
                            , tmp.getInt("depth"), coordi.getDouble(0), coordi.getDouble(1), tString[1]);

                }
                dbOpenHelper.close();
                Log.d("meta", "" + updatedNRow);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * It returns number of rows that has been updated
     * @return
     */
    public int getUpdateResult() {
        return updatedNRow;
    }

}

