package com.example.jordna.CitySoil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class Activity2 extends AppCompatActivity {
    static final String LOG_TAG = Activity2.class.getCanonicalName();
    private static final String CUSTOMER_SPECIFIC_IOT_ENDPOINT = "a18ls9ddgfsbwy-ats.iot.us-east-1.amazonaws.com";
    EditText txtTopic;
    TextView tvClientId;
    TextView tvStatus;

    Button btnlogserial;
    AWSIotMqttManager mqttManager;
    String clientId;
    String serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        txtTopic = findViewById(R.id.txtTopic);
        tvClientId = findViewById(R.id.tvClientId);
        tvStatus = findViewById(R.id.tvStatus);



        // MQTT client IDs are required to be unique per AWS IoT account.
        // This UUID is "practically unique" but does not _guarantee_
        // uniqueness.
        clientId = UUID.randomUUID().toString();

        // Initialize the credentials provider
        final CountDownLatch latch = new CountDownLatch(1);
        AWSMobileClient.getInstance().initialize(
                getApplicationContext(),
                new Callback<UserStateDetails>() {
                    @Override
                    public void onResult(UserStateDetails result) {
                        latch.countDown();
                    }

                    @Override
                    public void onError(Exception e) {
                        latch.countDown();
                        Log.e(LOG_TAG, "onError: ", e);
                    }
                }
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // MQTT Client
        mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_IOT_ENDPOINT);

        btnlogserial = (Button) findViewById(R.id.btnlogserial);
        btnlogserial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serial = txtTopic.getText().toString();
                    }
        });
    }
    public void connect(final View view) {
        Log.d(LOG_TAG, "clientId = " + clientId);

        try {
            mqttManager.connect(AWSMobileClient.getInstance(), new AWSIotMqttClientStatusCallback() {
                @Override
                public void onStatusChanged(final AWSIotMqttClientStatus status,
                                            final Throwable throwable) {
                    Log.d(LOG_TAG, "Status = " + String.valueOf(status));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText(status.toString());
                        }
                    });
                }
            });
        } catch (final Exception e) {
          //  Log.e(LOG_TAG, "Connection error.", e);
        }
    }

    public void tvClientIDshow(final View view) {
        tvClientId.setText(clientId);
    }

    public void publishSensor(final View view) {
        boolean on = ((Switch) view).isChecked();
        final String onmsg = "a";
        final String offmsg = "c";
        if(on)
        try {
            mqttManager.publishString(onmsg, serial, AWSIotMqttQos.QOS0);
        } catch (Exception e) {
            //  Log.e(LOG_TAG, "Publish error.", e);
        } else { try {mqttManager.publishString(offmsg, serial, AWSIotMqttQos.QOS0);
        } catch (Exception e) {
            //  Log.e(LOG_TAG, "Publish error.", e);
        }

        }
    }

    public void publishWatering(final View view) {
        boolean on = ((Switch) view).isChecked();
        final String onmsg = "b";
        final String offmsg = "d";
        if(on)
            try {
                mqttManager.publishString(onmsg, serial, AWSIotMqttQos.QOS0);
            } catch (Exception e) {
                //  Log.e(LOG_TAG, "Publish error.", e);
            } else { try {mqttManager.publishString(offmsg, serial, AWSIotMqttQos.QOS0);
        } catch (Exception e) {
            //  Log.e(LOG_TAG, "Publish error.", e);
        }

        }
    }

    public void disconnect(final View view) {
        try {
            mqttManager.disconnect();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Disconnect error.", e);
        }
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.screen1:
                Intent intent = new Intent(getApplicationContext(), Activity2.class);
                startActivity(intent);
                return true;
            case R.id.screen2:
                Intent intent_3 = new Intent(getApplicationContext(), Activity3.class);
                intent_3.putExtra("serialcode", serial);
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
