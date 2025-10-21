package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UsuarioDAO {

    // Consulta que busca un usuario por nombre y contraseña
    @Query("SELECT * FROM usuarios WHERE nombre = :nombre AND contrasenia = :contrasenia LIMIT 1")
    Usuario login(String nombre, String contrasenia);

    @Insert
    void insertar(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE LOWER(rol) = LOWER(:rol)")
    List<Usuario> obtenerPorRol(String rol);

    @Query("SELECT * FROM usuarios WHERE LOWER(rol) = 'profesor'")
    List<Usuario> obtenerProfesores();

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario obtenerPorId(int id);



}
