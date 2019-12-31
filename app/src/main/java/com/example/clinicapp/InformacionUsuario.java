package com.example.clinicapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//Al igual que la clase de I. Clinica, coge datos del usuario conectado en la APP y rellena los Text View
public class InformacionUsuario extends AppCompatActivity {

    TextView txtnombre;
    TextView txtfecha;
    TextView txtaltura;
    TextView txtpeso;
    TextView txttiposangre;
    ImageView img_foto;

    VarGlobales global;
    BDClinica bdclinica;
    SQLiteDatabase db;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_usuario);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        global = new VarGlobales();

        bdclinica = new BDClinica(this, "BDClinica", null, 1);
        db = bdclinica.getReadableDatabase();
        imageLoader = ImageLoader.getInstance();

        txtnombre = findViewById(R.id.txt_nombre);
        txtfecha = findViewById(R.id.txt_fecha);
        txtaltura = findViewById(R.id.txt_altura);
        txtpeso = findViewById(R.id.txt_peso);
        txttiposangre = findViewById(R.id.txt_tiposangre);
        img_foto = findViewById(R.id.img_fotousuario);

        try {
            rellenarInformacion();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void rellenarInformacion() throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM InformacionUsuario where usuarioid = " + global.getUSuario(), null);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate;
        Date date;
        String url_imagen = "";


        while (cursor.moveToNext()) {
            date = new Date(cursor.getLong(3));
            formattedDate = formatter.format(date);
            txtnombre.setText("Nombre: " + cursor.getString(1) + " " + cursor.getString(2));
            txtfecha.setText("Fecha de nacimiento: " + formattedDate);
            txtaltura.setText("Altura: " + cursor.getString(5) + " cm");
            txtpeso.setText("Peso: " + cursor.getString(4) + " kg");
            txttiposangre.setText("Tipo de Sangre: " + cursor.getString(6));
            url_imagen = cursor.getString(7);
        }

        imageLoader.displayImage(url_imagen, img_foto);
        db.close();
        cursor.close();
    }

    public void volver(View v) {
        finish();
    }
}
