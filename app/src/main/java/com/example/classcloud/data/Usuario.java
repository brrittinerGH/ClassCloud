package com.example.classcloud.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;
    public String contrasenia;
    public String rol; // "admin", "profe" o "alumno"

    public Usuario(String nombre, String contrasenia, String rol) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }


    public String getNombre() { return nombre; }
    public String getContrasenia() { return contrasenia; }
    public String getRol() { return rol; }
}
