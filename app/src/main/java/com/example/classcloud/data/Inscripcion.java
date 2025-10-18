package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscripciones")
public class Inscripcion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String alumno;   // nombre de usuario del alumno
    public String materia;  // nombre de la materia

    public Inscripcion(String alumno, String materia) {
        this.alumno = alumno;
        this.materia = materia;
    }
}
