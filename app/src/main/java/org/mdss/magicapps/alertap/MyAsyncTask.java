package org.mdss.magicapps.alertap;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyAsyncTask extends AsyncTask<double[],Void,String > {

    //TextView wtime ;
    private TextView[] wdetails;
    public MyAsyncTask(TextView[] weather1){
        //wtime = weatherTime;
        wdetails = new TextView[7];
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
            String v[] = new String[7];
            JSONObject jsonObject = new JSONObject(s);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject iaqi = data.getJSONObject("iaqi");
            try {
                JSONObject co = iaqi.getJSONObject("co");
                v[0] = "CO : "+co.getString("v");
            }catch (Exception e){
                 //v[0] = "no value found";
                e.printStackTrace();
            }
            try {
                JSONObject no2 = iaqi.getJSONObject("no2");
                v[1] = "NO2 : "+no2.getString("v");
            }catch (Exception e){
                //v[1] = "no value found";
                e.printStackTrace();
            }
            try {
                JSONObject so2 = iaqi.getJSONObject("so2");
                v[2] = "SO2 : "+so2.getString("v");
            }catch (Exception e){
                //v[2] = "no value found";
                e.printStackTrace();
            }
            try{
                JSONObject pm10 = iaqi.getJSONObject("pm10");
                v[3] = "PM10 : "+pm10.getString("v");
            }catch (Exception e){
                //v[3] = "no value found";
                e.printStackTrace();
            }
            try {
                JSONObject pm25 = iaqi.getJSONObject("pm25");
                v[4] = "PM2.5 : "+pm25.getString("v");
            }catch (Exception e){
                //v[4] = "no value found";
                e.printStackTrace();
            }
            try {
                JSONObject o3 = iaqi.getJSONObject("o3");
                v[5] = "O3 : "+o3.getString("v");
            }catch(Exception e)
            {
                //v[5] = "no value found";
                e.printStackTrace();
            }
            try {
                JSONObject t = iaqi.getJSONObject("t");
                v[6] = "Temperature : "+ t.getString("v");
                JSONObject h = iaqi.getJSONObject("h");
                v[6] += "\t\tHumidity : "+ h.getString("v");
                JSONObject w = iaqi.getJSONObject("w");
                v[6] += "\nWind : "+ w.getString("v");
                JSONObject r = iaqi.getJSONObject("r");
                v[6] += "\t\tRain : "+ r.getString("v");
                JSONObject d = iaqi.getJSONObject("d");
                v[6] += "\n\tDew : "+ d.getString("v");
                JSONObject p = iaqi.getJSONObject("p");
                v[6] += "\t\tA P : "+ p.getString("v");

            }catch(Exception e)
            {
                //v[6] = "no value found";
                e.printStackTrace();
            }
            for(int i =0; i<wdetails.length; i++)
                wdetails[i].setText(v[i]);
        }catch (Exception e){
            //wname.setText("No Result Found");
            wdetails[0].setText("JSON problem");
            e.printStackTrace();
        }


    }
}
