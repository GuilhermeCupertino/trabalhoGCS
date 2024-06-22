package com.example.p2.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.p2.R;
import com.example.p2.activities.EdtCidade;
import com.example.p2.activities.EdtEndereco;
import com.example.p2.entities.Cidade;

import java.util.List;

public class CidadeAdapter extends BaseAdapter {
    private Context context;
    private List<Cidade> cidades;

    public CidadeAdapter(Context context, List<Cidade> cidades) {
        this.context = context;
        this.cidades = cidades;
    }

    @Override
    public int getCount() {
        return cidades.size();
    }

    @Override
    public Object getItem(int position) {
        return cidades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_cidade, parent, false);
        }

        Cidade cidade = cidades.get(position);

        Button buttonCidade = convertView.findViewById(R.id.buttonCidade);
        buttonCidade.setText(cidade.getNomeCidade() + ", " + cidade.getEstado());

        buttonCidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cidadeId = cidade.getCidadeId();
                String cidadeNome = cidade.getNomeCidade();
                String estado = cidade.getEstado();
                saveEnderecoToSharedPreferences((int) cidadeId, cidadeNome, estado);
                Intent intent = new Intent(context, EdtCidade.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    private void saveEnderecoToSharedPreferences(int cidadeId, String cidadeNome, String estado) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CidadePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("cidadeId", cidadeId);
        editor.putString("cidadeNome", cidadeNome);
        editor.putString("estado", estado);
        editor.apply();
    }
}
