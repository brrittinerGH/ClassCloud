package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EvaluacionDAO {

    // 🔹 Insertar una nueva evaluación
    @Insert
    void insertar(Evaluacion evaluacion);

    // 🔹 Obtener todas las evaluaciones
    @Query("SELECT * FROM evaluaciones")
    List<Evaluacion> obtenerTodas();

    // 🔹 Obtener evaluaciones de una materia específica (por ID)
    @Query("SELECT * FROM evaluaciones WHERE materiaId = :materiaId")
    List<Evaluacion> obtenerPorMateria(int materiaId);

    // 🔹 Eliminar una evaluación por su ID
    @Query("DELETE FROM evaluaciones WHERE id = :id")
    void eliminarPorId(int id);

    // 🔹 Actualizar evaluación existente (opcional)
    @Update
    void actualizar(Evaluacion evaluacion);

    // 🔹 Eliminar todas las evaluaciones (útil para limpiar durante pruebas)
    @Query("DELETE FROM evaluaciones")
    void eliminarTodas();
}
