package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InscripcionDAO {

    @Insert
    void insertar(Inscripcion inscripcion);

    @Query("SELECT * FROM inscripciones WHERE alumno = :alumno")
    List<Inscripcion> obtenerPorAlumno(String alumno);

    @Query("DELETE FROM inscripciones WHERE alumno = :alumno AND materia = :materia")
    void eliminar(String alumno, String materia);
}
