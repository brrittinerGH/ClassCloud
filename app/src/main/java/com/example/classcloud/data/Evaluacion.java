package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "evaluaciones")
public class Evaluacion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String materia;
    public String tipo;
    public String descripcion;
    public String fecha;

    public Evaluacion(String materia, String tipo, String descripcion, String fecha) {
        this.materia = materia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
}
