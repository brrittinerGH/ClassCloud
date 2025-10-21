package com.example.classcloud.ui.profesor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Evaluacion;
import com.example.classcloud.data.EvaluacionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;

import java.util.ArrayList;
import java.util.List;

public class ProfesorVerEvaluacionesActivity extends AppCompatActivity {

    private ListView lvEvaluaciones;
    private EvaluacionDAO evaluacionDao;
    private MateriaDAO materiaDao;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_ver_evaluaciones);

        lvEvaluaciones = findViewById(R.id.lyEvaluaciones);
        evaluacionDao = AppDatabase.getInstance(this).evaluacionDao();
        materiaDao = AppDatabase.getInstance(this).materiaDao();

        mostrarEvaluaciones();
    }

    private void mostrarEvaluaciones() {
        List<Evaluacion> listaEvaluaciones = evaluacionDao.obtenerTodas();
        if (listaEvaluaciones.isEmpty()) {
            Toast.makeText(this, "No hay evaluaciones registradas", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> datos = new ArrayList<>();
        for (Evaluacion e : listaEvaluaciones) {
            // Obtener la materia correspondiente
            Materia m = materiaDao.obtenerPorId(e.materiaId);
            String nombreMateria = (m != null) ? m.nombre : "Materia desconocida";

            // Crear el texto para el ListView
            String texto = "📘 " + nombreMateria + "\n" +
                    e.tipo + ": " + e.descripcion + "\n" +
                    "📅 " + e.fecha;

            datos.add(texto);
        }

        // Crear el adapter y asignarlo al ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, datos);
        lvEvaluaciones.setAdapter(adapter);
    }
}
