package com.example.p2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.database.AppDatabase;
import com.example.p2.entities.Cidade;

public class NovaCidade extends AppCompatActivity {
    private EditText nomeEditText, estadoEditText;
    private Button voltar, salvarButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_cidade);

        nomeEditText = findViewById(R.id.desc);
        estadoEditText = findViewById(R.id.lat);

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
        String estado = estadoEditText.getText().toString();

        Cidade novaCidade = new Cidade();
        novaCidade.setNomeCidade(nome);
        novaCidade.setEstado(estado);

        db.cidadeDao().insert(novaCidade);
        Toast.makeText(this, "Cidade salva com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(NovaCidade.this, GerenciarCidades.class);
        startActivity(intent);
    }
}