package com.example.classcloud.ui.profesor;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classcloud.R;
import com.example.classcloud.data.AppDatabase;
import com.example.classcloud.data.Evaluacion;
import com.example.classcloud.data.EvaluacionDAO;
import java.util.ArrayList;
import java.util.List;

public class ProfesorVerEvaluacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesor_ver_evaluaciones);

        ListView listView = findViewById(R.id.listEvaluaciones);
        EvaluacionDAO dao = AppDatabase.getInstance(this).evaluacionDao();

        String profesor = getIntent().getStringExtra("nombreProfesor");
        List<Evaluacion> lista = dao.obtenerTodas();
        List<String> datos = new ArrayList<>();

        for (Evaluacion e : lista) {
            datos.add("📘 " + e.materia + "\n" + e.tipo + ": " + e.descripcion + "\n📅 " + e.fecha);
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos));
    }
}
