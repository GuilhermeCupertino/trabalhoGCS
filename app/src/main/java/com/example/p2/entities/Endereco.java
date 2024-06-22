package com.example.p2.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(entity = Cidade.class,
                parentColumns = "cidadeId",
                childColumns = "cidadeId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index(value = "cidadeId")}  // Add index here
)
public class Endereco {
    @PrimaryKey(autoGenerate = true)
    int enderecoId;
    String descricao;
    double latitude;
    double longitude;
    long cidadeId;

    // Getters and Setters
    public int getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(int enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(long cidadeId) {
        this.cidadeId = cidadeId;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}
