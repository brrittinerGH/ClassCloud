package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "materias")
public class Materia {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public int profesorId;

    public Materia(String nombre, int profesorId) {
        this.nombre = nombre;
        this.profesorId = profesorId;
    }
}
