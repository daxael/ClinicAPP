package com.example.clinicapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

//Clase que carga los servicios que ofrece la clinica al usuario, y le permite "contratarlos"
//Es una clase de prueba básica, por tanto únicamente da la opción de visualizar los servicios
//Y "contratarlos" y "cancelarlos" sin restricciones.
public class Servicios extends AppCompatActivity {
    private ListView lv_servicios;
    private BDClinica conexion;
    private SQLiteDatabase db;

    ArrayAdapter<String> adapter;
    List<String> lista_servicios = new ArrayList<String>();
    String[] itemname;
    AlertDialog.Builder dialog;
    VarGlobales global;

    int usuarioID;
    boolean servicio_contratado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);
        lv_servicios = findViewById(R.id.lv_servicios);

        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();

        global = new VarGlobales();
        usuarioID = global.getUSuario();
        cargarServiciosLV();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista_servicios);

        lv_servicios.setAdapter(adapter);

        //Al seleccionar un item (servicio) da la opción de ver si está contratado o no, y
        //de contratar ese servicio o cancelarlo.
        lv_servicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                servicio_contratado = comprobarContratatado(position + 1);
                String estado_servicio = "Este servicio actualmente se encuentra: ";

                if (servicio_contratado) {
                    estado_servicio = estado_servicio + " CONTRATADO";
                } else {
                    estado_servicio = estado_servicio + " SIN CONTRATAR";
                }

                dialog = new AlertDialog.Builder(Servicios.this);

                dialog.setTitle(estado_servicio);

                dialog.setNeutralButton("Atras", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                if (servicio_contratado) {
                    dialog.setNegativeButton("Cancelar Servicio", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            borrarServicioUsuario(position + 1);
                            Toast.makeText(Servicios.this, "Servicio Cancelado.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    dialog.setPositiveButton("Contratar Servicio", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contratarServicio(position + 1);
                            Toast.makeText(Servicios.this, "Servicio Contratado.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                dialog.show();
            }
        });
    }

    //Carga los distintos servicios de la BBDD a la listview
    public void cargarServiciosLV() {
        try {
            cargarServiciosBDD();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        itemname = new String[lista_servicios.size()];
        lista_servicios.toArray(itemname);
    }

    //Guarda los servicios desde la BBDD a la lista lista_servicios, para asignarloa la LV posteriormente
    public void cargarServiciosBDD() throws ParserConfigurationException, IOException, SAXException {
        String query = "select * from Servicio";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                lista_servicios.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void contratarServicio(int servicioID) {
        String query = "INSERT INTO ServicioUsuario(ServicioID, UsuarioID,Fecha_Inicio,Fecha_Fin) VALUES (" + servicioID + "," + usuarioID + "," + global.devolverFechaActual() + "," + global.devolverFechaActual() + ")";
        db.execSQL(query);
    }

    public void borrarServicioUsuario(int servicioID) {
        String query = "DELETE FROM ServicioUsuario WHERE usuarioid = " + usuarioID + " AND servicioid = " + servicioID;
        db.execSQL(query);
    }

    public boolean comprobarContratatado(int servicioID) {
        Cursor d = db.rawQuery("Select Count(*)  from ServicioUsuario where usuarioid= " + usuarioID + " and servicioID=" + servicioID, null);
        d.moveToFirst();
        int resultado = d.getInt(0);
        d.close();

        if (resultado == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void volver(View v) {
        db.close();
        finish();
    }
}
