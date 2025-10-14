package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDAO {

    // Consulta que busca un usuario por nombre y contraseña
    @Query("SELECT * FROM usuarios WHERE nombre = :nombre AND contrasenia = :contrasenia LIMIT 1")
    Usuario login(String nombre, String contrasenia);

    // Inserta un nuevo usuario en la base de datos
    @Insert
    void insertar(Usuario usuario);
}
