package org.mdss.magicapps.alertap;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;

import java.util.List;

//import android.location.LocationListener;

/**
 * Created by dipan on 28-10-2018.
 */

public class SimpleWakefulService extends JobIntentService {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    List provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public static final int JOB_ID = 1;


    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, SimpleWakefulService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(Intent intent) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        provider = locationManager.getAllProviders();
        String prov = (String) provider.get(0);
        Intent int01 = new Intent(this, SimpleWakefulService.class);
        PendingIntent pendingIntent01 = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,0,pendingIntent01);
        Location location = locationManager.getLastKnownLocation(prov);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");  //getting token id
        String lat = Double.toString(location.getLatitude());
        String lng = Double.toString(location.getLongitude());

        Response.Listener<String> responseListener = null;

        GetRequest registerRequest = new GetRequest(lat,lng,token, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(registerRequest);

    }

}