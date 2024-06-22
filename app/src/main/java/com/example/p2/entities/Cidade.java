package com.example.p2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cidade")
public class Cidade{
    @PrimaryKey (autoGenerate = true)
    long cidadeId;
    String nomeCidade;
    String estado;

    public Cidade() {}

    public long getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(long cidadeId) {
        this.cidadeId = cidadeId;
    }

    public String getNomeCidade() {
        return nomeCidade;
    }

    public void setNomeCidade(String nomeCidade) {
        this.nomeCidade = nomeCidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return  getNomeCidade() +", " + getEstado();
    }

}
