package br.com.celulasreligiosas.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import br.com.celulasreligiosas.dao.AppDatabase;
import br.com.celulasreligiosas.entity.Noticia;

/**
 * Created by robson.carlos.santos on 05/02/2018.
 */

public class NoticiaViewModel extends AndroidViewModel {
    private LiveData<List<Noticia>> noticias;
    private AppDatabase db;

    public NoticiaViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(this.getApplication().getApplicationContext());
        noticias = db.noticiaDao().getAll();
    }

    public LiveData<List<Noticia>> getNoticias() {
        return noticias;
    }

    void saveNoticia(Noticia noticia) {
        db.noticiaDao().insert(noticia);
    }

    void deleteNoticia(Noticia noticia) {
        db.noticiaDao().delete(noticia);
    }

}