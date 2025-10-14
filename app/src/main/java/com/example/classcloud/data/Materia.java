package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "materias")
public class Materia {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String profesor; // guardamos el nombre o usuario del profesor asignado

    public Materia(String nombre, String profesor) {
        this.nombre = nombre;
        this.profesor = profesor;
    }
}
