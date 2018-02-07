package br.com.celulasreligiosas.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.com.celulasreligiosas.entity.Noticia;

/**
 * Created by robson.carlos.santos on 05/02/2018.
 */

@Database(entities = {Noticia.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class,
                    "noticialist.db").allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract NoticiaDao noticiaDao();
}
