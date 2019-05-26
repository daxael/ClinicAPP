package com.example.clinicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Clase en la que se cargan los mensajes y sus datos correspondientes.
//También da la opción a responder mensajes y crear mensajes.
public class Mensajeria extends AppCompatActivity {
    ArrayList<DataModelMensajes> dataModels;
    ListView listView;
    AlertDialog.Builder dialog;

    private static CustomAdapter adapter;
    private List listaID = new ArrayList();
    private BDClinica conexion;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        listView = (ListView) findViewById(R.id.list);

        rellenarListView(); //Rellena la listview de mensajes del usuario de la BBDD

        //Da la opción de seleccionar mensajes, mirar su información, y responder.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                dialog = new AlertDialog.Builder(Mensajeria.this);

                dialog.setTitle("Contenido del Mensaje");
                dialog.setMessage(obtenerContenido(Integer.parseInt(listaID.get(position).toString())));

                dialog.setNeutralButton("Responder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responderMensaje(Integer.parseInt(listaID.get(position).toString()));
                    }
                });


                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onRestart() {
        rellenarListView();
        super.onRestart();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
        }

        return super.onOptionsItemSelected(item);
    }
    */

    public String obtenerContenido(int id) { //Devuelve un string con el contenido del mensaje
        String mensaje = "";

        String query = "select * from mensaje where mensajeid = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mensaje = "Origen: " + usuarioOrigen(cursor.getInt(5));
                mensaje = mensaje + "\nMensaje:" + cursor.getString(2);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return mensaje;
    }

    public String obtenerTitulo(int id) { //Devuelve únicamente el título del mensaje
        String titulo = "";

        String query = "select * from mensaje where mensajeid = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                titulo = cursor.getString(1);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return titulo;
    }

    public int obtenerOrigenID(int id) { //Devuelve la ID de aquel que envía el mensaje
        int nDestinatarioID = 0;

        String query = "select * from mensaje where mensajeid = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                nDestinatarioID = cursor.getInt(6);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return nDestinatarioID;
    }


    public String usuarioOrigen(int nID) { //Devuelve el nombre del usuario que responde al mensaje
        String usuario = "";

        String query = "select * from usuario where usuarioid = " + nID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                usuario = cursor.getString(1);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return usuario;
    }

    public void rellenarListView() {
        VarGlobales global = new VarGlobales();

        String query = "select * from mensaje where destinatarioID = " + global.getUSuario();
        Cursor cursor = db.rawQuery(query, null);

        dataModels = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                dataModels.add(new DataModelMensajes(cursor.getString(1), cursor.getString(2)));
                listaID.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter = new CustomAdapter(dataModels, getApplicationContext());
        listView.setAdapter(adapter);
    }

    //Método que se encarga de responder el mensaje, con datos como la ID del origen, el mensaje que quieres enviar, y el título de respuesta
    public void responderMensaje(final int nID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Responder Mensaje");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (comprobarSiRespondido(nID) == false) {
                    responder("Re:" + obtenerTitulo(nID), input.getText().toString(), obtenerOrigenID(nID));
                    actualizarMensaje(nID);
                } else {
                    Toast.makeText(Mensajeria.this, "Este mensaje ya ha sido respondido.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Comprueba si un mensaje está respondido. Si lo está, no da la opción a responder
    public boolean comprobarSiRespondido(int nID) {
        Cursor d = db.rawQuery("Select * from mensaje where mensajeID = " + nID, null);
        d.moveToFirst();
        if (d.getCount() > 0) {
            if (d.getInt(3) == 1) {
                return true;
            }
        }
        return false;
    }

    public void responder(String titulo, String contenido, int destinatario) {
        VarGlobales global = new VarGlobales();
        String query = "INSERT INTO Mensaje(Titulo,Contenido,Fecha,OrigenID,DestinatarioID) VALUES ('" + titulo + "','" + contenido + "'," + global.devolverFechaActual() + "," + global.getUSuario() + "," + destinatario + ")";
        db.execSQL(query);
        Toast.makeText(this, "Mensaje enviado.", Toast.LENGTH_SHORT).show();
    }

    public void actualizarMensaje(int nID) {
        String query = "UPDATE Mensaje SET Respondido = 1 WHERE MensajeID = " + nID;
        db.execSQL(query);
    }

    public void enviarMensaje(View v) {
        Intent intent = new Intent(getApplication(), EnviarMensaje.class);
        startActivity(intent);
    }

    public void volver(View v) {
        db.close();
        finish();
    }
}
