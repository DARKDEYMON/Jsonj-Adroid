package com.example.darkdeymon.recuperacionjson;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * Created by DARKDEYMON on 25/04/2016.
 */
public class asincrono3 extends AsyncTask <Uri,Void,Boolean> {

    private String boundary= "*****"+Long.toString(System.currentTimeMillis())+"*****"; //UUID.randomUUID().toString();
    private static final String LINE_FEED = "\r\n";
    private AppCompatActivity view;
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public asincrono3(AppCompatActivity j){
        view=j;
    }
    @Override
    protected Boolean doInBackground(Uri... params) {
        try {
            URL url=new URL("http://192.168.1.3:8000/mainapp/res/fotosrest/");
            HttpURLConnection con =(HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            OutputStream os = con.getOutputStream();
            OutputStreamWriter o = new OutputStreamWriter(os, "UTF-8");
            /* pruebas apartir de aca */

            //DataOutputStream request = new DataOutputStream(con.getOutputStream());


            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"),true);
            Log.e("nombre",params[0].getPath());
            Log.e("nombre real",getRealPathFromURI(params[0]));

            File f=new File(getRealPathFromURI(params[0]));



            String fileName = f.getName();
            String fieldName ="imagen";
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            Log.e("tipo",URLConnection.guessContentTypeFromName(fileName));

            FileInputStream fo= new FileInputStream(f);
            Log.e("nombre de archibo",f.getName());
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = fo.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            //o.write(fo.toString());
            //outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);

            /*
            JSONObject j= new JSONObject();
            String hola="";
            j.put("imagen", f.getName());
            */
            os.flush();
            fo.close();
            writer.append(LINE_FEED);
            writer.flush();
            //o.write("imagen=@"+hola+";type=image/jpg");

            //o.write(j.toString());
            //o.flush();
            writer.close();
            o.close();
            os.close();
            if(con.getResponseCode()==201)
            {
                Log.e("salio", String.valueOf(con.getResponseCode()));

                con.disconnect();
                return true;
            }
            else
            {
                BufferedReader read=new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String jsonText = readAll(read);
                Log.e("con",jsonText);
                con.disconnect();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //return null;
    }
    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = view.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    public String getImagePath(Uri uri){
        Cursor cursor = view.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = view.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
