package org.mdss.magicapps.alertap;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dipan on 27-10-2018.
 */

public class RegisterRequest extends StringRequest {
    private final String keyS = "A123";

    private static final String REGISTER_REQUEST_URL = "http://mindwebs.org/mdss/api_key.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String email, String token, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("hash", token);
        //params.put("key", key);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
