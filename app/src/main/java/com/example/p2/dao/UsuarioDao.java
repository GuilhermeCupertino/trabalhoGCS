package com.example.p2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.p2.entities.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM Usuario WHERE id=:idUsu LIMIT 1")
    Usuario getUser(int idUsu);

    @Query("SELECT * FROM Usuario")
    List<Usuario> getAll();

    @Insert
    void insertAll(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    // Método adicionado para autenticação de login
    @Query("SELECT * FROM usuario WHERE email = :email AND senha = :senha")
    Usuario getUsuario(String email, String senha);

    @Query("SELECT * FROM Usuario WHERE email = :email")
    Usuario getUserByEmail(String email);
}

