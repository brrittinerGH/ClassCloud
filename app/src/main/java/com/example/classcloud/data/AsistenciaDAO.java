package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AsistenciaDAO {

    @Insert
    void insertar(Asistencia asistencia);

    @Update
    void actualizar(Asistencia asistencia);

    @Delete
    void eliminar(Asistencia asistencia);

    @Query("SELECT * FROM asistencias")
    List<Asistencia> obtenerTodas();

    @Query("SELECT * FROM asistencias WHERE alumnoId = :idAlumno")
    List<Asistencia> obtenerPorAlumno(int idAlumno);

    @Query("SELECT * FROM asistencias WHERE materiaId = :idMateria")
    List<Asistencia> obtenerPorMateria(int idMateria);

    @Query("SELECT * FROM asistencias WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    List<Asistencia> obtenerPorAlumnoYMateria(int idAlumno, int idMateria);
}
