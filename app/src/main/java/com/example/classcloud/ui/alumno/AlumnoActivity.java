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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Actividad principal del alumno.
 * Permite ver las materias disponibles, inscribirse, consultar inscripciones,
 * ver evaluaciones, notas y asistencias.
 *
 * Esta clase se comunica con la base de datos Room mediante los DAO correspondientes.
 *
 * @author Lasso,Rittiner,Verrengia
 * @version 1.0
 *
 */
public class AlumnoActivity extends AppCompatActivity {

    private ListView listMaterias;
    private Button btnVerInscripciones, btnVerEvaluaciones, btnVerNotas, btnVerAsistencias, btnVolver;

    private MateriaDAO materiaDao;
    private InscripcionDAO inscripcionDao;

    private List<Materia> materias;
    private ArrayAdapter<String> adapter;

    private int idAlumno;
    private String nombreAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno);

        // Vistas
        listMaterias = findViewById(R.id.listMaterias);
        btnVerInscripciones = findViewById(R.id.btVerInscripciones);
        btnVerEvaluaciones = findViewById(R.id.btVerEvaluaciones);
        btnVerNotas = findViewById(R.id.btVerNotas);
        btnVerAsistencias = findViewById(R.id.btnVerAsistencias);
        btnVolver = findViewById(R.id.btVolver);

        // Base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        inscripcionDao = db.inscripcionDao();

        //  Recuperar datos del login
        idAlumno = getIntent().getIntExtra("idAlumno", -1);
        nombreAlumno = getIntent().getStringExtra("nombreAlumno");

        // Verificar si se obtuvo correctamente el ID del alumno
        if (idAlumno == -1) {
            Toast.makeText(this, getString(R.string.errorIdAlumno), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Cargar materias disponibles en la lista
        cargarMaterias();

        //  Inscribirse en una materia
        listMaterias.setOnItemClickListener((parent, view, position, id) -> {
            Materia seleccionada = materias.get(position);
            mostrarDialogoInscripcion(seleccionada);
        });

        //  Ver inscripciones
        btnVerInscripciones.setOnClickListener(v -> mostrarInscripciones());

        //  Ver evaluaciones de las materias inscriptas
        btnVerEvaluaciones.setOnClickListener(v -> mostrarEvaluaciones());

        //  Ver notas
        btnVerNotas.setOnClickListener(v -> mostrarNotas());

        //  Ver asistencias
        btnVerAsistencias.setOnClickListener(v -> mostrarAsistencias());

        //  Volver al login
        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Carga todas las materias disponibles desde la base de datos y las muestra en la lista.
     */
    private void cargarMaterias() {
        materias = materiaDao.obtenerTodas();
        List<String> nombres = new ArrayList<>();

        for (Materia m : materias) {
            nombres.add(m.nombre);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombres);
        listMaterias.setAdapter(adapter);
    }

    /**
     * Muestra un diálogo para inscribirse en una materia.
     * @param materia Materia seleccionada por el alumno.
     */
    private void mostrarDialogoInscripcion(Materia materia) {
        // Verificar si el alumno ya está inscripto en la materia
        Inscripcion existente = inscripcionDao.obtenerPorAlumnoYMateria(idAlumno, materia.id);

        if (existente != null) {
            Toast.makeText(this, getString(R.string.yaInscripto), Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el diálogo de confirmación
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.inscEn) + materia.nombre)
                .setMessage(getString(R.string.deseaIns))
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Registrar nueva inscripción en la base de datos
                    inscripcionDao.insertar(new Inscripcion(idAlumno, materia.id));
                    Toast.makeText(this, getString(R.string.inscRealizada), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss())
                .show();
    }


    /**
     * Muestra todas las materias en las que el alumno está inscripto.
     */
    private void mostrarInscripciones() {
        List<Inscripcion> inscripciones = inscripcionDao.obtenerPorAlumno(idAlumno);
        StringBuilder builder = new StringBuilder();

        if (inscripciones.isEmpty()) {
            builder.append(getString(R.string.noInscripto));
        } else {
            for (Inscripcion i : inscripciones) {
                Materia materia = materiaDao.obtenerPorId(i.materiaId);
                if (materia != null) {
                    builder.append("• ").append(materia.nombre).append("\n");
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.matInscriptas))
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Muestra las evaluaciones programadas de las materias en las que el alumno está inscripto.
     */
    private void mostrarEvaluaciones() {
        List<Inscripcion> inscripciones = inscripcionDao.obtenerPorAlumno(idAlumno);
        StringBuilder builder = new StringBuilder();

        for (Inscripcion insc : inscripciones) {
            Materia materia = materiaDao.obtenerPorId(insc.materiaId);
            if (materia != null) {
                builder.append("📘 ").append(materia.nombre).append(":\n");
                List<Evaluacion> evals = AppDatabase.getInstance(this)
                        .evaluacionDao()
                        .obtenerPorMateria(materia.id);
                if (evals.isEmpty()) {
                    builder.append(getString(R.string.sinEvalsProg)+"\n");
                } else {
                    for (Evaluacion e : evals) {
                        builder.append("  • ").append(e.tipo)
                                .append(" - ").append(e.descripcion)
                                .append(" (").append(e.fecha).append(")\n");
                    }
                }
                builder.append("\n");
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.evalsProx))
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }


    /**
     * Muestra todas las calificaciones y promedios del alumno por materia.
     */
    private void mostrarNotas() {
        List<Calificacion> calificaciones = AppDatabase.getInstance(this)
                .calificacionDao()
                .obtenerPorAlumno(idAlumno);

        if (calificaciones.isEmpty()) {
            Toast.makeText(this, getString(R.string.noNotasReg), Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.tusNotas)+"\n\n");


        Set<Integer> materiasProcesadas = new HashSet<>();

        for (Calificacion c : calificaciones) {
            if (!materiasProcesadas.contains(c.materiaId)) {
                Materia materia = AppDatabase.getInstance(this)
                        .materiaDao()
                        .obtenerPorId(c.materiaId);

                if (materia != null) {
                    Double promedio = AppDatabase.getInstance(this)
                            .calificacionDao()
                            .promedioPorMateria(idAlumno, materia.id);

                    builder.append("• ")
                            .append(materia.nombre)
                            .append(getString(R.string.prom))
                            .append(String.format("%.2f", promedio))
                            .append(")\n");

                    // Obtener todas las notas de esa materia
                    List<Calificacion> notasMateria = AppDatabase.getInstance(this)
                            .calificacionDao()
                            .obtenerNotasMateria(idAlumno, materia.id);

                    for (Calificacion nota : notasMateria) {
                        builder.append(getString(R.string.nota)).append(nota.nota).append("\n");
                    }

                    builder.append("\n"); // salto entre materias
                    materiasProcesadas.add(c.materiaId);
                }
            }
        }



        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.notYProm))
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Muestra el historial de asistencias del alumno.
     */
    private void mostrarAsistencias() {
        List<Asistencia> asistencias = AppDatabase.getInstance(this)
                .asistenciaDao()
                .obtenerPorAlumno(idAlumno);

        if (asistencias.isEmpty()) {
            Toast.makeText(this, getString(R.string.noAsistReg), Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.histAsist)+"\n\n");

        for (Asistencia a : asistencias) {
            Materia materia = materiaDao.obtenerPorId(a.materiaId);
            if (materia != null) {
                builder.append("• ").append(materia.nombre)
                        .append(" (").append(a.fecha).append("): ")
                        .append(a.estado).append("\n");
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.misAsist))
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
