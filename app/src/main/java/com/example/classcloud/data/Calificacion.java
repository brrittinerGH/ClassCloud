package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calificaciones")
public class Calificacion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int alumnoId;
    public int materiaId;
    public double nota;

    public Calificacion(int alumnoId, int materiaId, double nota) {
        this.alumnoId = alumnoId;
        this.materiaId = materiaId;
        this.nota = nota;
    }
}
