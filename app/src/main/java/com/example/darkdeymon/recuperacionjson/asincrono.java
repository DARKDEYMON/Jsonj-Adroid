package com.example.darkdeymon.recuperacionjson;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import org.json.JSONArray;
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
import java.util.ArrayList;

/**
 * Created by DARKDEYMON on 11/04/2016.
 */
public class asincrono extends AsyncTask <URL,Void, JSONArray>{


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    @Override
    protected JSONArray doInBackground(URL... params) {

        URL url=params[0];
        try {
            HttpURLConnection res=(HttpURLConnection)url.openConnection();

            String userCredentials = "darkdeymon:nadaesrealasies";

            /* autenticacion*/
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(),Base64.NO_WRAP | Base64.URL_SAFE);
            Log.e("pass", basicAuth);
            res.setRequestProperty ("Authorization", basicAuth);

            /*
            res.setRequestMethod("GET");
            OutputStream os = res.getOutputStream();
            OutputStreamWriter o = new OutputStreamWriter(os, "UTF-8");

            JSONObject a= new JSONObject();
            a.put("password","nadaesrealasies");
            a.put("username","darkdeymon");
            o.write(a.toString());
            o.close();
            os.close();
            res.getResponseCode();
            */

            BufferedReader read=new BufferedReader(new InputStreamReader(res.getInputStream()));
            String jsonText = readAll(read);
            Log.e("jason", jsonText);
            JSONArray json = new JSONArray(jsonText);
            read.close();
            Log.e("jason", String.valueOf(json.length()));
            ArrayList<item> items= item.obtenerItemArray(jsonText);
            Log.e("gson",items.get(0).getNombre());
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // getting JSON string from URL
        Log.e("jason","error");
        return null;
    }
}
