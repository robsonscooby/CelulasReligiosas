package br.com.celulasreligiosas.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.celulasreligiosas.entity.Noticia;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by robson.carlos.santos on 05/02/2018.
 */

@Dao
public interface NoticiaDao {
    @Query("SELECT * FROM noticia")
    LiveData<List<Noticia>> getAll();

    @Insert(onConflict = REPLACE)
    void insert(Noticia noticia);

    @Update
    void update(Noticia... noticias);

    @Delete
    void delete(Noticia... noticias);
}
