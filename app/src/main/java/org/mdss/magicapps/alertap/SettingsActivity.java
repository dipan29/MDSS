package org.mdss.magicapps.alertap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity {

    CheckBox co2, no2, so2, pm10, pm25, o3;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);
        co2 = findViewById(R.id.Co2);
        no2 = findViewById(R.id.No2);
        so2 = findViewById(R.id.So2);
        o3 = findViewById(R.id.o3);
        pm10 = findViewById(R.id.pm10);
        pm25 = findViewById(R.id.pm25);
    }

    public void save(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor = sharedPref.edit();
    }
}