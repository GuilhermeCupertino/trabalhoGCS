package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.dao.CidadeDao;
import com.example.p2.dao.EnderecoDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;
import com.example.p2.entities.Endereco;

public class EdtCidade extends AppCompatActivity {

    private Button voltar;
    private Button salvar;
    private Button excluir;
    private TextView cidadetxt;
    private TextView estadotxt;
    private long cidadeId;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_cidade);

        voltar = findViewById(R.id.voltarnv);
        cidadetxt = findViewById(R.id.txt_descricao);
        estadotxt = findViewById(R.id.txt_lat);
        excluir = findViewById(R.id.btn_excluir);
        salvar = findViewById(R.id.btn_salvarE);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EdtCidade.this, GerenciarCidades.class));
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarAlteracoes();
                startActivity(new Intent(EdtCidade.this, GerenciarCidades.class));
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirCidade();
            }
        });

        preencherDadosCidade();
    }

    private void salvarAlteracoes() {
        // Obtém a cidade do banco de dados pelo ID
        CidadeDao cidadeDao = AppDatabase.getDatabase(this).cidadeDao();

        int cidadeIdInt = (int) cidadeId;

        // Obtém a cidade do banco de dados com o ID fornecido
        Cidade cidade = cidadeDao.getCidade(cidadeIdInt);

        if (cidade != null) {
            // Atualiza os dados da cidade
            cidade.setNomeCidade(cidadetxt.getText().toString());
            cidade.setEstado(estadotxt.getText().toString());

            // Atualiza a cidade no banco de dados
            cidadeDao.update(cidade);

            Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro: cidade não encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirCidade() {
        // Obtém a cidade do banco de dados pelo ID
        CidadeDao cidadeDao = AppDatabase.getDatabase(this).cidadeDao();

        // Convertendo o long para int
        int cidadeIdInt = (int) cidadeId;

        // Obtém a cidade do banco de dados com o ID fornecido
        Cidade cidade = cidadeDao.getCidade(cidadeIdInt);

        // Verifica se a cidade foi encontrada
        if (cidade != null) {
            // Exclui a cidade do banco de dados
            cidadeDao.delete(cidade);
            Toast.makeText(this, "Cidade excluída com sucesso", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(EdtCidade.this, GerenciarCidades.class));
            finish();
        } else {
            Toast.makeText(this, "Erro: cidade não encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void preencherDadosCidade() {
        SharedPreferences sharedPreferences = getSharedPreferences("CidadePref", MODE_PRIVATE);
        cidadeId = sharedPreferences.getInt("cidadeId", -1);
        CidadeDao cidadeDao = AppDatabase.getDatabase(this).cidadeDao();
        int cidadeIdInt = (int) cidadeId;
        Cidade cidade = cidadeDao.getCidade(cidadeIdInt);

        if (cidade != null) {
            cidadetxt.setText(cidade.getNomeCidade());
            estadotxt.setText(cidade.getEstado());
        } else {
            Toast.makeText(this, "Erro: Cidade não encontrada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}