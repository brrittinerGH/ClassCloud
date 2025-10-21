package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "asistencias")
public class Asistencia {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int alumnoId;
    public int materiaId;
    public String fecha;
    public String estado;

    public Asistencia(int alumnoId, int materiaId, String fecha, String estado) {
        this.alumnoId = alumnoId;
        this.materiaId = materiaId;
        this.fecha = fecha;
        this.estado = estado;
    }
}
