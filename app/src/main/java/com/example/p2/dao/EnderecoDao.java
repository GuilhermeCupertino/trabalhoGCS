package com.example.p2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.p2.entities.Endereco;

import java.util.List;

@Dao
public interface EnderecoDao {
    // Método renomeado para refletir corretamente sua funcionalidade
    @Query("SELECT * FROM Endereco WHERE enderecoId = :idEnd LIMIT 1")
    Endereco getEndereco(int idEnd);

    // Busca todos os endereço
    @Query("SELECT * FROM Endereco")
    List<Endereco> getAll();

    @Update
    void update(Endereco endereco);

    @Delete
    void delete(Endereco endereco);

    @Insert
    void insert(Endereco novoEndereco);
}