package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "asistencias")
public class Asistencia {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String alumno;   // nombre o usuario del alumno
    public String materia;  // nombre de la materia
    public String fecha;    // formato: "2025-10-18"
    public String estado;   // "Presente" o "Ausente"

    public Asistencia(String alumno, String materia, String fecha, String estado) {
        this.alumno = alumno;
        this.materia = materia;
        this.fecha = fecha;
        this.estado = estado;
    }
}
