package org.mdss.magicapps.alertap;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.OnSuccessListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private double location1[];
    private TextView[] polutant;
    private TextView currentLocation;
    private TextView currentLocationWeather;
    private FusedLocationProviderClient m;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "aa";
    private SharedPreferences sharedPreferences;
    private Location mlocation;
    private static final String myPreference = "homeAddressPreference";
    private static boolean FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        m = LocationServices.getFusedLocationProviderClient(this);
        location1 = new double[2];
        polutant = new TextView[6];
        polutant[0] = findViewById(R.id.co);
        polutant[1] = findViewById(R.id.no2);
        polutant[2] = findViewById(R.id.so2);
        polutant[3] = findViewById(R.id.pm10);
        polutant[4]= findViewById(R.id.pm25);
        polutant[5] = findViewById(R.id.o3);
        currentLocation = findViewById(R.id.currentLoc);
        //currentLocationWeather = findViewById(R.id.currentLocW);
        Log.d("aa", "inClick");
        currentLocation.setText("Loading Location...");
        getLocation();
        //toolbar.setBackgroundColor(Integer.parseInt("#0000"));
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Log.d("aa","**");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("aa", "//");

            m.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d("aa", "++");
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.d("aab", "in getLocationIf");
                        FLAG = true;
                        mlocation = location;
                        currentLocation.setText(getString(R.string.location_text, mlocation.getLatitude(), mlocation.getLongitude(), mlocation.getTime()));
                        //currentLocationTime.setText(getString(R.string.location_time , 235565666/*mlocation.getTime()*/));
                        location1[0] = location.getLatitude();
                        location1[1] = location.getLongitude();

                        Log.d("aa", location1[0] + ".." + location1[1]);
                        MyAsyncTask myAsyncTask = new MyAsyncTask(polutant);
                        myAsyncTask.execute(location1);
                    } else if (location == null)
                        Log.d("aa", "location null");
                    else
                        Log.d("aa", "location null00");
                }
            });
            Log.d(TAG, "getLocation: permissions granted" + FLAG);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("aa", "123");
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("aa", "--");
                    getLocation();
                } else {
                    Toast.makeText(this, "Location Permission Denied!!!", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
