package com.example.classcloud.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

public class CrearProfesorActivity extends AppCompatActivity {

    private EditText etNombre, etContrasena;
    private Button btnGuardar, btnVolver;
    private UsuarioDAO usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_profesor);

        etNombre = findViewById(R.id.etNombre);
        etContrasena = findViewById(R.id.etContrasena);
        btnGuardar = findViewById(R.id.btGuardar);
        btnVolver = findViewById(R.id.btVolver);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario nuevo = new Usuario(nombre, contrasena, "profesor");
            usuarioDao.insertar(nuevo);
            Toast.makeText(this, "Profesor agregado correctamente", Toast.LENGTH_SHORT).show();

            etNombre.setText("");
            etContrasena.setText("");
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
