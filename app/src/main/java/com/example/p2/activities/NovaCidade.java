package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
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

public class NovaCidade extends AppCompatActivity {
    private EditText nomeEditText;
    private Spinner estadoSpinner;
    private Button voltar, salvarButton;
    private AppDatabase db;

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
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        salvarButton = findViewById(R.id.salvar);
        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarCidade();
            }
        });

        voltar = findViewById(R.id.voltarnv);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovaCidade.this, GerenciarCidades.class);
                startActivity(intent);
            }
        });
    }

    private void salvarCidade() {
        String nome = nomeEditText.getText().toString();
        String estado = estadoSpinner.getSelectedItem().toString();
        if (nome != null && !nome.trim().isEmpty()) {
            Cidade novaCidade = new Cidade();
            novaCidade.setNomeCidade(nome);
            novaCidade.setEstado(estado);

            db.cidadeDao().insert(novaCidade);
            Toast.makeText(NovaCidade.this, "Cidade salva com sucesso!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NovaCidade.this, GerenciarCidades.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nome e estado da cidade não podem ser vazios.", Toast.LENGTH_SHORT).show();
        }
    }
}