package br.com.celulasreligiosas.task;

import java.util.List;

import br.com.celulasreligiosas.entity.Noticia;

/**
 * Created by robson.carlos.santos on 04/02/2018.
 */

public interface NoticiaListener {
    public void getNoticias(List<Noticia> listArt);
}
