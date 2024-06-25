package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;


import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.model.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import java.util.List;

public class NovaCidade extends AppCompatActivity {
    private EditText nomeEditText, estadoEditText;
    private Button voltarButton, salvarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_cidade);


        nomeEditText = findViewById(R.id.desc);
        estadoSpinner = findViewById(R.id.estadoSpinner);

        // Preenche o Spinner com os estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.estados_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estadoSpinner.setAdapter(adapter);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "AppDatabase")
                .fallbackToDestructiveMigration()
                .build();

        salvarButton = findViewById(R.id.salvar);
        salvarButton.setOnClickListener(view -> verificarECadastrarCidade());

        voltarButton = findViewById(R.id.voltarnv);
        voltarButton.setOnClickListener(v -> {
            Intent intent = new Intent(NovaCidade.this, GerenciarCidades.class);
            startActivity(intent);
        });


    }

    private void verificarECadastrarCidade() {
        String nome = nomeEditText.getText().toString().trim();
        String estado = estadoEditText.getText().toString().trim();

        if (!nome.isEmpty() && !estado.isEmpty()) {
            String cidadeNomeCompleto = nome + ", " + estado;
            cidadeExiste(cidadeNomeCompleto);
        } else {
            Toast.makeText(this, "Nome e estado da cidade não podem ser vazios.", Toast.LENGTH_SHORT).show();
        }
    }

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

                boolean cidadeEncontrada = predictions.stream()
                        .anyMatch(prediction -> prediction.getPrimaryText(null).toString().equalsIgnoreCase(nomeCidade));

                if (cidadeEncontrada) {
                    salvarCidade(nomeCidade);
                } else {
                    Toast.makeText(NovaCidade.this, "Cidade não encontrada", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NovaCidade.this, "Erro ao verificar cidade: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            if (e instanceof ApiException) {
                ApiException apiException = (ApiException) e;
                Status status = apiException.getStatus();
                Toast.makeText(NovaCidade.this, "Erro ao verificar cidade: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarCidade(String nomeCidade) {
        String[] partes = nomeCidade.split(", ");
        if (partes.length == 2) {
            String nome = partes[0];
            String estado = partes[1];

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Cidade novaCidade = new Cidade();
                    novaCidade.setNomeCidade(nome);
                    novaCidade.setEstado(estado);
                    db.cidadeDao().insert(novaCidade);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Toast.makeText(NovaCidade.this, "Cidade salva com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NovaCidade.this, GerenciarCidades.class));
                }
            }.execute();
        }
    }
}


