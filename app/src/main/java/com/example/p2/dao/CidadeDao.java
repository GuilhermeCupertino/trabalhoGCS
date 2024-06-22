package com.example.p2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.p2.entities.Cidade;

import java.util.List;

@Dao
public interface CidadeDao{
    @Query("SELECT * FROM Cidade WHERE cidadeId=:idCity LIMIT 1")
    Cidade getCidade(int idCity);

    @Query("SELECT * FROM Cidade")
    List<Cidade> getAll();

    @Update
    void update(Cidade cidade);

    @Delete
    void delete(Cidade cidade);

    @Insert
    void insert(Cidade novaCidade);

}