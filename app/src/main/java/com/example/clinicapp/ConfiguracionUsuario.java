package com.example.clinicapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//Clase creada para añadir configuraciones de la cuenta de usuario. Le he puesto dos ejemplos básicos
//(posibilidad de cambiar nombre y contraseña)
public class ConfiguracionUsuario extends AppCompatActivity {
    private VarGlobales global;
    private BDClinica conexion;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_usuario);

        global = new VarGlobales();
        conexion = new BDClinica(getApplicationContext(), "BDClinica", null, 1);
        db = conexion.getReadableDatabase();
    }

    //Comprueba la disponibilidad del nombre de usuario
    public boolean comprobarUsuario(String nombre) {
        Cursor d = db.rawQuery("Select * from usuario where Nombre = '" + nombre + "'", null);
        d.moveToFirst();
        if (d.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    //Se encarga de cambiar el nombre de usuario, introduciendo el nombre y revisando si está
    //disponible o no. Se abre un dialog que pide el nuevo nombre.
    public void cambiarUsuario(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduzca el nuevo nombre.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty() == false) {
                    if (comprobarUsuario(input.getText().toString()) == true) {
                        String query = "UPDATE Usuario SET Nombre = '" + input.getText().toString() + "' WHERE UsuarioID = " + global.getUSuario();
                        db.execSQL(query);
                        Toast.makeText(ConfiguracionUsuario.this, "Usuario cambiado correctamente.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConfiguracionUsuario.this, "Debe introducir un nombre, por favor.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Método para cambiar de contraseña. Se abre un dialog que pide la nueva contraseña.
    public void cambiarContrasena(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduzca la nueva contraseña.");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty() == false) {
                    if (comprobarUsuario(input.getText().toString()) == true) {
                        if (comprobarUsuario(input.getText().toString()) == true) {
                            String query = "UPDATE Usuario SET Contrasena = '" + input.getText().toString() + "' WHERE UsuarioID = " + global.getUSuario();
                            db.execSQL(query);
                            Toast.makeText(ConfiguracionUsuario.this, "Contraseña cambiada correctamente.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ConfiguracionUsuario.this, "Debe introducir un nombre, por favor.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void volver(View v) {
        db.close();
        finish();
    }

}
