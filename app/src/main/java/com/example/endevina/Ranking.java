package com.example.endevina;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking extends Activity{

    private ArrayAdapter<Jugador> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        adapter = new ArrayAdapter<Jugador>(this, R.layout.record, MainActivity.jugadores){
            @Override
            public View getView(int pos, View convertView, ViewGroup container)
            {
                if( convertView==null ) {
                    convertView = getLayoutInflater().inflate(R.layout.player, container, false);
                }
                ((TextView) convertView.findViewById(R.id.name)).setText(getItem(pos).getName());
                ((TextView) convertView.findViewById(R.id.trie)).setText(Integer.toString(getItem(pos).getIntentos()));
                ((ImageView) convertView.findViewById(R.id.imgView)).setImageURI(getItem(pos).getPhotoPath());
                return convertView;
            }
        };

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
    }
}