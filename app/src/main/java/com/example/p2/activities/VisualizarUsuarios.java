package com.example.p2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.p2.R;
import com.example.p2.adapters.UsuarioAdapter;
import com.example.p2.dao.UsuarioDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Usuario;

import java.util.List;

public class VisualizarUsuarios extends AppCompatActivity {

    private ListView listViewUsuarios;
    private UsuarioAdapter adapter;
    private AppDatabase db;
    private Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_usuarios);

        listViewUsuarios = findViewById(R.id.lista_user);
        voltar = findViewById(R.id.voltarUsuario);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // Recuperar a lista de usuários do banco de dados
        UsuarioDao usuarioDao = db.usuarioDao();
        List<Usuario> usuario = usuario.getAll();

        // Configurar o adaptador personalizado
        adapter = new UsuarioAdapter(this, usuarios);
        listViewUsuarios.setAdapter(adapter);

        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisualizarUsuarios.this, TelaInicial.class));

            }
        });
    }
}