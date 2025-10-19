package com.example.classcloud.ui.profesor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
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
    private Button btnFecha, btnGuardarEval, btnVerEval, btnVolver;
    private EditText etDescripcion;

    private MateriaDAO materiaDao;
    private EvaluacionDAO evaluacionDao;
    private List<Materia> materiasProfesor;
    private String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_evaluaciones);

        // Inicializar vistas
        spinnerMateria = findViewById(R.id.spinnerMateria);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnFecha = findViewById(R.id.btFecha);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btnGuardarEval = findViewById(R.id.btGuardarEval);
        btnVerEval = findViewById(R.id.btVerEval);
        btnVolver = findViewById(R.id.btVolver);
        etDescripcion = findViewById(R.id.etDescripcion);

        // Base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        evaluacionDao = db.evaluacionDao();

        // Profesor logueado recibido por Intent y limpiado de espacios
        String profesor = getIntent().getStringExtra("nombreProfesor");
        if (profesor != null) {
            profesor = profesor.trim();
        } else {
            profesor = "";
        }

        // Obtener materias del profesor
        materiasProfesor = materiaDao.obtenerPorProfesor(profesor);

        // --- Si no hay materias, agregamos algunas de prueba ---
        if (materiasProfesor.isEmpty()) {
            materiasProfesor = new ArrayList<>();
            materiasProfesor.add(new Materia("Matemática", profesor));
            materiasProfesor.add(new Materia("Historia", profesor));
            materiasProfesor.add(new Materia("Lengua", profesor));
            Toast.makeText(this, "No había materias en la base. Se cargaron materias de prueba", Toast.LENGTH_LONG).show();
        }

        // Debug: mostrar materias
        Toast.makeText(this, "Materias encontradas: " + materiasProfesor.size(), Toast.LENGTH_LONG).show();
        for (Materia m : materiasProfesor) {
            Log.d("DEBUG", "Materia: " + m.nombre + ", Profesor: " + m.profesor);
        }

        // Preparar Spinner de materias
        List<String> nombresMaterias = new ArrayList<>();
        for (Materia m : materiasProfesor) {
            nombresMaterias.add(m.nombre);
        }

        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresMaterias
        );
        adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMateria.setAdapter(adapterMaterias);

        // Spinner de tipo de evaluación
        String[] tipos = {"Examen", "Trabajo práctico"};
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tipos
        );
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);

        // Selección de fecha
        btnFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvFechaSeleccionada.setText("Fecha: " + fechaSeleccionada);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Guardar evaluación
        btnGuardarEval.setOnClickListener(v -> {
            try {
                if (fechaSeleccionada.isEmpty()) {
                    Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (materiasProfesor.isEmpty()) {
                    Toast.makeText(this, "No hay materias disponibles para este profesor", Toast.LENGTH_SHORT).show();
                    return;
                }

                String materia = materiasProfesor.get(spinnerMateria.getSelectedItemPosition()).nombre;
                String tipo = spinnerTipo.getSelectedItem().toString();
                String descripcion = etDescripcion.getText().toString().trim();

                if (descripcion.isEmpty()) {
                    Toast.makeText(this, "Ingrese una descripción", Toast.LENGTH_SHORT).show();
                    return;
                }

                evaluacionDao.insertar(new Evaluacion(materia, tipo, descripcion, fechaSeleccionada));
                Toast.makeText(this, "Evaluación guardada correctamente", Toast.LENGTH_SHORT).show();

                etDescripcion.setText("");
                tvFechaSeleccionada.setText("Fecha no seleccionada");
                fechaSeleccionada = "";

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar la evaluación: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Volver
        btnVolver.setOnClickListener(v -> finish());

        // Ver evaluaciones (en desarrollo)
        btnVerEval.setOnClickListener(v ->
                Toast.makeText(this, "Abrir lista de evaluaciones (en desarrollo)", Toast.LENGTH_SHORT).show()
        );
    }
}
