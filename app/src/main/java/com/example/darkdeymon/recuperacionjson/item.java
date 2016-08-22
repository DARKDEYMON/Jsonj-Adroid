package com.example.darkdeymon.recuperacionjson;

import android.content.ClipData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by DARKDEYMON on 15/04/2016.
 */
public class item {
    int id;
    String nombre;
    float cantidad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public static ArrayList<item> obtenerItemArray(String json)
    {
        Gson gson= new Gson();
        Type tipe=new TypeToken<ArrayList<item>>(){}.getType();
        return gson.fromJson(json,tipe);
    }
    public static item obtenerItem(String json)
    {
        Gson gson=new Gson();
        return gson.fromJson(json,item.class);
    }
}
