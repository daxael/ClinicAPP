package com.example.clinicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Clase que simula la creación de una cita por parte de un usuario. No posee restricciones realistas (por ejemplo,
// seleccionar sólo unas fechas de disponibilidad)

public class Cita extends AppCompatActivity {
    private ArrayAdapter<String> arraycitas;
    private ListView citas;
    private BDClinica conexion;
    private SQLiteDatabase db;
    private List listaID = new ArrayList();

    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.cita);

        //Primero cargo la base de datos al abrir la actividad, y luego relleno de datos (citas) el listview
        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        arraycitas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        citas = (ListView) findViewById(R.id.listArray);

        mostrarCitas();

        //Asigno la posibilidad de hcaer click en lost items del listview, en este caso para saber datos de la cita
        citas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog = new AlertDialog.Builder(Cita.this);

                dialog.setTitle("Información Cita");
                dialog.setMessage(obtenerContenido(Integer.parseInt(listaID.get(position).toString())));

                dialog.setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    //Carga las citas cuando vuelves atrás desde la siguiente actividad (agregar citas)
    @Override
    protected void onRestart() {
        mostrarCitas();
        super.onRestart();
    }

    //Carga todas las citas propias en el listview
    public void mostrarCitas() {
        Cursor cursor;
        VarGlobales global = new VarGlobales();
        String query = "select * from Cita where origenid = " + global.getUSuario();

        arraycitas.clear();
        try {
            cursor = db.rawQuery(query, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

            if (cursor.moveToFirst()) {
                do {
                    arraycitas.add(cursor.getString(1));
                    listaID.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        citas.setAdapter(arraycitas);
    }

    //Devuelve el contanido de una cita en un String para poder mostrársela al usuario
    public String obtenerContenido(int id) {
        String mensaje = "";

        String query = "select * from cita where citaid = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                mensaje = "Destino: " + usuarioOrigen(cursor.getInt(5));
                mensaje = mensaje + "\nTitulo: " + cursor.getString(1);
                mensaje = mensaje + "\nComentario: " + cursor.getString(6);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return mensaje;
    }

    //Devuelve el nombre del usuario
    public String usuarioOrigen(int nID) {
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

    //Se abre la actividad para añadir citas
    public void anadirCita(View v) {
        Intent crearcita = new Intent(Cita.this, CrearCita.class);
        startActivity(crearcita);
    }

    public void atras(View v) {
        db.close();
        finish();
    }

}