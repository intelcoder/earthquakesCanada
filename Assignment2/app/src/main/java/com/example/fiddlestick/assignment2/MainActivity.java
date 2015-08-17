package com.example.fiddlestick.assignment2;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class MainActivity extends ActionBarActivity implements View.OnClickListener, DbData {
    private TextView fromDatetxt;
    private TextView toDatetxt;
    private TextView magnitudeTxt;
    private Button btn;

    private Context context;
    private AlertDialog numPickDialog;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ProgressBar progressBar;
    private NumberPicker numPickerInt;
    private NumberPicker numPickerDecimal;
    private double getMagnitude;
    private Spinner orderSpinner, spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        findElement();
        setDateTimeField();
        setSpinner(); //Set spinner for
        btn.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        new MyTask(context).execute(JSON_DATA_ADDRESS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("CycleMain", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CycleMain", "onDestroy");

    }

    /**
     * This methods get reference of elements in a view and save it into private field
     * And it also register all the textview to onClickListener
     */
    private void findElement() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        fromDatetxt = (TextView) findViewById(R.id.fromText);
        fromDatetxt.setInputType(InputType.TYPE_NULL);
        toDatetxt = (TextView) findViewById(R.id.toText);
        toDatetxt.setInputType(InputType.TYPE_NULL);
        magnitudeTxt = (TextView) findViewById(R.id.magnitudeText);

        btn = (Button) findViewById(R.id.Filter);
        btn.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        setOnClick(); // set all the textview to onclickListener
    }

    /**
     * This method setups number pickers which is from dialog_num_picker layout and show those as AlertDialog
     *
     */
    private void setNumberPicker() {
        numPickDialog = new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.dialog_num_picker, null))
                .setTitle("Magnitude(0.0 to All)")
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMagnitude = 0;
                        getMagnitude = Double.parseDouble(String.valueOf(numPickerInt.getValue()));
                        getMagnitude += (Double.parseDouble(String.valueOf(numPickerDecimal.getValue() * 0.1)));
                        if (getMagnitude == 0.0) {
                            magnitudeTxt.setText("ALL");

                        } else {
                            magnitudeTxt.setText(String.format("%.1f", getMagnitude));
                        }
                        spinner.requestFocus();
                        spinner.setEnabled(true);
                        spinner.setActivated(true);
                        spinner.dispatchSetActivated(true);
                        spinner.getPrompt();
                        dialog.dismiss();
                    }

                }).show();
        numPickDialog.getWindow().
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        numPickerInt = (NumberPicker) numPickDialog.findViewById(R.id.numPickerInt);
        numPickerInt.setMinValue(0);
        numPickerInt.setMaxValue(10);
        String[] degreesValues = {"0.0", "0.1", "0.2", "0.3", " 0.4", "0.5", "0.6", "0.7", "0.8", "0.9"};
        numPickerDecimal = (NumberPicker) numPickDialog.findViewById(R.id.numPickerDecimal);
        numPickerDecimal.setDisplayedValues(degreesValues);
        numPickerDecimal.setMinValue(0);
        numPickerDecimal.setMaxValue(degreesValues.length - 1);
        numPickerDecimal.setWrapSelectorWheel(true);

    }

    /**
     * This method setup data pickers.
     * FromDataPicker's default value is current date - 30 days
     * ToDatePicker's default value is current date
     * Date pickers' max and minimum are between current date and current date - 30 days
     * //http://stackoverflow.com/questions/16749361/how-set-maximum-date-in-datepicker-dialog-in-android
     * //http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
     */
    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance(); //Calendar obtain current date by getInstance
        fromDatetxt.setText(dateFormatter.format(getMinimumDate()));
        toDatetxt.setText(dateFormatter.format(getMaxDate()));//d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDatetxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        Date d = new Date(getMinimumDate());
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        fromDatePickerDialog.getDatePicker().updateDate(c.YEAR, c.MONTH, c.DAY_OF_MONTH);
        fromDatePickerDialog.getDatePicker().setMinDate(getMinimumDate());
        fromDatePickerDialog.getDatePicker().setMaxDate(getMaxDate());
        // fromDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDatetxt.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog.getDatePicker().setMaxDate(getMaxDate());
        toDatePickerDialog.getDatePicker().setMinDate(getMinimumDate());

    }

    /**
     * This method register onclick to TextViews
     */
    public void setOnClick() {

        magnitudeTxt.setOnClickListener(this);
        fromDatetxt.setOnClickListener(this);
        toDatetxt.setOnClickListener(this);
    }

    /**
     * This methods return minimum date for date picker
     *
     * @return It returns current date - 30 days as milliseconds
     */

    public long getMinimumDate() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DATE, -30);
        return min.getTimeInMillis();
    }

    /**
     * This methods return maximum date for date picker
     * @return It returns current date as milliseconds
     */
    public long getMaxDate() {
        Calendar min = Calendar.getInstance();
        return min.getTimeInMillis();
    }

    /**
     * This method set up two spinner which are location and order by
     */
    private void setSpinner() {
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.province, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner = (Spinner) findViewById(R.id.SPNer);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        orderSpinner = (Spinner) findViewById(R.id.main_orderBy_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.order_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(adapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                orderSpinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /**
     * This method handle onClick event for fromDatetxt, magnitudeTxt, and btn element
     * btn = filter button  which get all the text from filter screen and create OrderByObject out of it
     * Then,it pass OrderByObject to ListEarthQActivity for listview
     * If there is no matched result with provided information, it pops up alert dialog
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view == fromDatetxt) {
            fromDatePickerDialog.show();
        } else if (view == toDatetxt) {

            toDatePickerDialog.show();
        } else if (view == magnitudeTxt) {
            setNumberPicker();
        } else if (view == btn) { // On filter button click
            EditText mgText = (EditText) findViewById(R.id.magnitudeText);
            TextView fromText = (TextView) findViewById(R.id.fromText);
            TextView toText = (TextView) findViewById(R.id.toText);
            // double mag =Double.parseDouble((String) mgText.getText());
            String from = String.valueOf(fromText.getText());
            String to = String.valueOf(toText.getText());
            String temp = mgText.toString();
            // double magnitude = );
            String prov = (String) spinner.getSelectedItem();
            // OrderByObject obo = new OrderByObject(Double.parseDouble(mgText.getText()),Integer.parseInt(fromText.getText()));
            String order = (String) orderSpinner.getSelectedItem();
            OrderByObject obo = new OrderByObject(getMagnitude, from, to, prov, order);
            DbOpenHelper dbOpenHelper = new DbOpenHelper(this, DB_NAME, null, 1);
            if (dbOpenHelper.getFiltered(obo).getCount() > 0) {
                Intent tmp = new Intent(this, ListEarthQActivity.class);
                tmp.putExtra("obo", obo);
                startActivity(tmp);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Alert")
                        .setMessage("No matched result found\nPlease provides new filter info")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
            dbOpenHelper.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * this method handles on select event on action bar which contain one refresh button
     * On refresh button select, it execute AsyncTask(MyTask).
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                btn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new MyTask(context).execute(JSON_DATA_ADDRESS);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This class retrieve json string from website and pass the string to JsonToDb class to save
     * all the information into database
     */
    private class MyTask extends AsyncTask<String, String, String> {
        Context context;

        public MyTask(Context con) {
            context = con;
        }

        /**
         * This method takes use first argument that contain URL of the json file.
         * It read all the string with bufferedreader from web and pass to JsonToDb object
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String tmp = "";
                while ((tmp = br.readLine()) != null) {
                    sb.append(tmp);
                }
                br.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonToDb jp = new JsonToDb(sb.toString(), context);
            int newrow = jp.getUpdateResult();


            return newrow + "";
        }

        /**
         * This method changes visibility of button and progress bar.
         * This method also shows user that task has been done successfully
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            int tmp = Integer.parseInt(s);
            String toastMsg = "earthquake data is up-to-date\n";
            if (tmp > 0) toastMsg += tmp + " number of new earthquake updated";
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();

        }
    }


}

