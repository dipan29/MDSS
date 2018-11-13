package org.mdss.magicapps.alertap;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

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
    private FloatingActionButton floatingActionButton;
    private GoogleMap mMap;
    AutoCompleteTextView searchTxt;
    PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    GoogleApiClient mGoogleApiClient;
    LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    TextView t1;
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        final LatLng s = new LatLng(22.5726, 88.3639);
        mMap.addMarker(new MarkerOptions().position(s).title("Made with <3 in India"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(s));
        init();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //t1.setText("lat="+latLng.latitude+"lon="+latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("lat="+latLng.latitude+"lon="+latLng.longitude));
                double loc[] = new double[2];
                loc[0] = latLng.latitude;
                loc[1] = latLng.longitude;

                MyAsyncTask  myAsyncTask = new MyAsyncTask(polutant);
                myAsyncTask.execute(loc);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.addMarker(new MarkerOptions().position(s).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(s));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location1[0], location1[1]), 12.0f));
                MyAsyncTask  myAsyncTask = new MyAsyncTask(polutant);
                myAsyncTask.execute(location1);
            }
        });

    }
    private void init(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        searchTxt.setAdapter(mPlaceAutocompleteAdapter);
        Log.d("aa", "init: initializing");

        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    ImageView img = (ImageView) findViewById(R.id.magnify);
                    img.setImageResource(R.drawable.ic_clear_black_24dp);
                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
    }
    private void geoLocate(){
        Log.d("aa", "geoLocate: geolocating");

        String searchString = searchTxt.getText().toString();

        Geocoder geocoder = new Geocoder(HomeActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("aa", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            final LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pos).title(address.getFeatureName()));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 12.0f));
            double loc[] = new double[2];
            loc[0] = pos.latitude;
            loc[1] = pos.longitude;
            MyAsyncTask  myAsyncTask = new MyAsyncTask(polutant);
            myAsyncTask.execute(loc);
            Log.d("aa", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    public double[] getCoord(String des) {
        Geocoder gc = new Geocoder(this);
        double latlng[] = new double[2];
        if (gc.isPresent()) {
            List<Address> list = null;
            try {

                list = gc.getFromLocationName(des, 1);
                Address address = list.get(0);
                latlng[0] = address.getLatitude();
                latlng[1] = address.getLongitude();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("aa", e.getMessage());
            }
        }
        return  latlng;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        m = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        floatingActionButton = findViewById(R.id.myLoaction);

        //SECTION FOR ALARM - CHANGED AT 15:15 28-10-2018
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(HomeActivity.this, YourWakefulReceiver.class);
        boolean flag = (PendingIntent.getBroadcast(HomeActivity.this, 0,
                intent, PendingIntent.FLAG_NO_CREATE)==null);
        /*Register alarm if not registered already*/
        if(flag){
            PendingIntent alarmIntent = PendingIntent.getBroadcast(HomeActivity.this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Create Calendar obj called calendar
            Calendar calendar = Calendar.getInstance();

            /* Setting alarm for every one hour from the current time.*/
            int m = 1; //total minutes
            int intervalTimeMillis = 1000 * 60 * 180; // m min
            if (alarmMgr != null) {
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), intervalTimeMillis,
                        alarmIntent);
            }
        }
        //END OF ALARM BROADCAST
        searchTxt = findViewById(R.id.search_bar);
        location1 = new double[2];
        polutant = new TextView[8];
        polutant[0] = findViewById(R.id.co);
        polutant[1] = findViewById(R.id.no2);
        polutant[2] = findViewById(R.id.so2);
        polutant[3] = findViewById(R.id.pm10);
        polutant[4]= findViewById(R.id.pm25);
        polutant[5] = findViewById(R.id.o3);
        polutant[6] = findViewById(R.id.wh);
        polutant[7] = findViewById(R.id.aqi);
        setPreference();



        Log.d("aa", "inClick");

        final LatLng pos = new LatLng(location1[0], location1[1]);

        MyAsyncTask myAsyncTask = new MyAsyncTask(polutant);
        myAsyncTask.execute(location1);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navHeaderView = (NavigationView) findViewById(R.id.nav_view);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String UserName = sharedPref.getString("user_name", "No name saved");
        String UserEmail = sharedPref.getString("user_email", "No email saved");
        navigationView.setNavigationItemSelectedListener(this);
        try {
            TextView navName = (TextView) findViewById(R.id.txtNavName);

            navName.setText(UserName + "");
            Log.d("bb",UserName);

            TextView navEmail = (TextView) findViewById(R.id.txtNavEmail);

            navEmail.setText(UserEmail + "");
            Log.d("bb",UserEmail);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
            Intent newIn = new Intent(this, SettingsActivity.class);
            startActivity(newIn);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(this,AboutActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {

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
                        //currentLocation.setText(getString(R.string.location_text, mlocation.getLatitude(), mlocation.getLongitude(), mlocation.getTime()));
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
    public void clear(View v)
    {
        searchTxt.setText("");
        ImageView img = (ImageView) findViewById(R.id.magnify);
        img.setImageResource(R.drawable.ic_magnify);
    }
    public void setPreference()
    {
        SharedPreferences shared = getSharedPreferences("polutantPref", Context.MODE_PRIVATE);
        if(shared.getInt("co2",0) == 1)
            polutant[0].setVisibility(View.VISIBLE);
        if(shared.getInt("so2",0) == 1)
            polutant[1].setVisibility(View.VISIBLE);
        if(shared.getInt("no2",0) == 1)
            polutant[2].setVisibility(View.VISIBLE);
        if(shared.getInt("pm10",0) == 1)
            polutant[3].setVisibility(View.VISIBLE);
        if(shared.getInt("pm25",0) == 1)
            polutant[4].setVisibility(View.VISIBLE);
        if(shared.getInt("o3",0) == 1)
            polutant[5].setVisibility(View.VISIBLE);
    }
}
