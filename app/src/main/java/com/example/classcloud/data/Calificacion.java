package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calificaciones")
public class Calificacion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String alumno;   // nombre o usuario del alumno
    public String materia;  // nombre de la materia
    public double nota;     // nota numérica (ej. 8.5)

    public Calificacion(String alumno, String materia, double nota) {
        this.alumno = alumno;
        this.materia = materia;
        this.nota = nota;
    }
}
