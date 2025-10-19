package com.example.classcloud.ui.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

import java.util.ArrayList;
import java.util.List;

public class CrearMateriaActivity extends AppCompatActivity {

    private EditText nombreMateria, profesorMateria;
    private Button btGuardar, btVolver;
    private MateriaDAO materiaDao;

    Spinner spinnerProfesores;
    List<Usuario> listaProfesores;
    UsuarioDAO usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_materia);

        EditText etNombreMateria = findViewById(R.id.nombreMateria);
        Button btnGuardar = findViewById(R.id.btGuardarMateria);
        spinnerProfesores = findViewById(R.id.spinnerProfesor);

        AppDatabase db = AppDatabase.getInstance(this);
        usuarioDao = db.usuarioDao();

        // 🔹 Cargar solo los usuarios con rol profesor
        listaProfesores = usuarioDao.obtenerPorRol("profesor");

        List<String> nombres = new ArrayList<>();
        for (Usuario u : listaProfesores) {
            nombres.add(u.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfesores.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            String nombreMateria = etNombreMateria.getText().toString().trim();
            if (nombreMateria.isEmpty()) {
                Toast.makeText(this, "Ingrese el nombre de la materia", Toast.LENGTH_SHORT).show();
                return;
            }

            String profesorSeleccionado = listaProfesores.get(spinnerProfesores.getSelectedItemPosition()).getNombre();
            db.materiaDao().insertar(new Materia(nombreMateria, profesorSeleccionado));

            Toast.makeText(this, "Materia creada correctamente", Toast.LENGTH_SHORT).show();
            etNombreMateria.setText("");
        });

        btVolver.setOnClickListener(v -> finish());
    }
}
