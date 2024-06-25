package com.example.p2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_conta);

        initViews();
        initDatabase();
        applyInsets();

        voltar.setOnClickListener(v -> startActivity(new Intent(EditarConta.this, TelaInicial.class)));
        salvar.setOnClickListener(v -> salvarConta());

        preencherDadosUsuario();
    }

    private void initViews() {
        voltar = findViewById(R.id.btn_voltarTelaInicial);
        salvar = findViewById(R.id.btn_salvar);
        nome = findViewById(R.id.txt_editName);
        telefone = findViewById(R.id.txt_editPhone);
        email = findViewById(R.id.txt_editEmail);
        senha = findViewById(R.id.txt_editPassword);
    }

    private void initDatabase() {
        usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
    }

    private void applyInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void preencherDadosUsuario() {
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        usuarioId = sharedPreferences.getInt("usuarioId", -1);

        new FetchUserTask().execute(usuarioId);
    }

    private void salvarConta() {
        String novoNome = nome.getText().toString().trim();
        String novoTelefone = telefone.getText().toString().trim();
        String novoEmail = email.getText().toString().trim();
        String novaSenha = senha.getText().toString().trim();

        if (validarEntradas(novoNome, novoTelefone, novoEmail, novaSenha)) {
            new UpdateUserTask(novoNome, novoTelefone, novoEmail, novaSenha).execute(usuarioId);
        } else {
            showToast("Por favor, preencha todos os campos corretamente.");
        }
    }

    private boolean validarEntradas(String nome, String telefone, String email, String senha) {
        return !nome.isEmpty() && !telefone.isEmpty() && !email.isEmpty() && !senha.isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class FetchUserTask extends AsyncTask<Integer, Void, Usuario> {
        @Override
        protected Usuario doInBackground(Integer... integers) {
            int userId = integers[0];
            return usuarioDao.getUser(userId);
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null) {
                nome.setText(usuario.getNome());
                telefone.setText(usuario.getTelefone());
                email.setText(usuario.getEmail());
                senha.setText(usuario.getSenha());
            } else {
                showToast("Erro: usuário não encontrado");
                startActivity(new Intent(EditarConta.this, MainActivity.class));
                finish();
            }
        }
    }

    private class UpdateUserTask extends AsyncTask<Integer, Void, Boolean> {
        private String novoNome, novoTelefone, novoEmail, novaSenha;

        UpdateUserTask(String novoNome, String novoTelefone, String novoEmail, String novaSenha) {
            this.novoNome = novoNome;
            this.novoTelefone = novoTelefone;
            this.novoEmail = novoEmail;
            this.novaSenha = novaSenha;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int userId = integers[0];
            Usuario usuario = usuarioDao.getUser(userId);

            if (usuario != null) {
                if (!usuario.getNome().equals(novoNome) || 
                    !usuario.getTelefone().equals(novoTelefone) || 
                    !usuario.getEmail().equals(novoEmail) || 
                    !usuario.getSenha().equals(novaSenha)) {
                    
                    usuario.setNome(novoNome);
                    usuario.setTelefone(novoTelefone);
                    usuario.setEmail(novoEmail);
                    usuario.setSenha(novaSenha);  // Note: You should hash the password before storing

                    usuarioDao.update(usuario);
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                showToast("Dados atualizados com sucesso");
                startActivity(new Intent(EditarConta.this, TelaInicial.class));
                finish();
            } else {
                showToast("Nenhuma alteração realizada");
            }
        }
    }
}
