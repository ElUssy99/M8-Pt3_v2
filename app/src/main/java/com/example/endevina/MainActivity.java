package com.example.endevina;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Jugador> jugadores = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Dialog dialogo = null;

    private String name;
    private int intentos = 0;
    private int rango;

    File dir = new File("data"+File.separator+"data"+File.separator+"com.example.endavina"+File.separator+"photos");
    File imagePlayer;
    private boolean havePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dir.mkdir();

        encender();
    }

    private void encender() {
        havePhoto = false;

        descubrirNombre();
        final Button button = findViewById(R.id.button);
        final Button botonRecord = findViewById(R.id.button2);
        rango = numAleatorio();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adivinaNum();
            }
        });

        botonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tablaDeRecords();
            }
        });

    }

    private int numAleatorio() {
        int randomNum = (int) (Math.random() * 100 + 1);
        return randomNum;
    }

    public void tablaDeRecords() {
        Intent i = new Intent(this, Ranking.class);
        startActivity(i);
    }

    public void adivinaNum() {
        final EditText editText = findViewById(R.id.editText);
        final TextView textView = findViewById(R.id.textView);
        String st = String.valueOf(editText.getText());
        int numero = Integer.parseInt(st);
        if (numero > rango) {
            textView.setText(textView.getText() + "Has puesto el numero " + editText.getText().toString() + "\n");
            intentos++;
            Context context = getApplicationContext();
            CharSequence text = "El numero es menor a " + numero;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (numero < rango) {
            textView.setText(textView.getText() + "Has puesto el numero " + editText.getText().toString() + "\n");
            Context context = getApplicationContext();
            CharSequence text = "El numero es mayor a " + numero;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            intentos++;
        } else if (numero == rango) {
            textView.setText(textView.getText() + "Has puesto el numero " + editText.getText().toString() + "\n");
            jugadores.add(new Jugador(name,intentos, Uri.fromFile(imagePlayer)));
            crearFichero();
            Context context = getApplicationContext();
            CharSequence text = "¡Felicidades, lo has adivinado!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            rango = numAleatorio();
            descubrirNombre();
            textView.setText("");
            editText.setText("");
        }
    }

    // Pedir nombre y foto (ACTUALIZADO)
    private String descubrirNombre() {
        dialogo = new Dialog(MainActivity.this);
        dialogo.setContentView(R.layout.dialog);
        dialogo.setTitle("Datos de Usuario");
        dialogo.show();

        Button camera = dialogo.findViewById(R.id.botonFoto);
        Button register = dialogo.findViewById(R.id.botonDialog);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText textName = dialogo.findViewById(R.id.etNombre);
                name = textName.getText().toString();
                if(name.isEmpty() || !havePhoto){
                    Toast toast = Toast.makeText(getApplicationContext(), "No puedes dejar un campo vacio", Toast.LENGTH_LONG);
                    toast.show();
                }else{
                    Collections.sort(jugadores);
                    for (Jugador trie : jugadores) {
                        System.out.println("INFO -> " + trie.toString());
                    }
                    Jugador tryCurrentPlayer = new Jugador(name, intentos, Uri.fromFile(imagePlayer));
                    jugadores.add(tryCurrentPlayer);
                    crearFichero();
                    dialogo.dismiss();
                }
            }
        });
        return name;
    }

    private void crearFichero() {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("almacenarJugadors.txt",Context.MODE_APPEND));
            for (int i = 0; i < jugadores.size(); i++) {
                osw.write(jugadores.get(i).getName() + "," + jugadores.get(i).getIntentos() + "," + jugadores.get(i).getPhotoPath().toString());
                osw.append("\r\n");
            }
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            havePhoto = true;

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView iv = dialogo.findViewById(R.id.imageView);
            iv.setImageBitmap(imageBitmap);

            OutputStream os;
            try {
                imagePlayer = new File(dir, (jugadores.size()+1)+".png");
                os = new FileOutputStream(imagePlayer);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            } catch(IOException e) {
                System.out.println("[ERROR] - No se pudo guardar la imagen");
            }
        }
    }
}


