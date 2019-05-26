package com.example.clinicapp;

import java.text.SimpleDateFormat;
import java.util.Date;

//Clase donde guardo información a la que se pueda acceder de forma global, como la ID del usuario
//que entra.
public class VarGlobales {
    private static VarGlobales instance = null;
    private static int usuarioid;
    private static int EsMedico;

    protected VarGlobales() {

    }

    public static VarGlobales getInstance() {
        if (instance == null) {
            instance = new VarGlobales();
        }
        return instance;
    }

    public void setUsuario(int id) {
        this.usuarioid = id;
    }

    public int getUSuario() {
        return this.usuarioid;
    }

    public String devolverFechaActual() {
        String todayAsString = new SimpleDateFormat("ddMMyyyy").format(new Date());
        return todayAsString;
    }

    public void setEsMedico(int es_medico) {
        this.EsMedico = es_medico;
    }

    public int getEsMedico() {
        return this.EsMedico;
    }

}
