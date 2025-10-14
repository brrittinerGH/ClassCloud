package com.example.classcloud.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.classcloud.data.MateriaDAO;

@Database(entities = {Usuario.class, Materia.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDAO usuarioDao();

    public abstract MateriaDAO materiaDao();


    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "classcloud_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() // 🔹 necesario al cambiar la versión
                    .build();
        }
        return INSTANCE;
    }
}

