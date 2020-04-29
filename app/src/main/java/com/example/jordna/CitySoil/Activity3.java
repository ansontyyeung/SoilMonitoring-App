package com.example.jordna.CitySoil;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Activity3 extends AppCompatActivity {
    private TextView showsoilTemp;
    private TextView showsoilHumd;
    private TextView showsoilPh;
    private TextView showenvTemp;
    private TextView showenvHumd;
    private TextView showenvLight;
    private RequestQueue mQueue;


    //TextView textView9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        showsoilTemp = findViewById(R.id.textView17);
        showsoilHumd = findViewById(R.id.textView16);
        showsoilPh = findViewById(R.id.textView15);
        showenvTemp = findViewById(R.id.textView26);
        showenvHumd = findViewById(R.id.textView25);
        showenvLight = findViewById(R.id.textView24);

        mQueue = Volley.newRequestQueue(this);

        jsonParse();
    }

    private void jsonParse() {
        String serial = getIntent().getStringExtra("serialcode");
        String url = "https://9wmsrqjtpe.execute-api.us-east-1.amazonaws.com/dev/latest?id=" + serial;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("states");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject states = jsonArray.getJSONObject(i);

                                Double soilTemp = states.getDouble("soilTemp");
                                Double soilHumd = states.getDouble("soilHumd");
                                Double soilPh = states.getDouble("soilPh");
                                Double envTemp = states.getDouble("envTemp");
                                Double envHumd = states.getDouble("envHumd");
                                String envLight = states.getString("envLight");
                                // long createdAt = states.getLong("createdAt");

                                showsoilTemp.append(soilTemp + "°C" + "\n");
                                showsoilHumd.append(soilHumd + "%" + "\n");
                                showsoilPh.append(soilPh + "\n");
                                showenvTemp.append(envTemp + "°C" + "\n");
                                showenvHumd.append(envHumd + "%" + "\n");
                                showenvLight.append(envLight + "\n");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
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
                startActivity(intent_3);
                return true;
            case R.id.screen3:
                Intent intent_4 = new Intent(getApplicationContext(), Activity4.class);
                intent_4.putExtra("serialcode", serial);
                startActivity(intent_4);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

}


