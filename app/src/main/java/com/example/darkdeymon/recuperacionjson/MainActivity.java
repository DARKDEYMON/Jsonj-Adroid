package com.example.darkdeymon.recuperacionjson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private int PICK_IMAGE =0;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
                */


               // Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
              //  startActivityForResult(pickPhoto , 1);
                /*

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                */
          //  }
       // });

        asincrono a=new asincrono();
        JSONArray res;
        try {
            a.execute(new URL("http://192.168.1.3:8000/mainapp/res/items/?format=json"));
            res=a.get();
            Log.e("jason recuperado", String.valueOf(a.get()));
            for (int i=0;i<res.length();i++)
            {
                JSONObject res1 = res.getJSONObject(i);

                LinearLayout l=(LinearLayout)findViewById(R.id.content);
                TextView t=(TextView) getLayoutInflater().inflate(R.layout.message_text,l,false);
                t.setText(res1.get("id")+" "+res1.getString("nombre")+" "+res1.getString("cantidad"));
                l.addView(t);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Log.e("demos",storageDir.getAbsolutePath());
    }
    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj","open");

        }
    }
    private File photo;
    private Uri picUri=null;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photo= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),timeStamp+".jpg");
                picUri= Uri.fromFile(photo);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,picUri);
                takePicture.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(takePicture, 0);
                //dispatchTakePictureIntent();
                Log.d("Raj", "Fab 2");
                break;
        }
    }
    /*
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_TAKE_PHOTO = 0;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            //Log.e("paso","no paso");
            File photoFile = null;
            try {
                photoFile = createImageFile();
                picUri = Uri.fromFile(photoFile);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("paso","no paso");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    /*
    /*
    public void menu()
    {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getDrawable(R.drawable.ic_add_black_24dp));
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);// repeat many times:

        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageDrawable(getDrawable(R.drawable.ic_add_a_photo_black_24dp));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getDrawable(R.drawable.ic_camera_alt_black_24dp));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this).addSubActionView(button1).addSubActionView(button2).attachTo(actionButton).build();

    }*/
    /*
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){

                    if(photo.exists()) {

                        if(picUri!=null)
                        {
                            //Uri selectedImage = data.getData();
                            ImageView i=(ImageView)findViewById(R.id.imagen);
                            i.setImageURI(picUri);
                            Log.e("apli0", picUri.getPath());
                            AsincronoEditado a = new AsincronoEditado(this,photo);
                            a.execute();
                        }
                        else
                        {
                            Log.e("apli0", "no paso");
                        }
                        /*
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        Uri selectedImage = getImageUri(getApplicationContext(), photo);
                        Log.e("entro", MediaStore.EXTRA_OUTPUT);

                        ImageView i=(ImageView)findViewById(R.id.imagen);
                        i.setImageURI(selectedImage);
                        AsincronoEditado a = new AsincronoEditado(this);
                        a.execute(picUri);
                        */

                        //Log.d("apli0", selectedImage.getPath());
                        //imageview.setImageURI(selectedImage);
                    }else{
                        Log.e("apli0", "data bacio");
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();

                        /*
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        Log.e("imagen",encoded);
                        */

                        AsincronoEditado a=new AsincronoEditado(this,selectedImage);
                        a.execute();


                    Log.d("apli1",selectedImage.toString());
                    //imageview.setImageURI(selectedImage);
                }
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try {

                asincrono2 b= new asincrono2();
                b.execute(new URL("http://192.168.1.3:8000/mainapp/res/items/"));

                if(b.get())
                {

                    Toast.makeText(this,"correcto",Toast.LENGTH_LONG);
                    //Snackbar.make(,"se inserto correctamente",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                    Log.e("result", String.valueOf(b.get()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();

                }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
