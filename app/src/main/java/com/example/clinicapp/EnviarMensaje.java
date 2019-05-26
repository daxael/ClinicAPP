package com.example.clinicapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Clase para enviar un nuevo mensaje a un médico (o cualquier usuario, si eres médico)
public class EnviarMensaje extends AppCompatActivity {

    private BDClinica conexion;
    private SQLiteDatabase db;
    private Spinner sp_meds;
    private List listaID = new ArrayList();
    private EditText edt_titulo;
    private EditText edt_mensaje;

    VarGlobales global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_mensaje);
        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        sp_meds = (Spinner) findViewById(R.id.sp_meds);
        edt_titulo = (EditText) findViewById(R.id.edt_titulo);
        edt_mensaje = (EditText) findViewById(R.id.edt_mensaje);

        global = new VarGlobales();

        edt_titulo.requestFocus();
        rellenarSpinner();
    }

    //Se rellena el spinner de usuarios, para poder seleccionar a cuál enviar el mensaje.
    public void rellenarSpinner() {
        Cursor cursor;
        String query = "";
        String provisional;

        if (global.getEsMedico() == 1) {
            query = "select * from usuario where usuarioid != " + global.getUSuario();

        } else {
            query = "select * from usuario where EsMedico = 1 and usuarioid != " + global.getUSuario();

        }

        try {
            cursor = db.rawQuery(query, null);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

            if (cursor.moveToFirst()) {
                do {
                    provisional = cursor.getString(1);
                    adapter.add(provisional);
                    listaID.add(cursor.getString(0));
                } while (cursor.moveToNext());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_meds.setAdapter(adapter);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Guarda el destinatario y el origen del mensaje
    public void enviarMensaje(View v) {
        if (edt_titulo.getText().toString().equals("") == false && edt_mensaje.getText().toString().equals("") == false) {
            String query = "INSERT INTO Mensaje(Titulo,Contenido,Fecha,OrigenID,DestinatarioID) VALUES ('" + edt_titulo.getText().toString() + "','" + edt_mensaje.getText().toString() + "'," + global.devolverFechaActual() + "," + global.getUSuario() + "," + listaID.get(sp_meds.getSelectedItemPosition()) + ")";
            db.execSQL(query);
            Toast.makeText(this, "Mensaje enviado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Se deben rellenar tanto el título como el mensaje.", Toast.LENGTH_SHORT).show();
        }
    }

    public void atras(View v) {
        db.close();
        finish();
    }
}
