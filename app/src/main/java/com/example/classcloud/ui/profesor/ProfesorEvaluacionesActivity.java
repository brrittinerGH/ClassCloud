package com.example.classcloud.ui.profesor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Evaluacion;
import com.example.classcloud.data.EvaluacionDAO;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfesorEvaluacionesActivity extends AppCompatActivity {

    private Spinner spinnerMateria, spinnerTipo;
    private TextView tvFechaSeleccionada;
    private EditText etDescripcion;
    private Button btFecha, btGuardarEval, btVolver, btEvaluaciones;

    private MateriaDAO materiaDao;
    private EvaluacionDAO evaluacionDao;
    private List<Materia> materiasProfesor;
    private String fechaSeleccionada = "";

    private int profesorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_evaluaciones);

        //  Inicializar vistas
        spinnerMateria = findViewById(R.id.spinnerMateria);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btFecha = findViewById(R.id.btFecha);
        btGuardarEval = findViewById(R.id.btGuardarEval);
        btEvaluaciones = findViewById(R.id.btVerEval);
        btVolver = findViewById(R.id.btVolver);
        etDescripcion = findViewById(R.id.etDescripcion);

        //  Inicializar base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        evaluacionDao = db.evaluacionDao();

        //  Obtener ID del profesor logueado
        profesorId = getIntent().getIntExtra("idProfesor", -1);
        if (profesorId == -1) {
            Toast.makeText(this, getString(R.string.errorIdProfe), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //  Cargar materias asignadas al profesor
        materiasProfesor = materiaDao.obtenerPorProfesorId(profesorId);

        if (materiasProfesor == null || materiasProfesor.isEmpty()) {
            Toast.makeText(this, getString(R.string.noMateriasAsig), Toast.LENGTH_LONG).show();
        }

        //  Llenar spinner con nombres de materias
        List<String> nombresMaterias = new ArrayList<>();
        for (Materia m : materiasProfesor) {
            nombresMaterias.add(m.nombre);
        }

        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresMaterias
        );
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapterMaterias);

        //  Tipos de evaluación
        String[] tipos = {"Examen", "Trabajo práctico"};
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tipos
        );
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        //  Selector de fecha
        btFecha.setOnClickListener(v -> {
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

        //  Guardar evaluación
        btGuardarEval.setOnClickListener(v -> {
            if (materiasProfesor.isEmpty()) {
                Toast.makeText(this, getString(R.string.noMatsDisp), Toast.LENGTH_SHORT).show();
                return;
            }

            String descripcion = etDescripcion.getText().toString().trim();
            String tipo = spinnerTipo.getSelectedItem().toString();

            if (descripcion.isEmpty()) {
                Toast.makeText(this, getString(R.string.ingresDesc), Toast.LENGTH_SHORT).show();
                return;
            }

            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this,getString(R.string.selecFecha), Toast.LENGTH_SHORT).show();
                return;
            }

            Materia materiaSeleccionada = materiasProfesor.get(spinnerMateria.getSelectedItemPosition());

            Evaluacion nuevaEval = new Evaluacion(
                    materiaSeleccionada.id,   // 🔹 ahora guarda el ID de materia, no su nombre
                    tipo,
                    descripcion,
                    fechaSeleccionada
            );

            evaluacionDao.insertar(nuevaEval);
            Toast.makeText(this, getString(R.string.evalGuardadaCorrect), Toast.LENGTH_SHORT).show();

            etDescripcion.setText("");
            fechaSeleccionada = "";
            tvFechaSeleccionada.setText(getString(R.string.fechaNoSelec));
        });

        btEvaluaciones.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfesorVerEvaluacionesActivity.class);
            intent.putExtra("idProfesor", profesorId);
            startActivity(intent);
        });


        btVolver.setOnClickListener(v -> finish());
    }
}
