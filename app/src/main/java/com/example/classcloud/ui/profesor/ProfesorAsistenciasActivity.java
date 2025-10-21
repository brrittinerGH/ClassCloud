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
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;

import java.util.List;

public class ProfesorAsistenciasActivity extends AppCompatActivity {

    private EditText etAlumno, etMateria, etFecha, etEstado;
    private Button btnGuardarAsistencia, btnVerAsistencias, btnVolver;
    private AsistenciaDAO asistenciaDao;
    private UsuarioDAO usuarioDao;
    private MateriaDAO materiaDao;

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
        btnVolver = findViewById(R.id.btVolver);

        asistenciaDao = AppDatabase.getInstance(this).asistenciaDao();
        usuarioDao = AppDatabase.getInstance(this).usuarioDao();
        materiaDao = AppDatabase.getInstance(this).materiaDao();

        btnGuardarAsistencia.setOnClickListener(v -> {
            String alumnoStr = etAlumno.getText().toString().trim();
            String materiaStr = etMateria.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();
            String estado = etEstado.getText().toString().trim();

            if (alumnoStr.isEmpty() || materiaStr.isEmpty() || fecha.isEmpty() || estado.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int alumnoId = Integer.parseInt(alumnoStr);
                int materiaId = Integer.parseInt(materiaStr);

                asistenciaDao.insertar(new Asistencia(alumnoId, materiaId, fecha, estado));
                Toast.makeText(this, "Asistencia registrada correctamente", Toast.LENGTH_SHORT).show();

                etAlumno.setText("");
                etMateria.setText("");
                etFecha.setText("");
                etEstado.setText("");

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Alumno y Materia deben ser números", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerAsistencias.setOnClickListener(v -> {
            String materiaStr = etMateria.getText().toString().trim();
            if (materiaStr.isEmpty()) {
                Toast.makeText(this, "Ingrese el ID de la materia", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int materiaId = Integer.parseInt(materiaStr);
                List<Asistencia> lista = asistenciaDao.obtenerPorMateria(materiaId);

                if (lista.isEmpty()) {
                    Toast.makeText(this, "No hay asistencias para esta materia", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (Asistencia a : lista) {
                    Usuario alumno = usuarioDao.obtenerPorId(a.alumnoId);
                    Materia materia = materiaDao.obtenerPorId(a.materiaId);

                    String nombreAlumno = (alumno != null) ? alumno.getNombre() : "Alumno desconocido";
                    String nombreMateria = (materia != null) ? materia.nombre : "Materia desconocida";

                    builder.append("• ").append(a.fecha)
                            .append(" - ").append(nombreAlumno)
                            .append(" (").append(nombreMateria).append(")")
                            .append(": ").append(a.estado)
                            .append("\n");
                }

                Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Materia debe ser un número", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
