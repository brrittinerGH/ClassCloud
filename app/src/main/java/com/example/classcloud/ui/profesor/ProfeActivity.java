package com.example.classcloud.ui.profesor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
    private String profesorActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profe);

        tvTitulo = findViewById(R.id.tvTitulo);
        btnVerMaterias = findViewById(R.id.btnVerMaterias);
        btnEvaluaciones = findViewById(R.id.btnEvaluaciones);
        btnCalificaciones = findViewById(R.id.btnCalificaciones);
        btnAsistencias = findViewById(R.id.btnAsistencias);
        btnVolver = findViewById(R.id.btVolver);

        materiaDao = AppDatabase.getInstance(this).materiaDao();


        btnVerMaterias.setOnClickListener(v -> mostrarMateriasAsignadas());


        btnEvaluaciones.setOnClickListener(v ->
                startActivity(new Intent(this, ProfesorEvaluacionesActivity.class)));


        btnCalificaciones.setOnClickListener(v ->
                startActivity(new Intent(this, ProfesorCalificacionesActivity.class)));


        btnAsistencias.setOnClickListener(v ->
                startActivity(new Intent(this, ProfesorAsistenciasActivity.class)));


        btnVolver.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        profesorActual = getIntent().getStringExtra("nombreProfesor");

    }

    private void mostrarMateriasAsignadas() {
        List<Materia> materias = materiaDao.obtenerTodas();
        StringBuilder builder = new StringBuilder();
        for (Materia m : materias) {
            if (m.profesor.equals(profesorActual)) {
                builder.append("• ").append(m.nombre).append("\n");
            }
        }

        if (builder.length() == 0) builder.append("No tenés materias asignadas.");

        new AlertDialog.Builder(this)
                .setTitle("Mis materias")
                .setMessage(builder.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
