package com.example.clinicapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//Clase que redirecciona a las distintas clases (opciones) de la APP. Carga el nombre del usuario
//que está en la sesión de la APP.
public class MenuPrincipal extends AppCompatActivity {
    private TextView txt_nombre;
    private VarGlobales global;
    private BDClinica conexion;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        txt_nombre = (TextView) findViewById(R.id.tv_nombreusuario);

        global = new VarGlobales();

        txt_nombre.setText("Usuario: " + devolverNombreUsuario(global.getUSuario()));
    }

    public void abrirInformacion(View v) {
        Intent intent = new Intent(getApplication(), InformacionClinica.class);
        startActivity(intent);
    }

    public void abrirInformacionUsuario(View v) {
        Intent intent = new Intent(getApplication(), InformacionUsuario.class);
        startActivity(intent);
    }

    public void abrirListadoServicios(View v) {
        Intent intent = new Intent(getApplication(), Servicios.class);
        startActivity(intent);
    }

    public void abrirMensajes(View v) {
        Intent intent = new Intent(getApplication(), Mensajeria.class);
        startActivity(intent);
    }

    public void abrirCitas(View v) {
        Intent intent = new Intent(getApplication(), Cita.class);
        startActivity(intent);
    }

    public void abrirConfiguracion(View v) {
        Intent intent = new Intent(getApplication(), ConfiguracionUsuario.class);
        startActivity(intent);
    }

    public void cerrarSesion(View v) {
        VarGlobales global = new VarGlobales();
        global.setUsuario(-1);
        finish();
    }

    protected void onRestart() {
        txt_nombre.setText("Usuario: " + devolverNombreUsuario(global.getUSuario()));
        super.onRestart();
    }

    public String devolverNombreUsuario(int nID) {
        Cursor d = db.rawQuery("Select * from usuario where usuarioID = " + nID, null);
        d.moveToFirst();
        if (d.getCount() > 0) {
            return d.getString(1);
        } else {
            return "";
        }
    }
}
