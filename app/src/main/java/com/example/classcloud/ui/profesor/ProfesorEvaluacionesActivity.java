package com.example.classcloud.ui.profesor;

import android.app.DatePickerDialog;
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

        // 🔹 Vincular vistas del XML
        spinnerMateria = findViewById(R.id.spinnerMateria);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        btnFecha = findViewById(R.id.btFecha);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btnGuardarEval = findViewById(R.id.btGuardarEval);
        btnVerEval = findViewById(R.id.btVerEval);
        btnVolver = findViewById(R.id.btVolver);
        etDescripcion = findViewById(R.id.etDescripcion);


        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        evaluacionDao = db.evaluacionDao();


        String profesor = getIntent().getStringExtra("nombreProfesor");


        materiasProfesor = materiaDao.obtenerPorProfesor(profesor);

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


        String[] tipos = {"Examen", "Trabajo práctico"};
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                tipos
        );
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipos);


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


        btnGuardarEval.setOnClickListener(v -> {
            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
                return;
            }

            if (materiasProfesor.isEmpty()) {
                Toast.makeText(this, "No hay materias disponibles", Toast.LENGTH_SHORT).show();
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
        });

        btnVolver.setOnClickListener(v -> finish());

        btnVerEval.setOnClickListener(v -> {
            Toast.makeText(this, "Abrir lista de evaluaciones (en desarrollo)", Toast.LENGTH_SHORT).show();
        });
    }
}
