package com.example.classcloud.ui.profesor;

import android.content.Intent;
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
import com.example.classcloud.data.Inscripcion;
import com.example.classcloud.data.InscripcionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

import java.util.ArrayList;
import java.util.List;

public class ProfesorCalificacionesActivity extends AppCompatActivity {

    private Spinner spinnerMateria, spinnerAlumno;
    private EditText etNota;
    private Button btGuardar, btVerNotas, btVolver;

    private MateriaDAO materiaDao;
    private InscripcionDAO inscripcionDao;
    private CalificacionDAO calificacionDao;
    private UsuarioDAO usuarioDao;

    private List<Materia> materiasProfesor;
    private List<Usuario> alumnosInscriptos;

    private int profesorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_calificaciones);

        spinnerMateria = findViewById(R.id.spMateria);
        spinnerAlumno = findViewById(R.id.spinnerAlumno);
        etNota = findViewById(R.id.etNota);
        btGuardar = findViewById(R.id.btGuardarNota);
        btVerNotas = findViewById(R.id.btVerNotas); // 🔹 nuevo botón
        btVolver = findViewById(R.id.btVolver);

        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        inscripcionDao = db.inscripcionDao();
        calificacionDao = db.calificacionDao();
        usuarioDao = db.usuarioDao();

        // 🔹 Recuperar el ID del profesor desde el intent
        profesorId = getIntent().getIntExtra("idProfesor", -1);
        if (profesorId == -1) {
            Toast.makeText(this, getString(R.string.errorIdProfe), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 🔹 Cargar materias del profesor
        materiasProfesor = materiaDao.obtenerPorProfesorId(profesorId);
        List<String> nombresMaterias = new ArrayList<>();
        for (Materia m : materiasProfesor) {
            nombresMaterias.add(m.nombre);
        }

        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresMaterias
        );
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapterMaterias);

        // 🔹 Cuando se selecciona una materia, mostrar los alumnos inscriptos
        spinnerMateria.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (materiasProfesor.isEmpty()) return;
                Materia materiaSeleccionada = materiasProfesor.get(position);
                cargarAlumnosInscriptos(materiaSeleccionada.id);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // 🔹 Guardar la nota
        btGuardar.setOnClickListener(v -> {
            if (alumnosInscriptos == null || alumnosInscriptos.isEmpty()) {
                Toast.makeText(this, getString(R.string.noAlumnosIns), Toast.LENGTH_SHORT).show();
                return;
            }

            String notaStr = etNota.getText().toString().trim();
            if (notaStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.ingNota), Toast.LENGTH_SHORT).show();
                return;
            }

            double nota;
            try {
                nota = Double.parseDouble(notaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, getString(R.string.ingNumValido), Toast.LENGTH_SHORT).show();
                return;
            }

            int materiaId = materiasProfesor.get(spinnerMateria.getSelectedItemPosition()).id;
            int alumnoId = alumnosInscriptos.get(spinnerAlumno.getSelectedItemPosition()).id;

            Calificacion nueva = new Calificacion(alumnoId, materiaId, nota);
            calificacionDao.insertar(nueva);

            Toast.makeText(this, getString(R.string.calGuardada), Toast.LENGTH_SHORT).show();
            etNota.setText("");
        });

        // 🔹 Abrir pantalla “Ver Calificaciones”
        btVerNotas.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfesorVerCalificacionesActivity.class);
            intent.putExtra("idProfesor", profesorId);
            startActivity(intent);
        });

        // 🔹 Volver
        btVolver.setOnClickListener(v -> finish());
    }

    private void cargarAlumnosInscriptos(int materiaId) {
        List<Inscripcion> inscripciones = inscripcionDao.obtenerTodas();
        alumnosInscriptos = new ArrayList<>();

        for (Inscripcion ins : inscripciones) {
            if (ins.materiaId == materiaId) {
                Usuario alumno = usuarioDao.obtenerPorId(ins.alumnoId);
                if (alumno != null) {
                    alumnosInscriptos.add(alumno);
                }
            }
        }

        List<String> nombresAlumnos = new ArrayList<>();
        for (Usuario u : alumnosInscriptos) {
            nombresAlumnos.add(u.getNombre());
        }

        ArrayAdapter<String> adapterAlumnos = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresAlumnos
        );
        adapterAlumnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlumno.setAdapter(adapterAlumnos);
    }
}
