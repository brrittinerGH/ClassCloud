package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AsistenciaDAO {

    @Insert
    void insertar(Asistencia asistencia);

    @Query("SELECT * FROM asistencias WHERE alumno = :alumno")
    List<Asistencia> obtenerPorAlumno(String alumno);

    @Query("SELECT * FROM asistencias WHERE materia = :materia")
    List<Asistencia> obtenerPorMateria(String materia);

    @Query("DELETE FROM asistencias WHERE id = :id")
    void eliminarPorId(int id);
}
