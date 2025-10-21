package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface MateriaDAO {

    @Insert
    void insertar(Materia materia);

    @Update
    void actualizar(Materia materia);

    @Delete
    void eliminar(Materia materia);

    @Query("DELETE FROM materias WHERE id = :id")
    void eliminarPorId(int id);

    @Query("SELECT * FROM materias")
    List<Materia> obtenerTodas();

    @Query("SELECT * FROM materias WHERE profesorId = :idProfesor")
    List<Materia> obtenerPorProfesor(int idProfesor);

    @Query("SELECT * FROM materias WHERE id = :idMateria LIMIT 1")
    Materia obtenerPorId(int idMateria);

    @Query("SELECT * FROM materias WHERE profesorId = :profesorId")
    List<Materia> obtenerPorProfesorId(int profesorId);



}
