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

/**
 * Actividad que permite al administrador crear nuevos usuarios en el sistema,
 * ya sean profesores o alumnos. Contiene campos de texto para el nombre
 * y la contraseña, junto con opciones de rol y botones de acción.
 *
 *
 * @author Lasso,Rittiner,Verrengia
 * @version 1.0
 */
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
        // Inicializa la base de datos y obtiene el DAO correspondiente
        usuarioDao = AppDatabase.getInstance(this).usuarioDao();
        // Listener del botón "Guardar"
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String contrasena = etContrasena.getText().toString().trim();

            //Si los campos estan vacios envia un mensaje
            if (nombre.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, getString(R.string.comCampos), Toast.LENGTH_SHORT).show();
                return;
            }

            // Determinar el rol seleccionado
            String rol = rbProfesor.isChecked() ? "profesor" : "alumno";

            // Verificar si ya existe un usuario con ese nombre
            Usuario existente = usuarioDao.login(nombre, contrasena);
            if (existente != null) {
                Toast.makeText(this, getString(R.string.nomExistente), Toast.LENGTH_SHORT).show();
                return;
            }
            // Inserta el nuevo usuario en la base de datos
            usuarioDao.insertar(new Usuario(nombre, contrasena, rol));
            // Muestra mensaje de confirmación
            String mensaje = getString(R.string.usuarioCreado, rol);
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

            etNombre.setText("");
            etContrasena.setText("");
            rbProfesor.setChecked(true);
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
