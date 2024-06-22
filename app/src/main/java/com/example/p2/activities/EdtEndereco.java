package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.dao.CidadeDao;
import com.example.p2.dao.EnderecoDao;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;
import com.example.p2.entities.Endereco;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class EdtEndereco extends AppCompatActivity {

    private Button voltar;
    private Button salvar;
    private Button excluir;
    private AppDatabase db;
    private Spinner cidades;
    private List<Cidade> listaDeCidades;
    private ArrayAdapter<Cidade> cidadesAdapter;
    private int enderecoId;
    private TextView descricaotxt;
    private TextView lontxt;
    private TextView lattxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edt_endereco);

        voltar = findViewById(R.id.voltarnv);
        excluir = findViewById(R.id.btn_excluirE);
        salvar = findViewById(R.id.btn_salvarE);
        cidades = findViewById(R.id.cidadesSpinner);
        descricaotxt = findViewById(R.id.txt_descricao);
        lontxt = findViewById(R.id.txt_long);
        lattxt = findViewById(R.id.txt_lat);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        carregarCidades();

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EdtEndereco.this, GerenciarEndereco.class);
                startActivity(intent);
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarAlteracoes();
                Intent intent = new Intent(EdtEndereco.this, MapaEndereco.class);
                startActivity(intent);
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirEndereco();
            }
        });

        preencherDadosEndereco();

    }

    private void carregarCidades() {
        listaDeCidades = db.cidadeDao().getAll(); // Busca as cidades do banco
        cidadesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDeCidades);
        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cidades.setAdapter(cidadesAdapter);
    }

    private void salvarAlteracoes() {
        // Obtém o DAO do endereço do banco de dados
        EnderecoDao enderecoDao = AppDatabase.getDatabase(this).enderecoDao();

        // Obtém o endereço com o ID fornecido
        Endereco endereco = enderecoDao.getEndereco(enderecoId);

        if (endereco != null) {
            // Atualiza os dados do endereço
            endereco.setDescricao(descricaotxt.getText().toString());
            endereco.setLongitude(Double.parseDouble(lontxt.getText().toString()));
            endereco.setLatitude(Double.parseDouble(lattxt.getText().toString()));

            // Obtém a cidade selecionada no Spinner
            Cidade cidadeSelecionada = (Cidade) cidades.getSelectedItem();

            // Define a cidade associada ao endereço
            endereco.setCidadeId(cidadeSelecionada.getCidadeId());

            // Atualiza o endereço no banco de dados
            enderecoDao.update(endereco);

            Toast.makeText(this, "Alterações salvas com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro: Endereço não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirEndereco() {
        // Obtém o endereco do banco de dados pelo ID
        EnderecoDao enderecoDao = AppDatabase.getDatabase(this).enderecoDao();

        // Obtém o endereco do banco de dados com o ID fornecido
        Endereco endereco = enderecoDao.getEndereco(enderecoId);

        // Verifica se o endereco foi encontrada
        if (endereco != null) {
            // Exclui o endereco do banco de dados
            enderecoDao.delete(endereco);
            Toast.makeText(this, "Endereço excluído com sucesso", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(EdtEndereco.this, GerenciarEndereco.class));
            finish();
        } else {
            Toast.makeText(this, "Erro: endereco não encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void preencherDadosEndereco() {
        SharedPreferences sharedPreferences = getSharedPreferences("locPref", MODE_PRIVATE);
        enderecoId = sharedPreferences.getInt("idEnd", -1);
            
        EnderecoDao enderecoDao = AppDatabase.getDatabase(this).enderecoDao();
        Endereco endereco = enderecoDao.getEndereco(enderecoId);
        
        // Verifica se o endereço foi encontrado
        if (endereco != null) {
            // Define a descrição, longitude e latitude do endereço nos TextViews
            descricaotxt.setText(endereco.getDescricao());
            lontxt.setText(String.valueOf(endereco.getLongitude()));
            lattxt.setText(String.valueOf(endereco.getLatitude()));

            // Obtém o ID da cidade associada ao endereço
            long cidadeId = endereco.getCidadeId();

            // Define a seleção do Spinner com base no ID da cidade
            for (int i = 0; i < listaDeCidades.size(); i++) {
                if (listaDeCidades.get(i).getCidadeId() == cidadeId) {
                    cidades.setSelection(i);
                    break;
                }
            }
        } else {
            Toast.makeText(this, "Erro: Endereço não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
