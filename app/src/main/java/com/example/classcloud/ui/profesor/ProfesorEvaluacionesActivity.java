package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Evaluacion;
import com.example.classcloud.data.EvaluacionDAO;

import java.util.List;

public class ProfesorEvaluacionesActivity extends AppCompatActivity {

    private EditText etMateria, etTipo, etDescripcion, etFecha;
    private Button btGuardarEval, btVerEval, btVolver;
    private EvaluacionDAO evaluacionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_evaluaciones);

        etMateria = findViewById(R.id.etMateria);
        etTipo = findViewById(R.id.etTipo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etFecha = findViewById(R.id.etFecha);
        btGuardarEval = findViewById(R.id.btGuardarEval);
        btVerEval = findViewById(R.id.btVerEval);
        btVolver = findViewById(R.id.btVolver);

        evaluacionDao = AppDatabase.getInstance(this).evaluacionDao();

        btGuardarEval.setOnClickListener(v -> {
            String materia = etMateria.getText().toString().trim();
            String tipo = etTipo.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String fecha = etFecha.getText().toString().trim();

            if (materia.isEmpty() || tipo.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            evaluacionDao.insertar(new Evaluacion(materia, tipo, descripcion, fecha));
            Toast.makeText(this, "Evaluación guardada correctamente", Toast.LENGTH_SHORT).show();

            etMateria.setText("");
            etTipo.setText("");
            etDescripcion.setText("");
            etFecha.setText("");
        });

        btVerEval.setOnClickListener(v -> {
            List<Evaluacion> lista = evaluacionDao.obtenerPorMateria(etMateria.getText().toString().trim());
            if (lista.isEmpty()) {
                Toast.makeText(this, "No hay evaluaciones para esta materia", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder builder = new StringBuilder();
            for (Evaluacion e : lista) {
                builder.append("• ").append(e.tipo)
                        .append(" - ").append(e.descripcion)
                        .append(" (").append(e.fecha).append(")\n");
            }
            Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        });

        btVolver.setOnClickListener(v -> finish());
    }
}
