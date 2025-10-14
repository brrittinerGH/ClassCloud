package com.example.classcloud.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Materia;
import com.example.classcloud.data.MateriaDAO;


import java.util.ArrayList;
import java.util.List;

public class ModificarMateriaActivity extends AppCompatActivity {

    private ListView listMaterias;
    private Button btVolver;
    private MateriaDAO materiaDao;
    private List<Materia> listaMaterias;
    private ArrayAdapter<String> adapter;
    private List<String> nombresMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_materia);

        listMaterias = findViewById(R.id.listMaterias);
        btVolver = findViewById(R.id.btVolver);

        materiaDao = AppDatabase.getInstance(this).materiaDao();

        cargarMaterias();

        // Acción al hacer clic en una materia
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

        final EditText input = new EditText(this);
        input.setHint("Nuevo profesor para " + materia.nombre);
        input.setText(materia.profesor);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoProfesor = input.getText().toString().trim();
            if (nuevoProfesor.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            materia.profesor = nuevoProfesor;
            materiaDao.actualizar(materia);
            Toast.makeText(this, "Materia actualizada", Toast.LENGTH_SHORT).show();
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
