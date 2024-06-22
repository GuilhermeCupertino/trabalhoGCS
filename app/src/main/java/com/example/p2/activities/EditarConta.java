package com.example.p2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.p2.R;
import com.example.p2.dao.UsuarioDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Usuario;

public class EditarConta extends AppCompatActivity {

    private Button voltar;
    private Button salvar;
    private TextView nome;
    private TextView telefone;
    private TextView email;
    private TextView senha;
    private int usuarioId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_conta);

        voltar = findViewById(R.id.btn_voltarTelaInicial);
        salvar = findViewById(R.id.btn_salvar);
        nome = findViewById(R.id.txt_editName);
        telefone = findViewById(R.id.txt_editPhone);
        email = findViewById(R.id.txt_editEmail);
        senha = findViewById(R.id.txt_editPassword);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarConta.this, TelaInicial.class));
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarConta();
            }
        });

        // Preenche o TextView com os dados do usuário
        preencherDadosUsuario();
    }

    private void preencherDadosUsuario() {
        // Obtém o usuário do banco de dados pelo ID
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        usuarioId = sharedPreferences.getInt("usuarioId", -1);
        UsuarioDao usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
        Usuario usuario = usuarioDao.getUser(usuarioId);

        // Verifica se o usuário foi encontrado
        if (usuario != null) {
            // Preenche os TextViews com os dados do usuário
            nome.setText(usuario.getNome());
            telefone.setText(usuario.getTelefone());
            email.setText(usuario.getEmail());
            senha.setText(usuario.getSenha());
        } else {
            // Mostra uma mensagem de erro se o usuário não foi encontrado
            Toast.makeText(this, "Erro: usuário não encontrado", Toast.LENGTH_SHORT).show();

            // Volta para a tela de login
            Intent intent = new Intent(EditarConta.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void salvarConta() {
        String novoNome = nome.getText().toString();
        String novoTelefone = telefone.getText().toString();
        String novoEmail = email.getText().toString();
        String novaSenha = senha.getText().toString();

        // Obtém o usuário do banco de dados pelo ID
        UsuarioDao usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
        Usuario usuario = usuarioDao.getUser(usuarioId);

        // Verifica se o usuário foi encontrado
        if (usuario != null) {
            // Verifica se houve alterações nos dados do usuário
            if (!usuario.getNome().equals(novoNome) ||
                    !usuario.getTelefone().equals(novoTelefone) ||
                    !usuario.getEmail().equals(novoEmail) ||
                    !usuario.getSenha().equals(novaSenha)) {

                // Atualiza os dados do usuário no banco de dados
                usuario.setNome(novoNome);
                usuario.setTelefone(novoTelefone);
                usuario.setEmail(novoEmail);
                usuario.setSenha(novaSenha);
                usuarioDao.update(usuario);
                showToast("Dados atualizados com sucesso");

                // Volta para a tela inicial
                Intent intent = new Intent(EditarConta.this, TelaInicial.class);
                startActivity(intent);
                finish();
            } else {
                showToast("Nenhuma alteração realizada");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
