package br.com.celulasreligiosas.task;

import java.util.List;

import br.com.celulasreligiosas.entity.Noticia;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public interface RequestApi {
    @GET("todasNoticias")
    Call <List<Noticia>> getNoticias();

    @POST("cadastrar")
    Call<Noticia> cadastrar(@Body Noticia notica);
}
