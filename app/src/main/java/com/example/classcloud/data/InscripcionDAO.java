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

    // 🔹 Obtener todas las inscripciones
    @Query("SELECT * FROM inscripciones")
    List<Inscripcion> obtenerTodas();

    // 🔹 Obtener inscripciones por alumno (por ID)
    @Query("SELECT * FROM inscripciones WHERE alumnoId = :idAlumno")
    List<Inscripcion> obtenerPorAlumno(int idAlumno);

    // 🔹 Verificar si ya está inscripto en una materia
    @Query("SELECT COUNT(*) FROM inscripciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    int existeInscripcion(int idAlumno, int idMateria);

    // 🔹 Eliminar inscripción específica
    @Query("DELETE FROM inscripciones WHERE alumnoId = :idAlumno AND materiaId = :idMateria")
    void eliminarInscripcion(int idAlumno, int idMateria);
}
