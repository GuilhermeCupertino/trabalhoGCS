package com.example.p2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        // Inicializa o banco de dados Room
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries() // Permite consultas na main thread (só para fins de exemplo, não é recomendado para produção)
                .fallbackToDestructiveMigration() // Recria o banco de dados se necessário
                .build();

        // Carrega todas as cidades do banco de dados
        cidades = db.cidadeDao().getAll();

        // Ordena as cidades por nome
        Collections.sort(cidades, new Comparator<Cidade>() {
            @Override
            public int compare(Cidade cidade1, Cidade cidade2) {
                return cidade1.getNomeCidade().compareToIgnoreCase(cidade2.getNomeCidade());
            }
        });

        // Cria e configura o adapter para a ListView
        cidadeAdapter = new CidadeAdapter(this, cidades);
        listViewCidades.setAdapter(cidadeAdapter);

        // Configuração do botão "Voltar"
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GerenciarCidades.this, TelaInicial.class));
            }
        });

        // Configuração do botão "Nova Cidade"
        novaCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GerenciarCidades.this, NovaCidade.class));
            }
        });

        // Configuração do botão "Buscar"
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarCidades();
            }
        });

    }

    // Método para buscar cidades com base no texto de busca
    private void buscarCidades() {
        String textoBusca = buscarCid.getText().toString().trim();
        List<Cidade> cidadesFiltradas = new ArrayList<>();

        // Filtra as cidades com base no texto de busca (considerando ignorar acentos)
        for (Cidade cidade : cidades) {
            if (removerAcentos(cidade.getNomeCidade().toLowerCase()).contains(removerAcentos(textoBusca.toLowerCase()))) {
                cidadesFiltradas.add(cidade);
            }
        }

        // Atualiza o adapter com as cidades filtradas
        cidadeAdapter = new CidadeAdapter(this, cidadesFiltradas);
        listViewCidades.setAdapter(cidadeAdapter);
    }

    // Método para remover acentos de uma string
    private String removerAcentos(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
    
    // Método para verificar se a cidade existe utilizando a API do Google Places
    private void cidadeExiste(String nomeCidade) {
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(FindAutocompletePredictionsRequest.TypeFilter.CITIES)
                .setQuery(nomeCidade)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse response = task.getResult();
                List<AutocompletePrediction> predictions = response.getAutocompletePredictions();

                boolean cidadeEncontrada = false;

                for (AutocompletePrediction prediction : predictions) {
                    if (prediction.getPrimaryText(null).toString().equalsIgnoreCase(nomeCidade)) {
                        cidadeEncontrada = true;
                        break;
                    }
                }

                if (cidadeEncontrada) {
                    salvarCidade(nomeCidade);
                } else {
                    Toast.makeText(GerenciarCidades.this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GerenciarCidades.this, "Erro ao verificar cidade: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                Status status = apiException.getStatus();
                Toast.makeText(GerenciarCidades.this, "Erro ao verificar cidade: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para salvar a cidade no banco de dados
    private void salvarCidade(String nomeCidade) {
        Cidade novaCidade = new Cidade(nomeCidade);
        db.cidadeDao().inserir(novaCidade);

        Toast.makeText(GerenciarCidades.this, "Cidade cadastrada com sucesso", Toast.LENGTH_SHORT).show();
        cidades = db.cidadeDao().getAll();
        cidadeAdapter.setCidades(cidades);
        cidadeAdapter.notifyDataSetChanged();
    }
}
