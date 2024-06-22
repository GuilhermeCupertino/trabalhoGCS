package com.example.p2.activities;

import android.content.Intent;
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

public class CadastrarUsuario extends AppCompatActivity {

    private Button voltar;
    private Button cadastrar;
    private TextView nameField;
    private TextView phoneField;
    private TextView emailField;
    private TextView passwordField;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_usuario);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();


        cadastrar = findViewById(R.id.btn_cadastrar);
        voltar = findViewById(R.id.btn_voltarLogin);
        nameField = findViewById(R.id.txt_name);
        phoneField = findViewById(R.id.txt_phone);
        emailField = findViewById(R.id.txt_email);
        passwordField = findViewById(R.id.txt_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CadastrarUsuario.this, MainActivity.class));
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarUser();
            }
        });
    }

    private void cadastrarUser() {
        String nome = nameField.getText().toString();
        String telefone = phoneField.getText().toString();
        String senha = passwordField.getText().toString();
        String email = emailField.getText().toString();

        // Verificar se os campos estão preenchidos
        if (nome.isEmpty() || telefone.isEmpty() || senha.isEmpty() || email.isEmpty()) {
            Toast.makeText(CadastrarUsuario.this, "Por favor, preencha todos os campos para completar o cadastro.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se o email possui um formato válido usando regex
        if (!isValidEmail(email)) {
            Toast.makeText(CadastrarUsuario.this, "Por favor, insira um endereço de e-mail válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se o email já está em uso
        UsuarioDao usuarioDao = db.usuarioDao();
        Usuario existingUser = usuarioDao.getUserByEmail(email);
        if (existingUser != null) {
            // Se o email já estiver em uso, exibir uma mensagem e retornar
            Toast.makeText(CadastrarUsuario.this, "Este e-mail já está em uso. Realize login ou utilize outro e-mail.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar um novo objeto Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setTelefone(telefone);
        novoUsuario.setSenha(senha);
        novoUsuario.setEmail(email);

        // Inserir o novo usuário no banco de dados
        usuarioDao.insertAll(novoUsuario);

        // Mostrar uma mensagem de sucesso e voltar para a tela de login
        Toast.makeText(CadastrarUsuario.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CadastrarUsuario.this, MainActivity.class));
    }

    // Função para validar o formato do endereço de e-mail usando regex
    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}