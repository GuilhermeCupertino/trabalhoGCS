package com.example.p2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.p2.R;
import com.example.p2.entities.Usuario;

import java.util.List;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    public UsuarioAdapter(Context context, List<Usuario> usuarios) {
        super(context, 0, usuarios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtém o item de dados desta posição
        Usuario usuario = getItem(position);

        // Verifica se uma view existente está sendo reutilizada, caso contrário, inflar a view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_usuario, parent, false);
        }

        // Busca os TextView no layout list_item_usuario.xml
        TextView nomeTextView = convertView.findViewById(R.id.txt_nome);
        TextView emailTextView = convertView.findViewById(R.id.txt_email);

        // Popula os dados na view
        nomeTextView.setText(usuario.getNome());
        emailTextView.setText(usuario.getEmail());

        // Retorna a view para renderização na tela
        return convertView;
    }
}