package com.example.clinicapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDClinica extends SQLiteOpenHelper {

    //Clase que utilizo para crear la BBDD que se creará localmente en el dispositivo, en la cual se guardarán los diversos datos.
    //A continuación están las distintas tablas y un par de INSERTS para tener datos de ejemplo.

    String sqlCreateTablaUsuario = "CREATE TABLE Usuario (UsuarioID INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR(20) UNIQUE NOT NULL, EsMedico Boolean NOT NULL,Email VARCHAR(30)  UNIQUE NOT NULL, Telefono VARCHAR(9)  NOT NULL, DNI VARCHAR(9)  UNIQUE NOT NULL, Contrasena VARCHAR(30)  NOT NULL )";
    String sqlCreateTablaEmpresa = "CREATE TABLE Empresa (EmpresaID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Nombre VARCHAR(20) NOT NULL, Direccion VARCHAR(30)  NOT NULL, Email VARCHAR(30)  UNIQUE NOT NULL, Telefono VARCHAR(9)  UNIQUE NOT NULL, Fax VARCHAR(9), Web VARCHAR(30))";
    String sqlCreateTablaArchivos = "CREATE TABLE Archivos (ArchivoID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Descripcion VARCHAR(15), Prueba VARCHAR2(50))";
    String sqlCreateTablaCita = "CREATE TABLE Cita(CitaID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Titulo VARCHAR(50) NOT NULL, Fecha Date NOT NULL,Hora TIME NOT NULL, OrigenID INTEGER NOT NULL, DestinatarioID INTEGENER NOT NULL,Comentario VARCHAR(200))";
    String sqlCreateTableInformacionUsuario = "CREATE TABLE InformacionUsuario (InformacionID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, Nombre VARCHAR NOT NULL, Apellidos VARCHAR NOT NULL,  Fecha DATE NOT NULL, Peso FLOAT NOT NULL, Altura FLOAT NOT NULL, Tipo_Sangre VARCHAR(4), Foto BLOB,UsuarioID INTEGER NOT NULL UNIQUE REFERENCES Usuario (UsuarioID))";
    String sqlCreateTableServicio = "CREATE TABLE Servicio (ServicioID INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, Descripcion VARCHAR(100) NOT NULL, Precio FLOAT)";
    String sqlCreateTableServicioUsuario = "CREATE TABLE ServicioUsuario(ServicioUsuarioID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,ServicioID INTEGER NOT NULL REFERENCES Servicio (ServicioID), UsuarioID INTEGER NOT NULL REFERENCES Usuario (UsuarioID), Fecha_Inicio DATE NOT NULL, Fecha_Fin DATE NOT NULL)";
    String sqlCreateTableMensaje = "CREATE TABLE Mensaje(MensajeID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Titulo VARCHAR(30) NOT NULL,Contenido VARCHAR(100) NOT NULL, Respondido Boolean, Fecha DATE NOT NULL, DestinatarioID INTEGER NOT NULL, OrigenID INTEGER NOT NULL)";

    //Inserts para obtener información de prueba, para poder testear la APP


    String sqlInsertUsuario = "INSERT INTO Usuario(UsuarioID, Nombre,EsMedico, Email, Telefono, DNI, Contrasena) VALUES(null, 'Rodrigo',0, 'prueba@gmail.com', 693959218, 'H0545923D', '1111a')";
    String sqlInsertUsuarioDos = "INSERT INTO Usuario(UsuarioID, Nombre,EsMedico, Email, Telefono, DNI, Contrasena) VALUES(null, 'Alberto',1, 'prueba2@gmail.com', 60623454, 'H054443223D', '1111a')";
    String sqlInsertUsuarioTres = "INSERT INTO Usuario(UsuarioID, Nombre,EsMedico, Email, Telefono, DNI, Contrasena) VALUES(null, 'Rafael',1, 'prueba3@gmail.com', 693845839, 'H032923D', '1111a')";
    String sqlInsertInformacionUsuario = "INSERT INTO InformacionUsuario(InformacionID, Nombre,Apellidos, Fecha, Peso,Altura, Tipo_Sangre,UsuarioID) VALUES(null, 'Rodrigo','Novoa', 15041997, 70.0, 175,'B',1)";
    String sqlInsertInformacionUsuarioDos = "INSERT INTO InformacionUsuario(InformacionID, Nombre,Apellidos, Fecha, Peso,Altura, Tipo_Sangre,UsuarioID) VALUES(null, 'Alberto','Suarez Gómez', 26052019, 70.0, 175,'B',2)";
    String sqlInsertInformacionClinica = "INSERT INTO Empresa(EmpresaID, Nombre,Direccion, Email, Telefono, Fax, Web) VALUES(1, 'Clinica Prueba','c/ Prueba 16', 'emailclinica@gmail.com', 943523341, 9435864431,'wwww.clinicapp.com')";
    String sqlInsertServicioUno = "INSERT INTO Servicio(ServicioID, Descripcion) VALUES(null, 'SERVICIO DE PRUEBA UNO')";
    String sqlInsertServicioDos = "INSERT INTO Servicio(ServicioID, Descripcion) VALUES(null, 'SERVICIO DE PRUEBA DOS')";


    public BDClinica(Context contexto, String nombre,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateTablaUsuario);
        db.execSQL(sqlCreateTablaEmpresa);
        db.execSQL(sqlCreateTablaArchivos);
        db.execSQL(sqlCreateTablaCita);
        db.execSQL(sqlCreateTableInformacionUsuario);
        db.execSQL(sqlCreateTableServicio);
        db.execSQL(sqlCreateTableServicioUsuario);
        db.execSQL(sqlCreateTableMensaje);


        db.execSQL(sqlInsertUsuario);
        db.execSQL(sqlInsertUsuarioDos);
        db.execSQL(sqlInsertUsuarioTres);
        db.execSQL(sqlInsertInformacionUsuario);
        db.execSQL(sqlInsertInformacionUsuarioDos);
        db.execSQL(sqlInsertInformacionClinica);
        db.execSQL(sqlInsertServicioUno);
        db.execSQL(sqlInsertServicioDos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}

