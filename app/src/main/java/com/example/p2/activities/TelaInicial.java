package com.example.p2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.p2.R;
import com.example.p2.dao.UsuarioDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Usuario;

public class TelaInicial extends AppCompatActivity {

    private Button gerenciarCidades;
    private Button gerenciarEnderecos;
    private Button sair;
    private Button visualizarUsuarios;
    private ImageButton profile;
    private TextView bemVindo;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicial);

        gerenciarCidades = findViewById(R.id.btn_gerenciarC);
        gerenciarEnderecos = findViewById(R.id.btn_gerenciarE);
        profile = findViewById(R.id.btn_profile);
        bemVindo = findViewById(R.id.txt_bemVindo);
        sair = findViewById(R.id.btn_sair);
        visualizarUsuarios = findViewById(R.id.btn_visualizarU);

        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        usuarioId = sharedPreferences.getInt("usuarioId", -1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gerenciarCidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, GerenciarCidades.class));
            }
        });

        gerenciarEnderecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, GerenciarEndereco.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, EditarConta.class));
            }
        });

        visualizarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TelaInicial.this, VisualizarUsuarios.class));
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpa o SharedPreferences ao sair
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("usuarioId");
                editor.apply();
                startActivity(new Intent(TelaInicial.this, MainActivity.class));
                finish();
            }
        });

        bemVindoUsuario();
    }

    private void bemVindoUsuario() {
        if (usuarioId != -1) {
            // Obtém o nome do usuário logado no banco de dados
            AppDatabase db = AppDatabase.getDatabase(this);
            assert db != null;
            UsuarioDao usuarioDao = db.usuarioDao();
            Usuario usuario = usuarioDao.getUser(usuarioId);

            if (usuario != null) {
                String nomeUsuario = usuario.getNome();
                bemVindo.setText(getString(R.string.bemVindo) + " " + nomeUsuario);
            }
        }
    }
}