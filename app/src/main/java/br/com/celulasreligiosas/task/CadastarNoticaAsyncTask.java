package br.com.celulasreligiosas.task;

import android.os.AsyncTask;
import java.io.IOException;
import br.com.celulasreligiosas.entity.Noticia;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class CadastarNoticaAsyncTask extends AsyncTask<Noticia, Void, Void> {

    public static final String SERVICE_TODAS_NOTICIAS = "http://ws-celulasreligiosas-app-celulas-religiosas.a3c1.starter-us-west-1.openshiftapps.com/rest/serviceNoticia/";

    @Override
    protected Void doInBackground(Noticia... noticias) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_TODAS_NOTICIAS).addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestApi requestApi = retrofit.create(RequestApi.class);
        try {
            Response response = requestApi.cadastrar(noticias[0]).execute();
            response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
