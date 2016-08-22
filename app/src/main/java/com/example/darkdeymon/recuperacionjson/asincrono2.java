package com.example.darkdeymon.recuperacionjson;

import android.content.ClipData;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DARKDEYMON on 12/04/2016.
 */
public class asincrono2 extends AsyncTask<URL,Void,Boolean> {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    @Override
    protected Boolean doInBackground(URL... params) {
        try {

            HttpURLConnection con=(HttpURLConnection)params[0].openConnection();
            //con.setReadTimeout(10000);
            //con.setConnectTimeout(15000);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty( "Content-Type", "application/json" );
            con.setRequestMethod("POST");

            OutputStream os = con.getOutputStream();
            OutputStreamWriter o = new OutputStreamWriter(os, "UTF-8");

            JSONObject a= new JSONObject();
            a.put("nombre","helados");
            a.put("cantidad","20");

            o.write(a.toString());
            //o.flush();
            o.close();
            os.close();

            if(con.getResponseCode()==201)
            {

                BufferedReader read=new BufferedReader(new InputStreamReader(con.getInputStream()));
                String jsonText = readAll(read);
                Log.e("con",jsonText);

                item t = item.obtenerItem(jsonText);
                Log.e("con", String.valueOf(t.getId()));

                //Log.e("con",con.getContent().toString());
                con.disconnect();
                return true;
            }
            else
            {
                con.disconnect();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
