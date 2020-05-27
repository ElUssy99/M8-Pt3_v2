package com.example.endevina;

import android.net.Uri;

public class Jugador implements Comparable<Jugador> {
    private String name;
    private int intentos;
    private Uri photoPath;

    public Jugador(String name, int intentos, Uri photoPath) {
        this.name = name;
        this.intentos = intentos;
        this.photoPath =  photoPath;
    }

    public String getName() {
        return name;
    }

    public int getIntentos() {
        return intentos;
    }

    public Uri getPhotoPath() { return photoPath; }

    @Override
    public String toString() {
        return "Jugador{" + "name='" + name + '\'' + ", Intentos=" + intentos + '}';
    }

    @Override
    public int compareTo(Jugador j){
        return this.intentos - j.intentos;
    }

}