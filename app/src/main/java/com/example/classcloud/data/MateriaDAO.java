package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MateriaDAO {

    @Insert
    void insertar(Materia materia);

    @Query("SELECT * FROM materias")
    List<Materia> obtenerTodas();

    @Query("SELECT * FROM materias WHERE id = :id")
    Materia obtenerPorId(int id);

    @Update
    void actualizar(Materia materia);

    @Query("DELETE FROM materias WHERE id = :id")
    void eliminarPorId(int id);

    @Query("SELECT * FROM materias WHERE LOWER(profesor) = LOWER(:nombreProfesor)")
    List<Materia> obtenerPorProfesor(String nombreProfesor);

}
