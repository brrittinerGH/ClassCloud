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

/**
 * Actividad que permite al administrador modificar o eliminar materias existentes.
 * Muestra una lista de materias con su respectivo profesor asignado,
 * permitiendo cambiar el profesor o eliminar la materia mediante un diálogo.
 *
 *  Desde esta pantalla el usuario puede:
 *
 *     Visualizar todas las materias registradas
 *     Modificar el profesor asignado a una materia
 *     Eliminar materias existentes
 *     Volver al menú principal
 *
 * @author Lasso,Rittiner,Verrengia
 * @version 1.0
 */
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

        // Inicialización de base de datos y DAOs
        AppDatabase db = AppDatabase.getInstance(this);
        materiaDao = db.materiaDao();
        usuarioDao = db.usuarioDao();

        // Carga inicial de las materias en la lista
        cargarMaterias();

        // Al seleccionar una materia, se abre el diálogo para modificar o eliminar
        listMaterias.setOnItemClickListener((parent, view, position, id) -> {
            Materia seleccionada = listaMaterias.get(position);
            mostrarDialogoModificar(seleccionada);
        });
        // Acción del botón "Volver": cierra la actividad
        btVolver.setOnClickListener(v -> finish());
    }


    /**
     * Carga todas las materias desde la base de datos y las muestra en el ListView.
     * También muestra el nombre del profesor asignado a cada materia.
     */
    private void cargarMaterias() {
        listaMaterias = materiaDao.obtenerTodas();
        nombresMaterias = new ArrayList<>();
        // Construye la lista con el nombre de la materia y su profesor asignado
        for (Materia m : listaMaterias) {
            Usuario profesor = usuarioDao.obtenerPorId(m.profesorId);
            String nombreProfe = (profesor != null) ? profesor.getNombre() : "Sin asignar";
            nombresMaterias.add(getString(R.string.materia_con_profesor, m.nombre, nombreProfe));
        }
        // Configura el adaptador para mostrar los datos en la lista
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresMaterias);
        listMaterias.setAdapter(adapter);
    }


    /**
     * Muestra un diálogo para modificar o eliminar la materia seleccionada.
     * Permite cambiar el profesor asignado o eliminar la materia completamente.
     *
     * @param materia La materia seleccionada que se desea modificar o eliminar.
     */
    private void mostrarDialogoModificar(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.titulo_modificar_materia, materia.nombre));

        // Cargar lista de profesores
        List<Usuario> profesores = usuarioDao.obtenerPorRol("profesor");
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
        // Botón "Guardar" → actualiza el profesor asignado
        builder.setPositiveButton(getString(R.string.btGuardar), (dialog, which) -> {
            if (profesores.isEmpty()) {
                Toast.makeText(this, getString(R.string.noProfesDis), Toast.LENGTH_SHORT).show();
                return;
            }

            int nuevoProfeId = profesores.get(spinnerProfes.getSelectedItemPosition()).id;
            materia.profesorId = nuevoProfeId;
            materiaDao.actualizar(materia);

            Toast.makeText(this, getString(R.string.materiaAct), Toast.LENGTH_SHORT).show();
            cargarMaterias();
        });
        // Botón "Eliminar" → elimina la materia seleccionada
        builder.setNegativeButton(getString(R.string.eliminar), (dialog, which) -> {
            materiaDao.eliminarPorId(materia.id);
            Toast.makeText(this, getString(R.string.materiaEli), Toast.LENGTH_SHORT).show();
            cargarMaterias();
        });
        // Botón "Cancelar" → cierra el diálogo sin realizar cambios
        builder.setNeutralButton(getString(R.string.cancelar), (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
