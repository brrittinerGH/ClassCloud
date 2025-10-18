package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Asistencia;
import com.example.classcloud.data.AsistenciaDAO;

import java.util.List;

public class ProfesorAsistencias extends AppCompatActivity {

    private EditText etAlumno, etMateria, etFecha, etEstado;
    private Button btnGuardarAsistencia, btnVerAsistencias, btnVolver;
    private AsistenciaDAO asistenciaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_asistencias);

        etAlumno = findViewById(R.id.etAlumno);
        etMateria = findViewById(R.id.etMateria);
        etFecha = findViewById(R.id.etFecha);
        etEstado = findViewById(R.id.etEstado);
        btnGuardarAsistencia = findViewById(R.id.btnGuardarAsistencia);
        btnVerAsistencias = findViewById(R.id.btnVerAsistencias);
        btnVolver = findViewById(R.id.btnVolver);

        asistenciaDao = AppDatabase.getInstance(this).asistenciaDao();

        btnGuardarAsistencia.setOnClickListener(v -> {
            String alumno = etAlumno.getText().toString().trim();
            String materia = etMateria.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
            String estado = etEstado.getText().toString().trim();

            if (alumno.isEmpty() || materia.isEmpty() || fecha.isEmpty() || estado.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            asistenciaDao.insertar(new Asistencia(alumno, materia, fecha, estado));
            Toast.makeText(this, "Asistencia registrada correctamente", Toast.LENGTH_SHORT).show();

            etAlumno.setText("");
            etMateria.setText("");
            etFecha.setText("");
            etEstado.setText("");
        });

        btnVerAsistencias.setOnClickListener(v -> {
            List<Asistencia> lista = asistenciaDao.obtenerPorMateria(etMateria.getText().toString().trim());
            if (lista.isEmpty()) {
                Toast.makeText(this, "No hay asistencias para esta materia", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            for (Asistencia a : lista) {
                builder.append("• ").append(a.fecha)
                        .append(" - ").append(a.alumno)
                        .append(": ").append(a.estado)
                        .append("\n");
            }

            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
