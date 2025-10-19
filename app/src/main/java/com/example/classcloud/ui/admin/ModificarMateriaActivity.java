package com.example.classcloud.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
            nombresMaterias.add(m.nombre + " (Profesor: " + m.profesor + ")");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresMaterias);
        listMaterias.setAdapter(adapter);
    }

    private void mostrarDialogoModificar(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar materia");

        final Spinner spinnerProfesores = new Spinner(this);

        List<Usuario> listaProfesores = usuarioDao.obtenerPorRol("profesor");
        List<String> nombresProfesores = new ArrayList<>();

        for (Usuario p : listaProfesores) {
            nombresProfesores.add(p.getNombre());
        }

        ArrayAdapter<String> adapterProfesores = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombresProfesores
        );
        adapterProfesores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfesores.setAdapter(adapterProfesores);

        int indexActual = nombresProfesores.indexOf(materia.profesor);
        if (indexActual >= 0) {
            spinnerProfesores.setSelection(indexActual);
        }

        builder.setView(spinnerProfesores);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            if (listaProfesores.isEmpty()) {
                Toast.makeText(this, "No hay profesores disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            String nuevoProfesor = listaProfesores.get(spinnerProfesores.getSelectedItemPosition()).getNombre();
            materia.profesor = nuevoProfesor;
            materiaDao.actualizar(materia);

            Toast.makeText(this, "Profesor actualizado correctamente", Toast.LENGTH_SHORT).show();
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
