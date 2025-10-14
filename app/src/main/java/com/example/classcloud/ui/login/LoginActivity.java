package com.example.classcloud.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classcloud.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Conectamos los elementos del XML
        Button loginButton = findViewById(R.id.login);
        EditText userField = findViewById(R.id.Usuario);
        EditText passField = findViewById(R.id.Contrasenia);

        // Acción al presionar el botón
        loginButton.setOnClickListener(v -> {
            String usuario = userField.getText().toString().trim();
            String contrasena = passField.getText().toString().trim();

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                Toast.makeText(this, "Inicio como administrador", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminActivity.class));
            } else if (usuario.equals("profe") && contrasena.equals("1234")) {
                Toast.makeText(this, "Inicio como profesor", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProfesorActivity.class));
            } else if (usuario.equals("alumno") && contrasena.equals("1234")) {
                Toast.makeText(this, "Inicio como alumno", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AlumnoActivity.class));
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




