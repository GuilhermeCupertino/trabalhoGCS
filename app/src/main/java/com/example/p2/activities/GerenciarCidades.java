package com.example.p2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.p2.R;
import com.example.p2.adapters.CidadeAdapter;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class GerenciarCidades extends AppCompatActivity {
    private ListView listViewCidades;
    private CidadeAdapter cidadeAdapter;
    private AppDatabase db;
    private Button voltar;
    private Button novaCidade;
    private Button buscar;
    private EditText buscarCid;
    private List<Cidade> cidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_cidades);

        listViewCidades = findViewById(R.id.lista_end);
        voltar = findViewById(R.id.voltarCidade);
        novaCidade = findViewById(R.id.nova);
        buscar = findViewById(R.id.botaoBuscar_cid);
        buscarCid = findViewById(R.id.buscarCid);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        cidades = db.cidadeDao().getAll();

        Collections.sort(cidades, new Comparator<Cidade>() {
            @Override
            public int compare(Cidade cidade1, Cidade cidade2) {
                return cidade1.getNomeCidade().compareToIgnoreCase(cidade2.getNomeCidade());
            }
        });

        cidadeAdapter = new CidadeAdapter(this, cidades);
        listViewCidades.setAdapter(cidadeAdapter);

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GerenciarCidades.this, TelaInicial.class));
            }
        });

        novaCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GerenciarCidades.this, NovaCidade.class));
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarCidades();
            }
        });

    }

    private void buscarCidades() {
        String textoBusca = buscarCid.getText().toString().trim();
        List<Cidade> cidadesFiltradas = new ArrayList<>();

        for (Cidade cidade : cidades) {
            if (removerAcentos(cidade.getNomeCidade().toLowerCase()).contains(removerAcentos(textoBusca.toLowerCase()))) {
                cidadesFiltradas.add(cidade);
            }
        }

        cidadeAdapter = new CidadeAdapter(this, cidadesFiltradas);
        listViewCidades.setAdapter(cidadeAdapter);
    }

    private String removerAcentos(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}