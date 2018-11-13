package org.mdss.magicapps.alertap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dipan on 28-10-2018.
 */

public class YourWakefulReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, SimpleWakefulService.class);
        SimpleWakefulService.enqueueWork(context,service);
    }
}
