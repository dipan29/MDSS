package org.mdss.magicapps.alertap;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dipan on 28-10-2018.
 */

public class GetRequest extends StringRequest {
    private final String keyS = "A123";

    private static final String GET_REQUEST_URL = "http://mindwebs.org/mdss/aquicn_api.php";
    private Map<String, String> params;

    public GetRequest(String lat, String lng, String token, Response.Listener<String> listener) {
        super(Method.POST, GET_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("hash", token);
        //params.put("key", key);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
