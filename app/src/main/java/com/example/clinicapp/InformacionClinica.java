package com.example.clinicapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//Clase en la que se carga la información general de la clínica.

public class InformacionClinica extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView txtnombre;
    TextView txtdireccion;
    TextView txtemail;
    TextView txttelefono;
    TextView txtfax;
    TextView txtweb;

    BDClinica bdclinica;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_clinica);

        //Se crea un MapFragment para poder asignar ahí la ID del mapa, y poder manipularlo.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bdclinica = new BDClinica(this, "BDClinica", null, 1);
        db = bdclinica.getReadableDatabase();

        txtnombre = (TextView) findViewById(R.id.txt_nombre);
        txtdireccion = (TextView) findViewById(R.id.txt_direccion);
        txtemail = (TextView) findViewById(R.id.txt_email);
        txttelefono = (TextView) findViewById(R.id.txt_telefono);
        txtfax = (TextView) findViewById(R.id.txt_fax);
        txtweb = (TextView) findViewById(R.id.txt_web);

        rellenarInformacion();
    }

    //Asignamos unas coordenadas en el mapa, las cuales se reflejarán en el posicionamiento de éste, en la APP
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        LatLng clinica = new LatLng(40.712784, -74.005941);
        mMap.addMarker(new MarkerOptions().position(clinica).title("Clinica"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clinica, 13));
    }

    //Rellena los distintos TextView con información de la clínica, desde la BBDD
    public void rellenarInformacion() {
        Cursor cursor = db.rawQuery("SELECT * FROM empresa where empresaid = 1", null);

        while (cursor.moveToNext()) {
            txtnombre.setText("Nombre: " + cursor.getString(1));
            txtdireccion.setText("Dirección: " + cursor.getString(2));
            txtemail.setText("E-mail: " + cursor.getString(3));
            txttelefono.setText("Teléfono: " + cursor.getString(4));
            txtfax.setText("Fax: " + cursor.getString(5));
            txtweb.setText("Web: " + cursor.getString(6));
        }

        cursor.close();
        db.close();
    }

    public void volver(View v) {
        finish();
    }

}
