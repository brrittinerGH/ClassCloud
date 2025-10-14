package com.example.classcloud.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Indicamos las entidades que usa esta base de datos y su versión
@Database(entities = {Usuario.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // DAO que maneja los usuarios
    public abstract UsuarioDAO usuarioDao();

    // Instancia única (patrón Singleton)
    private static AppDatabase INSTANCE;

    // Método para obtener la instancia de la base de datos
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "classcloud_db" // nombre del archivo de la base de datos
                    )
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
