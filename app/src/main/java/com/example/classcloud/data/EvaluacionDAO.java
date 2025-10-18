package com.example.classcloud.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EvaluacionDAO {

    @Insert
    void insertar(Evaluacion evaluacion);

    @Query("SELECT * FROM evaluaciones WHERE materia = :materia")
    List<Evaluacion> obtenerPorMateria(String materia);

    @Query("DELETE FROM evaluaciones WHERE id = :id")
    void eliminarPorId(int id);
}
