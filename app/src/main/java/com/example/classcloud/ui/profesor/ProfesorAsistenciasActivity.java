package com.example.classcloud.ui.profesor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Asistencia;
import com.example.classcloud.data.AsistenciaDAO;
import com.example.classcloud.data.InscripcionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfesorAsistenciasActivity extends AppCompatActivity {

    private Spinner spinnerMateria, spinnerAlumno, spinnerEstado;
    private TextView tvFechaSeleccionada;
    private Button btnFecha, btnGuardarAsistencia, btnVerAsistencias, btnVolver;

    private AsistenciaDAO asistenciaDao;
    private UsuarioDAO usuarioDao;
    private MateriaDAO materiaDao;
    private InscripcionDAO inscripcionDao;

    private List<Materia> materiasProfesor;
    private List<Usuario> alumnosMateria;
    private String fechaSeleccionada = "";

    private int profesorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_asistencias);

        spinnerMateria = findViewById(R.id.spinnerMateria);
        spinnerAlumno = findViewById(R.id.spinnerAlumno);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btnFecha = findViewById(R.id.btFecha);
        btnGuardarAsistencia = findViewById(R.id.btnGuardarAsistencia);
        btnVerAsistencias = findViewById(R.id.btnVerAsistencias);
        btnVolver = findViewById(R.id.btVolver);

        //  Inicializar DAOs
        AppDatabase db = AppDatabase.getInstance(this);
        asistenciaDao = db.asistenciaDao();
        usuarioDao = db.usuarioDao();
        materiaDao = db.materiaDao();
        inscripcionDao = db.inscripcionDao();

        //  Obtener el ID del profesor logueado
        profesorId = getIntent().getIntExtra("idProfesor", -1);
        if (profesorId == -1) {
            Toast.makeText(this, getString(R.string.errorIdProfe), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //  Cargar materias del profesor
        materiasProfesor = materiaDao.obtenerPorProfesorId(profesorId);
        List<String> nombresMaterias = new ArrayList<>();
        for (Materia m : materiasProfesor) nombresMaterias.add(m.nombre);

        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombresMaterias);
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapterMaterias);

        //  Cargar estados posibles
        String[] estados = {getString(R.string.presente), getString(R.string.ausente), getString(R.string.tarde)};
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, estados);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstados);

        //  Al seleccionar materia, cargar alumnos inscriptos
        spinnerMateria.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (materiasProfesor.isEmpty()) return;

                Materia materiaSeleccionada = materiasProfesor.get(position);
                List<Integer> idsAlumnos = inscripcionDao.obtenerAlumnosPorMateria(materiaSeleccionada.id);
                alumnosMateria = new ArrayList<>();

                List<String> nombresAlumnos = new ArrayList<>();
                for (int alumnoId : idsAlumnos) {
                    Usuario u = usuarioDao.obtenerPorId(alumnoId);
                    if (u != null) {
                        alumnosMateria.add(u);
                        nombresAlumnos.add(u.getNombre());
                    }
                }

                ArrayAdapter<String> adapterAlumnos = new ArrayAdapter<>(ProfesorAsistenciasActivity.this,
                        android.R.layout.simple_spinner_item, nombresAlumnos);
                adapterAlumnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAlumno.setAdapter(adapterAlumnos);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        //  Selector de fecha con calendario
        btnFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvFechaSeleccionada.setText(getString(R.string.fecha,fechaSeleccionada));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        //  Guardar asistencia
        btnGuardarAsistencia.setOnClickListener(v -> {
            if (materiasProfesor.isEmpty() || alumnosMateria == null || alumnosMateria.isEmpty()) {
                Toast.makeText(this, getString(R.string.selecMatYalum), Toast.LENGTH_SHORT).show();
                return;
            }

            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, getString(R.string.selecFecha), Toast.LENGTH_SHORT).show();
                return;
            }

            int materiaId = materiasProfesor.get(spinnerMateria.getSelectedItemPosition()).id;
            int alumnoId = alumnosMateria.get(spinnerAlumno.getSelectedItemPosition()).getId();
            String estado = spinnerEstado.getSelectedItem().toString();

            asistenciaDao.insertar(new Asistencia(alumnoId, materiaId, fechaSeleccionada, estado));
            Toast.makeText(this, getString(R.string.asisRegCorrect), Toast.LENGTH_SHORT).show();

            tvFechaSeleccionada.setText(getString(R.string.fechaNoSelec));
            fechaSeleccionada = "";
        });

        //  Ver asistencias (en AlertDialog)
        btnVerAsistencias.setOnClickListener(v -> {
            if (materiasProfesor.isEmpty()) {
                Toast.makeText(this, getString(R.string.noMateriasAsig), Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            for (Materia m : materiasProfesor) {
                List<Asistencia> asistencias = asistenciaDao.obtenerPorMateria(m.id);
                if (asistencias.isEmpty()) continue;

                builder.append("📘 ").append(m.nombre).append(":\n");
                for (Asistencia a : asistencias) {
                    Usuario alumno = usuarioDao.obtenerPorId(a.alumnoId);
                    String nombreAlumno = (alumno != null) ? alumno.getNombre() : getString(R.string.alumDesconocido);
                    builder.append("  • ").append(a.fecha)
                            .append(" - ").append(nombreAlumno)
                            .append(": ").append(a.estado)
                            .append("\n");
                }
                builder.append("\n");
            }

            if (builder.length() == 0)
                builder.append(getString(R.string.noAsistReg));

            TextView textView = new TextView(this);
            textView.setText(builder.toString());
            textView.setPadding(30, 30, 30, 30);
            textView.setTextSize(16f);
            textView.setScrollBarStyle(android.view.View.SCROLLBARS_INSIDE_INSET);

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.asisReg))
                    .setView(textView)
                    .setPositiveButton(getString(R.string.cerrar), null)
                    .show();
        });

        //  Volver
        btnVolver.setOnClickListener(v -> finish());
    }
}
