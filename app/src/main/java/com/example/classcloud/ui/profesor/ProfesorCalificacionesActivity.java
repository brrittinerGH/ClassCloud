package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Calificacion;
import com.example.classcloud.data.CalificacionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;

import java.util.List;

public class ProfesorCalificacionesActivity extends AppCompatActivity {

    private EditText etAlumno, etNota;
    private Spinner spMateria;
    private Button btnGuardarNota, btnVerNotas, btnVolver;
    private CalificacionDAO calificacionDao;
    private MateriaDAO materiaDao;

    private String profesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_calificaciones);

        etAlumno = findViewById(R.id.etAlumno);
        etNota = findViewById(R.id.etNota);
        spMateria = findViewById(R.id.spMateria); // Spinner
        btnGuardarNota = findViewById(R.id.btGuardarNota);
        btnVerNotas = findViewById(R.id.btVerNotas);
        btnVolver = findViewById(R.id.btVolver);

        calificacionDao = AppDatabase.getInstance(this).calificacionDao();
        materiaDao = AppDatabase.getInstance(this).materiaDao();

        profesor = getIntent().getStringExtra("PROFESOR");

        List<Materia> materias = materiaDao.obtenerPorProfesor(profesor);
        ArrayAdapter<Materia> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, materias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMateria.setAdapter(adapter);

        btnGuardarNota.setOnClickListener(v -> {
            String alumno = etAlumno.getText().toString().trim();
            Materia materia = (Materia) spMateria.getSelectedItem();
            String notaTexto = etNota.getText().toString().trim();

            if (alumno.isEmpty() || materia == null || notaTexto.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double nota = Double.parseDouble(notaTexto);
            calificacionDao.insertar(new Calificacion(alumno, materia.nombre, nota));
            Toast.makeText(this, "Nota guardada correctamente", Toast.LENGTH_SHORT).show();

            etAlumno.setText("");
            etNota.setText("");
        });

        btnVerNotas.setOnClickListener(v -> {
            List<Calificacion> lista = calificacionDao.obtenerPorAlumno(etAlumno.getText().toString().trim());
            if (lista.isEmpty()) {
                Toast.makeText(this, "No hay notas para este alumno", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            for (Calificacion c : lista) {
                builder.append("• ").append(c.materia)
                        .append(": ").append(c.nota)
                        .append("\n");
            }
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}