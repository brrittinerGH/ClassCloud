package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Calificacion;
import com.example.classcloud.data.CalificacionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

import java.util.ArrayList;
import java.util.List;

public class ProfesorVerCalificacionesActivity extends AppCompatActivity {

    private ListView listCalificaciones;
    private Button btnVolver;
    private CalificacionDAO calificacionDao;
    private MateriaDAO materiaDao;
    private UsuarioDAO usuarioDao;
    private int profesorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_ver_calificaciones);

        listCalificaciones = findViewById(R.id.listCalificaciones);
        btnVolver = findViewById(R.id.btnVolver);

        AppDatabase db = AppDatabase.getInstance(this);
        calificacionDao = db.calificacionDao();
        materiaDao = db.materiaDao();
        usuarioDao = db.usuarioDao();

        profesorId = getIntent().getIntExtra("idProfesor", -1);
        if (profesorId == -1) {
            Toast.makeText(this, getString(R.string.errorIdProfe), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mostrarCalificaciones();

        btnVolver.setOnClickListener(v -> finish());
    }

    private void mostrarCalificaciones() {
        List<Materia> materiasProfesor = materiaDao.obtenerPorProfesorId(profesorId);
        List<String> datos = new ArrayList<>();

        for (Materia m : materiasProfesor) {
            List<Calificacion> calificaciones = calificacionDao.obtenerPorMateria(m.id);
            if (calificaciones.isEmpty()) continue;

            datos.add("📘 " + m.nombre + ":");

            for (Calificacion c : calificaciones) {
                Usuario alumno = usuarioDao.obtenerPorId(c.alumnoId);
                String nombreAlumno = (alumno != null) ? alumno.getNombre() : getString(R.string.alumDesconocido);
                datos.add("   • " + nombreAlumno+getString(R.string.nota) + c.nota);
            }

            datos.add(""); // espacio entre materias
        }

        if (datos.isEmpty()) {
            datos.add(getString(R.string.noCalReg));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, datos);
        listCalificaciones.setAdapter(adapter);
    }
}
