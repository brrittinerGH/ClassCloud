package com.example.classcloud.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;

public class CrearMateriaActivity extends AppCompatActivity {

    private EditText nombreMateria, profesorMateria;
    private Button btGuardar, btVolver;
    private MateriaDAO materiaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_materia);

        nombreMateria = findViewById(R.id.nombreMateria);
        profesorMateria = findViewById(R.id.profesorMateria);
        btGuardar = findViewById(R.id.btGuardarMateria);
        btVolver = findViewById(R.id.btVolver);

        materiaDao = AppDatabase.getInstance(this).materiaDao();

        btGuardar.setOnClickListener(v -> {
            String nombre = nombreMateria.getText().toString().trim();
            String profesor = profesorMateria.getText().toString().trim();

            if (nombre.isEmpty() || profesor.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            materiaDao.insertar(new Materia(nombre, profesor));
            Toast.makeText(this, "Materia creada correctamente", Toast.LENGTH_SHORT).show();

            nombreMateria.setText("");
            profesorMateria.setText("");
        });

        btVolver.setOnClickListener(v -> finish());
    }
}
