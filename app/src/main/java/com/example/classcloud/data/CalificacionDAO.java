package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CalificacionDAO {

    @Insert
    void insertar(Calificacion calificacion);

    @Query("SELECT * FROM calificaciones WHERE alumno = :alumno")
    List<Calificacion> obtenerPorAlumno(String alumno);

    @Query("SELECT AVG(nota) FROM calificaciones WHERE alumno = :alumno AND materia = :materia")
    Double promedioPorMateria(String alumno, String materia);

    @Query("DELETE FROM calificaciones WHERE id = :id")
    void eliminarPorId(int id);
}
