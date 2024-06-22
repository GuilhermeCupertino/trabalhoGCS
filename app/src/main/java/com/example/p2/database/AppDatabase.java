package com.example.p2.database;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.p2.dao.EnderecoDao;
import com.example.p2.dao.UsuarioDao;
import com.example.p2.dao.CidadeDao;
import com.example.p2.entities.Endereco;
import com.example.p2.entities.Usuario;
import com.example.p2.entities.Cidade;

@androidx.room.Database(entities = {Usuario.class, Cidade.class, Endereco.class}, version = 11)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "AppDatabase")
                            .fallbackToDestructiveMigration()  // Add this line during development to recreate the DB when the version is incremented
                            .allowMainThreadQueries()  // Be cautious with this in production
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UsuarioDao usuarioDao();
    public abstract CidadeDao cidadeDao();
    public abstract EnderecoDao enderecoDao();
}