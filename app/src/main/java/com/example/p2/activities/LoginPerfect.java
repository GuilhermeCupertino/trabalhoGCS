package com.example.p2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.p2.R;
import com.example.p2.dao.UsuarioDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Usuario;

public class LoginPerfect extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button login;
    private Button registerButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.btn_cadastrarLogin);

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastrarUsuario.class));
            }
        });
    }

    private void login() {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        AppDatabase db = AppDatabase.getDatabase(this);
        assert db != null;
        UsuarioDao usuarioDao = db.usuarioDao();
        Usuario usuario = usuarioDao.getUsuario(username, password);

        if (usuario != null) {
            // Usuário autenticado com sucesso
            // Salvar o ID do usuário no SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("usuarioId", usuario.getId());
            editor.apply();

            startActivity(new Intent(this, TelaInicial.class));
            finish();
        } else {
            // Falha na autenticação
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_LONG).show();
        }
    }
}
