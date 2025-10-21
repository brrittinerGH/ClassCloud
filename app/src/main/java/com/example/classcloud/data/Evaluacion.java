package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "evaluaciones")
public class Evaluacion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int materiaId; // 👈 relación con la materia
    public String tipo;
    public String descripcion;
    public String fecha;

    public Evaluacion(int materiaId, String tipo, String descripcion, String fecha) {
        this.materiaId = materiaId;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }
}

