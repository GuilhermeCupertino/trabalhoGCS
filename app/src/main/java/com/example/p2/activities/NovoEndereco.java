package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;
import com.example.p2.entities.Endereco;

import java.util.List;

public class NovoEndereco extends AppCompatActivity {
    private Button voltar;
    private TextView descricao;
    private TextView latitude;
    private TextView longitude;
    private Spinner cidades;
    private AppDatabase db;
    private Button salvarEndereco;
    private List<Cidade> listaDeCidades;
    private ArrayAdapter<Cidade> cidadesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_endereco);

        voltar = findViewById(R.id.voltarnv);
        cidades = findViewById(R.id.cidadesSpinner);
        descricao = findViewById(R.id.desc);
        latitude = findViewById(R.id.lat);
        longitude = findViewById(R.id.lon);
        salvarEndereco = findViewById(R.id.salvarNovoEnd);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        carregarCidades();

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovoEndereco.this, GerenciarEndereco.class);
                startActivity(intent);
            }
        });

        salvarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarEndereco();
                Intent intent = new Intent(NovoEndereco.this, GerenciarEndereco.class);
                startActivity(intent);
            }
        });
    }

    private void carregarCidades() {
        listaDeCidades = db.cidadeDao().getAll(); // Busca as cidades do banco
        cidadesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDeCidades);
        cidadesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cidades.setAdapter(cidadesAdapter);
    }

    private void salvarEndereco() {
        String novaDescricao = descricao.getText().toString();
        String novaLatitude = latitude.getText().toString();
        String novaLongitude = longitude.getText().toString();
        Cidade cidadeSelecionada = (Cidade) cidades.getSelectedItem();

        if (cidadeSelecionada == null) {
            Toast.makeText(this, "Selecione uma cidade.", Toast.LENGTH_SHORT).show();
            return;
        }

        Endereco novoEndereco = new Endereco();
        novoEndereco.setDescricao(novaDescricao);
        novoEndereco.setLatitude(Double.parseDouble(novaLatitude));
        novoEndereco.setLongitude(Double.parseDouble(novaLongitude));
        novoEndereco.setCidadeId(cidadeSelecionada.getCidadeId());
        db.enderecoDao().insert(novoEndereco);

        Toast.makeText(this, "Endereco salvo com sucesso!", Toast.LENGTH_SHORT).show();
    }

    }


