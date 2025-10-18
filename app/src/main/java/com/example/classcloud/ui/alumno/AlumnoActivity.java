package com.example.classcloud.ui.alumno;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Asistencia;
import com.example.classcloud.data.Calificacion;
import com.example.classcloud.data.Evaluacion;
import com.example.classcloud.data.Inscripcion;
import com.example.classcloud.data.InscripcionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class AlumnoActivity extends AppCompatActivity {

    private ListView listMaterias;
    private Button btnVerInscripciones, btnVolver;
    private MateriaDAO materiaDao;
    private InscripcionDAO inscripcionDao;
    private List<Materia> materias;
    private ArrayAdapter<String> adapter;
    private String usuarioActual = "alumno";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        listMaterias = findViewById(R.id.listMaterias);
        btnVerInscripciones = findViewById(R.id.btVerInscripciones);
        btnVolver = findViewById(R.id.btVolver);

        materiaDao = AppDatabase.getInstance(this).materiaDao();
        inscripcionDao = AppDatabase.getInstance(this).inscripcionDao();

        cargarMaterias();

        listMaterias.setOnItemClickListener((parent, view, position, id) -> {
            Materia seleccionada = materias.get(position);
            mostrarDialogoInscripcion(seleccionada);
        });

        btnVerInscripciones.setOnClickListener(v -> mostrarInscripciones());

        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        Button btnVerEvaluaciones = findViewById(R.id.btVerEvaluaciones);

        btnVerEvaluaciones.setOnClickListener(v -> {
            List<Inscripcion> inscripciones = inscripcionDao.obtenerPorAlumno(usuarioActual);
            StringBuilder builder = new StringBuilder();

            for (Inscripcion i : inscripciones) {
                List<Evaluacion> evals = AppDatabase.getInstance(this)
                        .evaluacionDao()
                        .obtenerPorMateria(i.materia);
                builder.append("📘 ").append(i.materia).append(":\n");
                if (evals.isEmpty()) {
                    builder.append("  Sin evaluaciones programadas.\n");
                } else {
                    for (Evaluacion e : evals) {
                        builder.append("  • ").append(e.tipo)
                                .append(" - ").append(e.descripcion)
                                .append(" (").append(e.fecha).append(")\n");
                    }
                }
                builder.append("\n");
            }

            new AlertDialog.Builder(this)
                    .setTitle("Evaluaciones próximas")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });

        Button btnVerNotas = findViewById(R.id.btVerNotas);

        btnVerNotas.setOnClickListener(v -> {
            List<Calificacion> calificaciones = AppDatabase.getInstance(this)
                    .calificacionDao()
                    .obtenerPorAlumno(usuarioActual);

            if (calificaciones.isEmpty()) {
                Toast.makeText(this, "No tenés notas registradas", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            builder.append("📚 Tus calificaciones:\n\n");

            for (Calificacion c : calificaciones) {
                Double promedio = AppDatabase.getInstance(this)
                        .calificacionDao()
                        .promedioPorMateria(usuarioActual, c.materia);
                builder.append("• ").append(c.materia)
                        .append(": ").append(c.nota)
                        .append(" (Promedio: ").append(String.format("%.2f", promedio))
                        .append(")\n");
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Mis notas y promedios")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });

        Button btnVerAsistencias = findViewById(R.id.btnVerAsistencias);

        btnVerAsistencias.setOnClickListener(v -> {
            List<Asistencia> asistencias = AppDatabase.getInstance(this)
                    .asistenciaDao()
                    .obtenerPorAlumno(usuarioActual);

            if (asistencias.isEmpty()) {
                Toast.makeText(this, "No hay asistencias registradas", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            builder.append("📅 Historial de asistencias:\n\n");

            for (Asistencia a : asistencias) {
                builder.append("• ").append(a.materia)
                        .append(" (").append(a.fecha).append("): ")
                        .append(a.estado).append("\n");
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Mis asistencias")
                    .setMessage(builder.toString())
                    .setPositiveButton("OK", null)
                    .show();
        });


    }

    private void cargarMaterias() {
        materias = materiaDao.obtenerTodas();
        List<String> nombres = new ArrayList<>();

        for (Materia m : materias) {
            nombres.add(m.nombre + " (Prof: " + m.profesor + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listMaterias.setAdapter(adapter);
    }

    private void mostrarDialogoInscripcion(Materia materia) {
        new AlertDialog.Builder(this)
                .setTitle("Inscribirse en " + materia.nombre)
                .setMessage("¿Deseás inscribirte en esta materia?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    inscripcionDao.insertar(new Inscripcion(usuarioActual, materia.nombre));
                    Toast.makeText(this, "Inscripción realizada", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void mostrarInscripciones() {
        List<Inscripcion> inscripciones = inscripcionDao.obtenerPorAlumno(usuarioActual);
        StringBuilder builder = new StringBuilder();

        for (Inscripcion i : inscripciones) {
            builder.append("• ").append(i.materia).append("\n");
        }

        if (inscripciones.isEmpty()) builder.append("No estás inscripto en ninguna materia.");

        new AlertDialog.Builder(this)
                .setTitle("Mis materias")
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }



}