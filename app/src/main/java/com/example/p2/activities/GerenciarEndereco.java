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
import com.example.p2.adapters.EnderecoAdapter;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Endereco;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class GerenciarEndereco extends AppCompatActivity {
    private ListView listViewEnd;
    private EnderecoAdapter enderecoAdapter;
    private AppDatabase db;
    private Button voltar;
    private Button novoEnd;
    private Button buscar;
    private EditText buscarEnd;
    private List<Endereco> enderecos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_endereco);

        listViewEnd = findViewById(R.id.lista_end);
        voltarDetalhesEndereco = findViewById(R.id.voltarCidade);
        novoEnd = findViewById(R.id.novoend);
        buscar = findViewById(R.id.botaoBuscar_end);
        buscarEnd = findViewById(R.id.buscarEnd);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        enderecos = db.enderecoDao().getAll();

        // Ordenar os endereços em ordem alfabética pela descrição
        Collections.sort(enderecos, new Comparator<Endereco>() {
            @Override
            public int compare(Endereco endereco1, Endereco endereco2) {
                return endereco1.getDescricao().compareToIgnoreCase(endereco2.getDescricao());
            }
        });

        enderecoAdapter = new EnderecoAdapter(this, enderecos);
        listViewEnd.setAdapter(enderecoAdapter);

        // Configura um listener para o botão voltarDetalhesEndereco
        voltarDetalhesEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia uma nova atividade
                // O novo Intent especifica que a atividade atual (GerenciarEndereco)
                // irá iniciar a atividade TelaInicial
                startActivity(new Intent(GerenciarEndereco.this, TelaInicial.class));
            }
        });
    }

        novoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GerenciarEndereco.this, NovoEndereco.class));
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarEnderecos();
            }
        });
    }

    private void buscarEnderecos() {
        String textoBusca = buscarEnd.getText().toString().trim();
        List<Endereco> enderecosFiltrados = new ArrayList<>();

        for (Endereco endereco : enderecos) {
            if (removerAcentos(endereco.getDescricao().toLowerCase()).contains(removerAcentos(textoBusca.toLowerCase()))) {
                enderecosFiltrados.add(endereco);
            }
        }

        enderecoAdapter = new EnderecoAdapter(this, enderecosFiltrados);
        listViewEnd.setAdapter(enderecoAdapter);
    }

    private String removerAcentos(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
}
