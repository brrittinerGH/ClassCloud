package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button btnVolver;
    private EvaluacionDAO evaluacionDao;
    private MateriaDAO materiaDao;
    private int profesorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_ver_evaluaciones);

        //  Referencias de vistas
        lvEvaluaciones = findViewById(R.id.listEvaluaciones);
        btnVolver = findViewById(R.id.btnVolver);

        //  DAOs
        AppDatabase db = AppDatabase.getInstance(this);
        evaluacionDao = db.evaluacionDao();
        materiaDao = db.materiaDao();

        //  Recibir ID del profesor logueado
        profesorId = getIntent().getIntExtra("idProfesor", -1);
        if (profesorId == -1) {
            Toast.makeText(this, getString(R.string.errorIdProfe), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mostrarEvaluaciones();

        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarEvaluaciones() {
        //  Obtener materias del profesor
        List<Materia> materiasProfesor = materiaDao.obtenerPorProfesorId(profesorId);

        if (materiasProfesor.isEmpty()) {
            Toast.makeText(this, getString(R.string.noMateriasAsig), Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> datos = new ArrayList<>();

        //  Recorrer cada materia y traer sus evaluaciones
        for (Materia m : materiasProfesor) {
            List<Evaluacion> evals = evaluacionDao.obtenerPorMateria(m.id);

            if (evals.isEmpty()) continue;

            datos.add("📘 " + m.nombre + ":");

            for (Evaluacion e : evals) {
                String texto = "   • " + e.tipo + " - " + e.descripcion + "\n" +
                        "     📅 " + e.fecha;
                datos.add(texto);
            }

            datos.add("");
        }

        if (datos.isEmpty()) {
            datos.add(getString(R.string.noEvalsRegis));
        }

        //  Mostrar en ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, datos);
        lvEvaluaciones.setAdapter(adapter);
    }
}
