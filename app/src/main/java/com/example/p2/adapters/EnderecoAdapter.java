package com.example.p2.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.p2.R;
import com.example.p2.activities.MapaEndereco;
import com.example.p2.entities.Endereco;

import java.util.List;

public class EnderecoAdapter extends BaseAdapter {
    private static final String TAG = "EnderecoAdapter";

    private Context context;
    private List<Endereco> enderecos;

    public EnderecoAdapter(Context context, List<Endereco> enderecos) {
        this.context = context;
        this.enderecos = enderecos;
    }

    @Override
    public int getCount() {
        return enderecos.size();
    }

    @Override
    public Endereco getItem(int position) {
        return enderecos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_endereco, parent, false);
            holder = new ViewHolder();
            holder.buttonEnd = convertView.findViewById(R.id.buttonEndereco);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Endereco endereco = getItem(position);
        if (endereco != null) {
            holder.buttonEnd.setText(endereco.getDescricao());
        } else {
            holder.buttonEnd.setText("Endereco Desconhecido");
        }

        holder.buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long enderecoId = endereco.getEnderecoId();
                String descricao = endereco.getDescricao();
                saveEnderecoToSharedPreferences((int) enderecoId, descricao);
                Intent intent = new Intent(context, MapaEndereco.class);
                context.startActivity(intent);
                // Debug log to confirm the value is saved
                Log.d(TAG, "Saved enderecoId: " + enderecoId);
                Log.d(TAG, "Saved descricao: " + descricao);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        Button buttonEnd;
    }

    private void saveEnderecoToSharedPreferences(int enderecoId, String descricao) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("locPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idEnd", enderecoId);
        editor.putString("endMarcado", descricao);
        editor.apply();
    }
}
