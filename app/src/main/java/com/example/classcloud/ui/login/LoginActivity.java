package com.example.classcloud.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classcloud.AdminActivity;
import com.example.classcloud.AlumnoActivity;
import com.example.classcloud.ProfeActivity;
import com.example.classcloud.R;

import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Conectamos los elementos del XML
        Button loginButton = findViewById(R.id.login);
        EditText userField = findViewById(R.id.Usuario);
        EditText passField = findViewById(R.id.Contrasenia);

        // Inicializamos la base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        UsuarioDAO dao = db.usuarioDao();

        // Insertamos usuarios iniciales si la tabla está vacía
        if (dao.login("admin", "1234") == null) {
            dao.insertar(new Usuario("admin", "1234", "admin"));
            dao.insertar(new Usuario("profe", "1234", "profe"));
            dao.insertar(new Usuario("alumno", "1234", "alumno"));
        }

        // Acción al presionar el botón
        loginButton.setOnClickListener(v -> {
            String nombreUsuario = userField.getText().toString().trim();
            String contrasenia = passField.getText().toString().trim();

            if (nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validamos el usuario con Room
            Usuario usuario = dao.login(nombreUsuario, contrasenia);

            if (usuario != null) {
                Toast.makeText(this, "Bienvenido " + usuario.getRol(), Toast.LENGTH_SHORT).show();

                if (usuario.getRol().equals("admin")) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else if (usuario.getRol().equals("profe")) {
                    startActivity(new Intent(this, ProfeActivity.class));
                } else if (usuario.getRol().equals("alumno")) {
                    startActivity(new Intent(this, AlumnoActivity.class));
                }

                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}




