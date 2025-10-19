package com.example.classcloud.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;

public class CrearUsuarioActivity extends AppCompatActivity {

    private EditText etNombre, etContrasena;
    private RadioGroup radioGroupRol;
    private RadioButton rbProfesor, rbAlumno;
    private Button btnGuardar, btnVolver;
    private UsuarioDAO usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        etNombre = findViewById(R.id.etNombre);
        etContrasena = findViewById(R.id.etContrasena);
        radioGroupRol = findViewById(R.id.radioGroupRol);
        rbProfesor = findViewById(R.id.rbProfesor);
        rbAlumno = findViewById(R.id.rbAlumno);
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

            // Determinar el rol seleccionado
            String rol = rbProfesor.isChecked() ? "profesor" : "alumno";

            // Verificar si ya existe un usuario con ese nombre
            Usuario existente = usuarioDao.login(nombre, contrasena);
            if (existente != null) {
                Toast.makeText(this, "Ya existe un usuario con ese nombre", Toast.LENGTH_SHORT).show();
                return;
            }

            usuarioDao.insertar(new Usuario(nombre, contrasena, rol));
            Toast.makeText(this, "Usuario (" + rol + ") creado correctamente", Toast.LENGTH_SHORT).show();

            etNombre.setText("");
            etContrasena.setText("");
            rbProfesor.setChecked(true);
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
