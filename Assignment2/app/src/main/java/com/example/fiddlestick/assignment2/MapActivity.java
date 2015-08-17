package com.example.fiddlestick.assignment2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        //quakeArrayList
        createMapView();
        addMarker();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                LatLng l = marker.getPosition();
                View v = getLayoutInflater().inflate(R.layout.marker_window_layout, null);
                TextView v1 = (TextView) v.findViewById(R.id.marker_window_lat);
                TextView v2 = (TextView) v.findViewById(R.id.marker_window_lon);

                String[] tmp = marker.getSnippet().split("\n");

                v1.setText("Time Info : " + tmp[0].replace("T" , "\n"));
                v2.setText("Magnitude : " + tmp[1]);
                return v;
            }
        });
       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
           @Override
           public boolean onMarkerClick(Marker marker) {
               marker.showInfoWindow();

               return false;
           }
       });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This methods create google map in fragment
     */
    private void createMapView() {
        try {
            if (null == mMap) {
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if (null == mMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     *This method create and add markers to google map
     *It retrieve quakeArrayList which is Arraylist of Earthquake and add marker depends on quakeArrayList
     * if position was passed to this intent from ListEarthQActivity, it create one marker from position of quakeArrayList
     */
    private void addMarker() {
        /** Make sure that the map has been initialised **/
        LatLng lt = new LatLng(58.722599, -103.798828);
        float focus = 1f;
        if (null != mMap) {
            ArrayList<EarthQuake> earthQuakes = (ArrayList<EarthQuake>) getIntent().getSerializableExtra("quakeArrayList");
            getIntent().removeExtra("quakeArrayList");
            int position = getIntent().getIntExtra("position", 0);
            if (earthQuakes != null) {
                    if(position==0) {
                        for (int i = 0; i < earthQuakes.size(); i++) {
                            mMap.addMarker(
                                    new MarkerOptions()
                                            .visible(true)
                                            .title("Earthquake")
                                            .snippet(earthQuakes.get(i).getDateTime() + "\n" + earthQuakes.get(i).getMagnitude() + "\n")
                                            .position(new LatLng(earthQuakes.get(i).getLat(), earthQuakes.get(i).getLon()))
                            );
                        }
                    }else if(position>0){
                        lt=new LatLng(earthQuakes.get(position).getLat(),earthQuakes.get(position).getLon());
                        mMap.addMarker(   new MarkerOptions()
                                .visible(true)
                                .title("Earthquake")
                                .snippet(earthQuakes.get(position).getDateTime() + "\n" + earthQuakes.get(position).getMagnitude() + "\n")
                                .position(new LatLng(earthQuakes.get(position).getLat(), earthQuakes.get(position).getLon())));
                        focus=4f;

                    }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lt, focus));

        }
    }

}
