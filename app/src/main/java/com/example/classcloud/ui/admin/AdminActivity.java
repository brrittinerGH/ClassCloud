package com.example.classcloud.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classcloud.R;
import android.content.Intent;
import com.example.classcloud.ui.login.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btCrearMateria = findViewById(R.id.btCrearMateria);
        Button btModificarMaterias = findViewById(R.id.btModificarMaterias);
        Button btVolver = findViewById(R.id.btVolver);

        // Ir a pantalla para crear materias
        btCrearMateria.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearMateriaActivity.class);
            startActivity(intent);
        });

        // Ir a pantalla para modificar materias
        btModificarMaterias.setOnClickListener(v -> {
            Intent intent = new Intent(this, ModificarMateriaActivity.class);
            startActivity(intent);
        });

        // Volver al login
        btVolver.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnAgregarUsuario = findViewById(R.id.btAgregarProfesor);

        btnAgregarUsuario.setText("Agregar Usuario");

        btnAgregarUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearUsuarioActivity.class));
        });

    }
}


