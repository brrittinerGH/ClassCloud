package com.example.classcloud.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class ModificarMateriaActivity extends AppCompatActivity {

    private ListView listMaterias;
    private Button btVolver;
    private MateriaDAO materiaDao;
    private UsuarioDAO usuarioDao;
    private List<Materia> listaMaterias;
    private ArrayAdapter<String> adapter;
    private List<String> nombresMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_materia);

        listMaterias = findViewById(R.id.listMaterias);
        btVolver = findViewById(R.id.btVolver);

        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        usuarioDao = db.usuarioDao();

        cargarMaterias();

        listMaterias.setOnItemClickListener((parent, view, position, id) -> {
            Materia seleccionada = listaMaterias.get(position);
            mostrarDialogoModificar(seleccionada);
        });

        btVolver.setOnClickListener(v -> finish());
    }

    private void cargarMaterias() {
        listaMaterias = materiaDao.obtenerTodas();
        nombresMaterias = new ArrayList<>();

        for (Materia m : listaMaterias) {
            Usuario profesor = usuarioDao.obtenerPorId(m.profesorId);
            String nombreProfe = (profesor != null) ? profesor.getNombre() : "Sin asignar";
            nombresMaterias.add(m.nombre + " (Profesor/a: " + nombreProfe + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresMaterias);
        listMaterias.setAdapter(adapter);
    }

    private void mostrarDialogoModificar(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar materia: " + materia.nombre);

        // Cargar lista de profesores
        List<Usuario> profesores = usuarioDao.obtenerPorRol("profesor/a");
        List<String> nombresProfesores = new ArrayList<>();
        for (Usuario u : profesores) {
            nombresProfesores.add(u.getNombre());
        }

        // Crear spinner con los profesores
        Spinner spinnerProfes = new Spinner(this);
        ArrayAdapter<String> adapterProfes = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nombresProfesores
        );
        adapterProfes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfes.setAdapter(adapterProfes);

        // Seleccionar el profesor actual en el spinner
        Usuario profesorActual = usuarioDao.obtenerPorId(materia.profesorId);
        if (profesorActual != null) {
            int indexActual = nombresProfesores.indexOf(profesorActual.getNombre());
            if (indexActual >= 0) {
                spinnerProfes.setSelection(indexActual);
            }
        }

        builder.setView(spinnerProfes);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            if (profesores.isEmpty()) {
                Toast.makeText(this, "No hay profesores disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            int nuevoProfeId = profesores.get(spinnerProfes.getSelectedItemPosition()).id;
            materia.profesorId = nuevoProfeId;
            materiaDao.actualizar(materia);

            Toast.makeText(this, "Materia actualizada correctamente", Toast.LENGTH_SHORT).show();
            cargarMaterias();
        });

        builder.setNegativeButton("Eliminar", (dialog, which) -> {
            materiaDao.eliminarPorId(materia.id);
            Toast.makeText(this, "Materia eliminada", Toast.LENGTH_SHORT).show();
            cargarMaterias();
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
