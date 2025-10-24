package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalificacionDAO {

    @Insert
    void insertar(Calificacion calificacion);

    @Update
    void actualizar(Calificacion calificacion);

    @Delete
    void eliminar(Calificacion calificacion);

    // Obtener todas las calificaciones
    @Query("SELECT * FROM calificaciones")
    List<Calificacion> obtenerTodas();

    // Obtener calificaciones de un alumno
    @Query("SELECT * FROM calificaciones WHERE alumnoId = :idAlumno")
    List<Calificacion> obtenerPorAlumno(int idAlumno);

    // Obtener calificaciones de un alumno en una materia específica
    @Query("SELECT * FROM calificaciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    List<Calificacion> obtenerNotasMateria(int idAlumno, int idMateria);

    // Calcular el promedio del alumno en una materia
    @Query("SELECT AVG(nota) FROM calificaciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    Double promedioPorMateria(int idAlumno, int idMateria);

    @Query("SELECT * FROM calificaciones WHERE materiaId = :materiaId")
    List<Calificacion> obtenerPorMateria(int materiaId);

}
