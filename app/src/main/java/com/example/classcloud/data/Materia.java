package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "materias")
public class Materia {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String profesor;

    public Materia(String nombre, String profesor) {
        this.nombre = nombre;
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
