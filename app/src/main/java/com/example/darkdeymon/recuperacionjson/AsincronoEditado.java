package com.example.darkdeymon.recuperacionjson;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by DARKDEYMON on 26/04/2016.
 */
public class AsincronoEditado extends AsyncTask <Void,Void,Boolean>{

    private AppCompatActivity view;
    private File file;
    private Uri uri;
    public AsincronoEditado(AppCompatActivity j,File f)
    {
        file=f;
        view=j;
    }
    public AsincronoEditado(AppCompatActivity j,Uri u)
    {
        uri=u;
        view=j;
    }
    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            MultipartUtility m= new MultipartUtility("http://192.168.1.3:8000/mainapp/res/fotosrest/","UTF-8");


            //File f= new File(getRealPathFromURI(params[0]));

            if(file==null)
            {
                file= new File(getRealPathFromURI(uri));
            }


            Log.e("nombre",file.getAbsolutePath());
            m.addFilePart("imagen",file);
            Log.e("paso",m.finish().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
}
