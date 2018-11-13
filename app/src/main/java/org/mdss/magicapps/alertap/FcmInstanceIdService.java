package org.mdss.magicapps.alertap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;

/**
 * Created by Dipan on 27-10-2018.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {

/*
    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),recent_token);
        editor.commit();

    }
*/

}
