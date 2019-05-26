package com.example.clinicapp;

//Clase molde para la ListView de 'Mensajeria'. Se guardan los atributos que se mostrarán
//en la linea de dicha ListView.
public class DataModelMensajes {

    String titulo;
    String descripcion;

    public DataModelMensajes(String titulo, String descripcion) {
        this.titulo=titulo;
        this.descripcion=descripcion;
    }

    public String getName() {
        return titulo;
    }

    public String getType() {
        return descripcion;
    }

}
