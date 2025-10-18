package com.example.classcloud.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class, Materia.class, Inscripcion.class, Evaluacion.class, Calificacion.class, Asistencia.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDAO usuarioDao();
    public abstract MateriaDAO materiaDao();
    public abstract InscripcionDAO inscripcionDao();
    public abstract EvaluacionDAO evaluacionDao();
    public abstract CalificacionDAO calificacionDao();
    public abstract AsistenciaDAO asistenciaDao();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "classcloud_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}

