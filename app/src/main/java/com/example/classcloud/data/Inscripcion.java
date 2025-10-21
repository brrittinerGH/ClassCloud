package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscripciones")
public class Inscripcion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int alumnoId;
    public int materiaId;

    public Inscripcion(int alumnoId, int materiaId) {
        this.alumnoId = alumnoId;
        this.materiaId = materiaId;
    }
}
