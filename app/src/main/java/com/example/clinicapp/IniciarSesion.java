package com.example.clinicapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Clase principal desde la que el usuario inicia sesión.
public class IniciarSesion extends AppCompatActivity {
    BDClinica bdclinica;
    SQLiteDatabase db;

    String usuario;
    String contrasena;

    private Button bacceder;
    private EditText unombre, ucontrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciarsesion_layout);

        bacceder = findViewById(R.id.btn_continuar);
        unombre = findViewById(R.id.edt_nombre);
        ucontrasena = findViewById(R.id.edt_contrasena);

        bdclinica = new BDClinica(this, "BDClinica", null, 1);
        db = bdclinica.getReadableDatabase();

        //En el siguiente bloque de código se realizan validaciones para comprobar que los campos
        //no estén vacíos, y que el usuario introduzca datos correctos.
        bacceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unombre.getText().toString().isEmpty()) {
                    unombre.setError("Introduzca un nombre válido."); //Da un mensaje de error, y redirige al usuario al campo.
                    unombre.requestFocus();
                } else if (ucontrasena.getText().toString().isEmpty() || ucontrasena.getText().length() < 4) {
                    ucontrasena.setError("Introduzca una contraseña válida.");
                    ucontrasena.requestFocus();
                } else {
                    usuario = unombre.getText().toString();
                    contrasena = ucontrasena.getText().toString();
                    Cursor d = db.rawQuery("Select * from Usuario where nombre= '" + usuario + "' and contrasena='" + contrasena + "'", null);
                    d.moveToFirst();
                    if (d.getCount() > 0) { //Revisa que exista el usuario, y sigue al menú asginando esa ID
                        int resultado = d.getInt(0);
                        int es_medico = d.getInt(2);
                        String nombre = d.getString(1);
                        VarGlobales global = new VarGlobales();
                        global.setUsuario(resultado); //Asigna a una clase global que el ID del usuario
                        global.setEsMedico(es_medico); //Asigna a una clase globa si el usuario es médico o no
                        Intent intento = new Intent(getApplicationContext(), MenuPrincipal.class); //Abre la clase del menú
                        d.close();
                        startActivity(intento);
                    } else {
                        Toast.makeText(getApplicationContext(), "Datos Incorrectos, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onRestart() {
        unombre.getText().clear();
        ucontrasena.getText().clear();
        unombre.requestFocus();
        super.onRestart();
    }

    public void cerrarAPP(View v) {
        finish();
        System.exit(0);
    }
}
