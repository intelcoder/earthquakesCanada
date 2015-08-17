package com.example.fiddlestick.assignment2;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * This is customized cursor adapter for list view in ListEarthQActivity
 * Created by fiddlestick on 2015-08-08.
 */
//https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a-CursorAdapter
public class DoCursorAdapter extends CursorAdapter {
    final String RED = "#FF0000";
    final String GREEN = "#009933";
    final String ORANGE = "#E68A00";

    /**
     * This initialized required element with passed argument
     * @param context
     * @param c
     * @param flags
     */
    public DoCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.each_list, parent, false);
    }

    /**
     * This method set text for TextView from cursor
     * It changes text color on different magnitude
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView timeText = (TextView) view.findViewById(R.id.time);
        TextView magText = (TextView) view.findViewById(R.id.magnitude);
        TextView locText = (TextView) view.findViewById(R.id.location);

        String time = cursor.getString(cursor.getColumnIndex("time"));
        time = time.substring(0, 19).replace("T", " at ");
        timeText.setText(time);
        String mag = cursor.getString(cursor.getColumnIndex("magnitude"));
        magText.setText(mag);

        magText.setTextColor(returnColor(Double.parseDouble(mag)));
        locText.setText(cursor.getString(cursor.getColumnIndex("location")));
        cursor.moveToNext();

    }

    public int returnColor(double d) {

        if (d < 3) {
            //Green
            return Color.parseColor(GREEN);

        } else if (d < 5) {
            //Yellow
            return Color.parseColor(ORANGE);
        } else if (d < 10) {
            return Color.parseColor(RED);
        }
        return Color.parseColor("#000000");
    }
}
