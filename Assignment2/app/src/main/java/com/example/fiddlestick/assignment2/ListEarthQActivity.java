package com.example.fiddlestick.assignment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListEarthQActivity extends ActionBarActivity implements View.OnClickListener, DbData {
    private DbOpenHelper dbOpenHelper;
    private Intent intent;
    private Button btn;
    private Context con;
    private View detailView;
    private int earthPosition = 0;
    private ArrayList<EarthQuake> quakeArrayList = new ArrayList<EarthQuake>();

    /**
     * On create retrieve OrderByObject from intent, and get a cursor with information that orderbyObject contains
     * The rows that returned with OrderByObject generate listview
     * This method also create Arraylist of EarthQuake object out of cursor
     * Finally, it handles LongClick event.
     * On LongClick event , it pops up alert dialog that contain detail information of clicked row.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_earth_q);

        con = this;
        dbOpenHelper = new DbOpenHelper(this, DB_NAME, null, 1);
        OrderByObject obo = (OrderByObject) getIntent().getSerializableExtra("obo");
        Cursor cursor = dbOpenHelper.getFiltered(obo);
        ListView listView = (ListView) findViewById(R.id.QuakeList);
        intent = new Intent(this, MapActivity.class);// startActivity(intent);
        btn = (Button) findViewById(R.id.toMap);
        btn.setOnClickListener(this);

        if (cursor.getCount() > 0) {
            DoCursorAdapter doCursorAdapter = new DoCursorAdapter(this, cursor, 0);

            listView.setAdapter(doCursorAdapter);

            cursor.moveToFirst();
            while (!cursor.isLast()) {
                String time = cursor.getString(1);
                double mag = cursor.getDouble(2);
                int depth = cursor.getInt(3);
                double lat = cursor.getInt(4);
                double log = cursor.getInt(5);
                String loc = cursor.getString(6);
                cursor.moveToNext();
                quakeArrayList.add(new EarthQuake(time, mag, depth, lat, log, loc));
            }


            intent.putExtra("quakeArrayList", quakeArrayList);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    EarthQuake eq = quakeArrayList.get(position);
                    detailView = getLayoutInflater().inflate(R.layout.detail_earthquake_info, null);
                    earthPosition = position;
                    TextView oTime = (TextView) detailView.findViewById(R.id.detail_origin);
                    TextView mag = (TextView) detailView.findViewById(R.id.detail_magnitude);
                    TextView depth = (TextView) detailView.findViewById(R.id.detail_depth);
                    TextView location = (TextView) detailView.findViewById(R.id.detail_location);
                    TextView lat = (TextView) detailView.findViewById(R.id.detail_latitude);
                    TextView lon = (TextView) detailView.findViewById(R.id.detail_longitude);
                    EarthQuake e = quakeArrayList.get(earthPosition);
                    mag.setText(e.getMagnitude() + "");
                    oTime.setText(e.getDateTime().replace("T", " at ").substring(0, 19));
                    depth.setText(e.getDepth() + "");

                    location.setText(e.getProvince());
                    lat.setText(String.valueOf(e.getLat()));
                    lon.setText(String.valueOf(e.getLon()));

                    oTime.setText(e.getDateTime());
                    mag.setText(e.getMagnitude() + "");

                    //With this alert dialog user can see location of earthquake in google map
                    AlertDialog ag = new AlertDialog.Builder(con).setView(detailView)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Detail Information").setPositiveButton("Google map", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    intent.putExtra("position", earthPosition);
                                    startActivity(intent);
                                }
                            }).show();
                    return false;
                }
            });

        } else {
            //Should not be able to see this
            Toast.makeText(this, "No earthquake matched with filter ", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This methods handles OnResume event
     */
    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_earth_q, menu);
        return true;
    }

    /**
     * This method handles onItemSelected event
     * On refresh button click, it finish current activity which is ListEarthQActivity and start MainActivity for update of the earthquake
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
       if (id == R.id.list_earth_q_refresh) {
            finish();
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * It start MapActivity on button click
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == btn) {
            startActivity(intent);
        }
    }


}
