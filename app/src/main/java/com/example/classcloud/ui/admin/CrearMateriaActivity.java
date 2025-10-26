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

/**
 * Actividad que permite al administrador crear una nueva materia y asignarle un profesor.
 * Muestra un campo para ingresar el nombre de la materia y un spinner con la lista de profesores disponibles.
 *
 *
 * @author Lasso,Rittiner,Verrengia
 * @version 1.0
 */
public class CrearMateriaActivity extends AppCompatActivity {

    private EditText nombreMateria;
    private Button btGuardar, btVolver;
    private Spinner spinnerProfesores;

    private MateriaDAO materiaDao;
    private UsuarioDAO usuarioDao;
    private List<Usuario> listaProfesores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_materia);


        nombreMateria = findViewById(R.id.nombreMateria);
        btGuardar = findViewById(R.id.btGuardarMateria);
        btVolver = findViewById(R.id.btVolver);
        spinnerProfesores = findViewById(R.id.spinnerProfesor);

        // Inicialización de la base de datos y los DAOs
        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        usuarioDao = db.usuarioDao();

        // Obtiene la lista de profesores registrados
        listaProfesores = usuarioDao.obtenerPorRol("profesor");
        // Si no hay profesores, se muestra un mensaje de advertencia
        if (listaProfesores.isEmpty()) {
            Toast.makeText(this, getString(R.string.noprofes), Toast.LENGTH_LONG).show();
        }
        // Crea una lista con los nombres de los profesores para mostrar en el spinner
        List<String> nombres = new ArrayList<>();
        for (Usuario u : listaProfesores) {
            nombres.add(u.getNombre());
        }

        // Configura el adaptador del spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombres
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfesores.setAdapter(adapter);

        // Acción del botón Guardar: valida datos e inserta la materia en la base
        btGuardar.setOnClickListener(v -> {
            try {
                String nombre = nombreMateria.getText().toString().trim();
                if (nombre.isEmpty()) {
                    Toast.makeText(this, getString(R.string.mtrNombre), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listaProfesores.isEmpty()) {
                    Toast.makeText(this, getString(R.string.noprofes), Toast.LENGTH_SHORT).show();
                    return;
                }


                int profesorId = listaProfesores.get(spinnerProfesores.getSelectedItemPosition()).id;


                Materia nuevaMateria = new Materia(nombre, profesorId);
                materiaDao.insertar(nuevaMateria);

                Toast.makeText(this, getString(R.string.mtrCreadaCo), Toast.LENGTH_SHORT).show();
                nombreMateria.setText("");

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.mtrError) + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btVolver.setOnClickListener(v -> finish());
    }
}
