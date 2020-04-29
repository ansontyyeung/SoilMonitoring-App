package com.example.jordna.CitySoil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity4 extends AppCompatActivity {
    private String TAG = Activity4.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    ArrayList<HashMap<String, String>> histJsonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        histJsonList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        new GetHistory().execute();
    }


    private class GetHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(Activity4.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            // JSON data url
            String serial = getIntent().getStringExtra("serialcode");
            String Jsonurl = "https://9wmsrqjtpe.execute-api.us-east-1.amazonaws.com/dev/listStates?id=" + serial;

            HttpHandler httpHandler = new HttpHandler();

            // request to json data url and getting response
            String jsonString = httpHandler.makeServiceCall(Jsonurl);
            Log.e(TAG, "Response from url: " + jsonString);
            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    // Getting JSON Array node
                    JSONArray history = jsonObject.getJSONArray("states");

                    for (int i = 0; i < history.length(); i++) {
                        JSONObject states = history.getJSONObject(i);
                        String soilTemp = states.getString("soilTemp");
                        String soilHumd = states.getString("soilHumd");
                        String soilPh = states.getString("soilPh");
                        String envTemp = states.getString("envTemp");
                        String envHumd = states.getString("envHumd");
                        String envLight = states.getString("envLight");
                        String date = states.getString("date");
                        String time = states.getString("time");

                        // tmp hash map for single hist
                        HashMap<String, String> hist = new HashMap<>();

                        // adding each child node to HashMap key => value
                        hist.put("datentime", date + " " + time);
                        hist.put("soildata", "Temp: " + soilTemp + "°C " + "Humd: " + soilHumd + "% " + "pH: " + soilPh);
                        hist.put("envdata", "Temp: " + envTemp + "°C " + "Humd: " + envHumd + "% " + "Light: " + envLight);

                        // adding hist to hist list
                        histJsonList.add(hist);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Could not get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Could not get json from server.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    Activity4.this, histJsonList,
                    R.layout.list_item, new String[]{"datentime", "soildata",
                    "envdata"}, new int[]{R.id.datentime,
                    R.id.soildata, R.id.envdata});

            listView.setAdapter(adapter);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String serial = getIntent().getStringExtra("serialcode");
        switch (item.getItemId()) {
            case R.id.screen1:
                Intent intent = new Intent(getApplicationContext(), Activity2.class);
                intent.putExtra("serialcode", serial);
                startActivity(intent);
                return true;
            case R.id.screen2:
                Intent intent_3 = new Intent(getApplicationContext(), Activity3.class);
                intent_3.putExtra("serialcode", serial);
                startActivity(intent_3);
                return true;
            case R.id.screen3:
                Intent intent_4 = new Intent(getApplicationContext(), Activity4.class);
                startActivity(intent_4);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
