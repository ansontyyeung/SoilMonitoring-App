package com.example.jordna.CitySoil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void login(View view)
    {
        Intent intent = new Intent(getApplicationContext(), Activity2.class);
        startActivity(intent);
    }

}
