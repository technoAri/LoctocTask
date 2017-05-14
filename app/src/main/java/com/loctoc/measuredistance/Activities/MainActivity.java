package com.loctoc.measuredistance.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loctoc.measuredistance.OtherUtils.InternetConnection;
import com.loctoc.measuredistance.OtherUtils.Keys;
import com.loctoc.measuredistance.Parser.JsonHanldlerCoOrdinates;
import com.loctoc.measuredistance.Parser.JsonHanldlerKey;
import com.loctoc.measuredistance.R;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button getDistance;
    String tempKey = "oVQST5D6NT";
    ProgressDialog dialog;
    public static String permKey, latitude, longitude, latitude2, longitude2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDistance = (Button) findViewById(R.id.button_measure_distance);
        getDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (InternetConnection.checkConnection(getApplicationContext())) {
                    new GetDataTask().execute();
                    new GetDataTaskCoOrdinates1().execute();
                    new GetDataTaskCoOrdinates2().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Could not calculate distance. Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please Wait...");
            dialog.setMessage("Calculating distance");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObject = JsonHanldlerKey.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        if (permKey == null)
                            permKey = jsonObject.getString(Keys.KEY_PERM_KEY);
                        System.out.println(permKey);
                    }
                } else {

                }
            } catch (Exception je) {
                Log.i("TAG", "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class GetDataTaskCoOrdinates1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObject = JsonHanldlerCoOrdinates.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        latitude = jsonObject.getString(Keys.KEY_LATITUDE);
                        longitude = jsonObject.getString(Keys.KEY_LONGITUDE);
                        System.out.println(latitude + longitude + latitude2 + longitude2);
                    }
                } else {

                }
            } catch (Exception je) {
                Log.i("TAG", "" + je.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class GetDataTaskCoOrdinates2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JSONObject jsonObject = JsonHanldlerCoOrdinates.getDataFromWeb();

            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        latitude2 = jsonObject.getString(Keys.KEY_LATITUDE);
                        longitude2 = jsonObject.getString(Keys.KEY_LONGITUDE);
                        System.out.println(latitude + longitude + latitude2 + longitude2);
                    }
                } else {

                }
            } catch (Exception je) {
                Log.i("TAG", "" + je.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            calculateDistance();
        }

        private void calculateDistance() {
            final int R = 6371;

            if (latitude != null && longitude != null && latitude2 != null && longitude2 != null) {
                double lat1 = Double.valueOf(latitude);
                double lng1 = Double.valueOf(longitude);
                double lat2 = Double.valueOf(latitude2);
                double lng2 = Double.valueOf(longitude2);
                Double latDistance = toRad(lat2 - lat1);
                Double lonDistance = toRad(lng2 - lng1);
                Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                Double distance = R * c;
                Toast.makeText(getApplicationContext(), "Distance between the end poins is:" + distance + " KM", Toast.LENGTH_LONG).show();
            }
        }

        private Double toRad(double value) {
            return value * Math.PI / 180;
        }
    }
}

