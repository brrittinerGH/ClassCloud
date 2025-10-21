package com.example.classcloud.ui.profesor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.ui.login.LoginActivity;

import java.util.List;

public class ProfeActivity extends AppCompatActivity {

    private Button btnVerMaterias, btnEvaluaciones, btnCalificaciones, btnAsistencias, btnVolver;
    private TextView tvTitulo;
    private MateriaDAO materiaDao;

    private int profesorId;          // 🔹 ID del profesor logueado
    private String nombreProfesor;   // 🔹 Nombre solo para mostrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profe);

        // Inicializar vistas
        tvTitulo = findViewById(R.id.tvTitulo);
        btnVerMaterias = findViewById(R.id.btVerMaterias);
        btnEvaluaciones = findViewById(R.id.btEvaluaciones);
        btnCalificaciones = findViewById(R.id.btCalificaciones);
        btnAsistencias = findViewById(R.id.btAsistencias);
        btnVolver = findViewById(R.id.btVolver);

        // Inicializar base de datos
        materiaDao = AppDatabase.getInstance(this).materiaDao();

        // Obtener datos del profesor logueado
        profesorId = getIntent().getIntExtra("idProfesor", -1);
        nombreProfesor = getIntent().getStringExtra("nombreProfesor");

        if (profesorId == -1) {
            Toast.makeText(this, "Error: no se pudo identificar al profesor", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvTitulo.setText("Bienvenido, " + nombreProfesor);

        // 🔹 Ver materias asignadas
        btnVerMaterias.setOnClickListener(v -> mostrarMateriasAsignadas());

        // 🔹 Crear o ver evaluaciones
        btnEvaluaciones.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfesorEvaluacionesActivity.class);
            intent.putExtra("idProfesor", profesorId);
            intent.putExtra("nombreProfesor", nombreProfesor);
            startActivity(intent);
        });

        // 🔹 Cargar calificaciones
        btnCalificaciones.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfesorCalificacionesActivity.class);
            intent.putExtra("idProfesor", profesorId);
            startActivity(intent);
        });

        // 🔹 Controlar asistencias
        btnAsistencias.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfesorAsistenciasActivity.class);
            intent.putExtra("idProfesor", profesorId);
            startActivity(intent);
        });

        // 🔹 Volver al login
        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    private void mostrarMateriasAsignadas() {
        List<Materia> materias = materiaDao.obtenerPorProfesorId(profesorId);

        StringBuilder builder = new StringBuilder();
        for (Materia m : materias) {
            builder.append("• ").append(m.nombre).append("\n");
        }

        if (builder.length() == 0) {
            builder.append("No tenés materias asignadas.");
        }

        new AlertDialog.Builder(this)
                .setTitle("Mis materias")
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
