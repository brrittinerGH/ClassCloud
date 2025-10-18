package com.example.classcloud.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Usuario;
import com.example.classcloud.data.UsuarioDAO;
import com.example.classcloud.ui.admin.AdminActivity;
import com.example.classcloud.ui.alumno.AlumnoActivity;
import com.example.classcloud.ui.profesor.ProfeActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login);
        EditText userField = findViewById(R.id.Usuario);
        EditText passField = findViewById(R.id.Contrasenia);

        // Inicializar base de datos
        AppDatabase db = AppDatabase.getInstance(this);
        UsuarioDAO dao = db.usuarioDao();

        // 🔹 Crear usuario admin si no existe
        if (dao.login("admin", "1234") == null) {
            dao.insertar(new Usuario("admin", "1234", "admin"));
        }

        // Acción al presionar el botón
        loginButton.setOnClickListener(v -> {
            String nombreUsuario = userField.getText().toString().trim();
            String contrasenia = passField.getText().toString().trim();

            if (nombreUsuario.isEmpty() || contrasenia.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Buscar usuario en la base de datos
            Usuario usuario = dao.login(nombreUsuario, contrasenia);

            if (usuario != null) {
                // Usuario encontrado
                Toast.makeText(this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();

                switch (usuario.getRol()) {
                    case "admin":
                        startActivity(new Intent(this, AdminActivity.class));
                        break;

                    case "profesor":
                        Intent intentProfesor = new Intent(this, ProfeActivity.class);
                        intentProfesor.putExtra("nombreProfesor", usuario.getNombre());
                        startActivity(intentProfesor);
                        break;

                    case "alumno":
                        Intent intentAlumno = new Intent(this, AlumnoActivity.class);
                        intentAlumno.putExtra("nombreAlumno", usuario.getNombre());
                        startActivity(intentAlumno);
                        break;

                    default:
                        Toast.makeText(this, "Rol desconocido", Toast.LENGTH_SHORT).show();
                        break;
                }

                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
