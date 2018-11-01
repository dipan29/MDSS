package org.mdss.magicapps.alertap;

import android.content.Context;
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
        int ar[]= new int[6];
        for(int i =0; i<6 ;i++)
            ar[i] =0;
        if(co2.isChecked())
            ar[0] = 1;
        if(no2.isChecked())
            ar[1] = 1;
        if(so2.isChecked())
            ar[2] = 1;
        if(o3.isChecked())
            ar[3] = 1;
        if(pm10.isChecked())
            ar[4] = 1;
        if(pm25.isChecked())
            ar[5] = 1;
        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sharedPref = getSharedPreferences("polutantPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ss = sharedPref.edit();
        ss.putInt("co2",ar[0]);
        ss.apply();
        ss.putInt("so2",ar[2]);
        ss.apply();
        ss.putInt("no2",ar[1]);
        ss.apply();
        ss.putInt("o3",ar[3]);
        ss.apply();
        ss.putInt("pm10",ar[4]);
        ss.apply();
        ss.putInt("pm25",ar[5]);
        ss.apply();


    }
}