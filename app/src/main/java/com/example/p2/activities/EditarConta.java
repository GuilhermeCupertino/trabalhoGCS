
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

    private Button voltar, salvar, deletar;
    private TextView nome, telefone, email, senha;
    private int usuarioId;
    private SharedPreferences sharedPreferences;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        deletar = findViewById(R.id.btn_deletar); // Referência ao botão de deletar
        nome = findViewById(R.id.txt_editName);
        telefone = findViewById(R.id.txt_editPhone);
        email = findViewById(R.id.txt_editEmail);
        senha = findViewById(R.id.txt_editPassword);
    }

        voltar.setOnClickListener(v -> startActivity(new Intent(EditarConta.this, TelaInicial.class)));
        salvar.setOnClickListener(v -> salvarConta());
        deletar.setOnClickListener(v -> confirmarDelecao()); // Adiciona um ouvinte de cliques ao botão de deletar

        preencherDadosUsuario();
    }

    private void confirmarDelecao() {
        new AlertDialog.Builder(this)
            .setTitle("Confirmar Deleção")
            .setMessage("Você tem certeza que deseja deletar sua conta? Esta ação é irreversível.")
            .setPositiveButton("Deletar", (dialog, which) -> deletarConta())
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void deletarConta() {
        UsuarioDao usuarioDao = AppDatabase.getDatabase(this).usuarioDao();
        Usuario usuario = usuarioDao.getUser(usuarioId);
        if (usuario != null) {
            usuarioDao.delete(usuario);
            showToast("Conta deletada com sucesso.");

            // Volta para a tela de login
            Intent intent = new Intent(EditarConta.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            showToast("Erro: usuário não encontrado");

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
