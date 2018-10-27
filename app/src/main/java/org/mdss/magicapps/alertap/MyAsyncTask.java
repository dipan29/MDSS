package org.mdss.magicapps.alertap;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyAsyncTask extends AsyncTask<double[],Void,String > {

    //TextView wtime ;
    private TextView[] wdetails;
    public MyAsyncTask(TextView[] weather1){
        //wtime = weatherTime;
        for(int i =0; i<weather1.length; i++)
            wdetails[i] = weather1[i];
    }
//    @Override
//    protected String doInBackground(Void... voids) {
//        Random r =new Random();
//        int rand = r.nextInt(11);
//        int s = rand*200;
//        try {
//            Thread.sleep(s);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return "Awake at last after sleeping for"+s+"miliseconds";
//    }

    @Override
    protected String doInBackground(double[]... doubles) {
        return NetworkUtil.getBookInfo(doubles[0]);
    }

    @Override
    protected  void onPostExecute(String s){

        super.onPostExecute(s);
        try {
            String v[] = new String[6];
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject iaqi = data.getJSONObject("iaqi");
            JSONObject co = iaqi.getJSONObject("co");
            v[0] = co.getString("v");
            JSONObject no2 = iaqi.getJSONObject("no2");
            v[1] = no2.getString("v");
            JSONObject so2 = iaqi.getJSONObject("so2");
            v[2] = so2.getString("v");
            JSONObject pm10 = iaqi.getJSONObject("pm10");
            v[3] = pm10.getString("v");
            JSONObject pm25 = iaqi.getJSONObject("pm25");
            v[4] = pm25.getString("v");
            JSONObject o3 = iaqi.getJSONObject("o3");
            v[5] = o3.getString("v");
            for(int i =0; i<wdetails.length; i++)
                wdetails[i].setText(v[i]);
        }catch (Exception e){
            //wname.setText("No Result Found");
            wdetails[0].setText("No Result Found");
            e.printStackTrace();
        }


    }
}
