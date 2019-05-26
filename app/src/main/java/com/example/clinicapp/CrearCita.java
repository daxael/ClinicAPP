package com.example.clinicapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Clase que se utiliza para crear una nueva cita
public class CrearCita extends AppCompatActivity implements View.OnClickListener {
    private Button bfecha, bhora, bguardar, batras;
    private Spinner spinner_medicos;
    private EditText etitulo, enotas;
    private EditText efecha, ehora;
    private int dia, mes, anio, horas, minutos;
    private String fecha, hora;
    private BDClinica conexion;
    private SQLiteDatabase db;
    private List listaID = new ArrayList();

    VarGlobales global;
    Calendar calendario;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearcita);

        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        spinner_medicos = (Spinner) findViewById(R.id.sp_medicos);
        etitulo = (EditText) findViewById(R.id.etxtnombre);
        efecha = (EditText) findViewById(R.id.txtfecha);
        ehora = (EditText) findViewById(R.id.etxthora);
        enotas = (EditText) findViewById(R.id.txtcomentarios);

        bhora = (Button) findViewById(R.id.btnhora);
        bguardar = (Button) findViewById(R.id.btnguardar);
        bfecha = (Button) findViewById(R.id.btnfecha);
        batras = (Button) findViewById(R.id.btnatras);


        bfecha.setOnClickListener(this);
        bhora.setOnClickListener(this);
        bguardar.setOnClickListener(this);
        batras.setOnClickListener(this);

        global = new VarGlobales();

        etitulo.requestFocus(); //cada vez que se abra la actividad, enfoca al titulo de la cita.
        rellenarSpinner(); //rellena el spinner con el nombre de los médicos
    }

    @Override
    public void onClick(View v) {
        if (v == bfecha) {
            calendario = Calendar.getInstance();
            anio = calendario.get(Calendar.YEAR);
            mes = calendario.get(Calendar.MONTH);
            dia = calendario.get(Calendar.DAY_OF_MONTH);


            datePickerDialog = new DatePickerDialog(CrearCita.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    efecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    fecha = String.valueOf(dayOfMonth) + "0" + String.valueOf(month + 1) + String.valueOf(year);
                }
            }
                    , dia, mes, anio);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
            efecha.setError(null);
        } else if (v == bhora) {
            final Calendar calendario = Calendar.getInstance();
            horas = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            hora = String.valueOf(horas) + String.valueOf(minutos);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    String minutos = null;
                    minutos = String.valueOf(minute);
                    if (minute < 10) {
                        minutos = "0" + minute;
                    }
                    ehora.setText(hourOfDay + ":" + minutos);
                }
            }
                    , horas, minutos, true);

            timePickerDialog.show();
            ehora.setError(null);
        } else if (v == bguardar) {
            try {
                guardarDatos();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (v == batras) {
            finish();
        }
    }

    //Se encarga de revisar que estén todos los datos necesarios, y los guarda en la BBDD
    public void guardarDatos() throws ParseException {
        String titulo, notas;

        if (etitulo.getText().toString().equals("")) {
            etitulo.setError("Introduce el titulo");
            etitulo.requestFocus();
        } else if (efecha.getText().toString().equals("")) {
            efecha.setError("Introduce la fecha");
            efecha.requestFocus();
        } else if (ehora.getText().toString().equals("")) {
            ehora.setError("Introduce la hora");
            ehora.requestFocus();
        } else {
            titulo = etitulo.getText().toString();
            notas = enotas.getText().toString();

            String query = "INSERT INTO Cita(Titulo, Fecha,Hora,OrigenID,DestinatarioID,Comentario) VALUES ('" + titulo + "'," + fecha + "," + hora + "," + global.getUSuario() + "," + listaID.get(spinner_medicos.getSelectedItemPosition()) + ",'" + notas + "')";
            db.execSQL(query);

            Toast.makeText(this, "Cita creada.", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    //Rellena el spinner con los nombres de los médicos
    public void rellenarSpinner() {
        Cursor cursor;
        String query = "";
        String provisional;

        if (global.getEsMedico() == 1) { //Si es un médico el usuario, da opción a seleccionar todos los usuarios menos él mismo
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
                spinner_medicos.setAdapter(adapter);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cerrarActividad(View v) {
        db.close();
        finish();
    }
}