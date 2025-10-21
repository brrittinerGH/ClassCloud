package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface InscripcionDAO {

    @Insert
    void insertar(Inscripcion inscripcion);

    @Delete
    void eliminar(Inscripcion inscripcion);

    @Query("SELECT * FROM inscripciones")
    List<Inscripcion> obtenerTodas();

    @Query("SELECT * FROM inscripciones WHERE alumnoId = :idAlumno")
    List<Inscripcion> obtenerPorAlumno(int idAlumno);

    @Query("SELECT COUNT(*) FROM inscripciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    int existeInscripcion(int idAlumno, int idMateria);

    @Query("DELETE FROM inscripciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    void eliminarInscripcion(int idAlumno, int idMateria);

    @Query("SELECT alumnoId FROM inscripciones WHERE materiaId = :materiaId")
    List<Integer> obtenerAlumnosPorMateria(int materiaId);

    @Query("SELECT * FROM Inscripciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria LIMIT 1")
    Inscripcion obtenerPorAlumnoYMateria(int idAlumno, int idMateria);


}
