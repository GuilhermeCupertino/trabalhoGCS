package com.example.p2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Usuario{
    @PrimaryKey(autoGenerate = true)
    int id;
    String nome;
    String email;
    String senha;
    String telefone;
    public Usuario(){}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return  getNome() +": " + getEmail();

    }

}

