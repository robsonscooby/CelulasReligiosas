package br.com.celulasreligiosas.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import br.com.celulasreligiosas.NoticiasActivity;
import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.adapter.NoticiasAdapter;
import br.com.celulasreligiosas.entity.Noticia;
import br.com.celulasreligiosas.entity.ParamRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class RetrofitAsyncTask extends AsyncTask<ParamRequest, Void, List<Noticia>>{

    public static final String SERVICE_TODAS_NOTICIAS = "http://ws-celulasreligiosas-app-celulas-religiosas.a3c1.starter-us-west-1.openshiftapps.com/rest/serviceNoticia/";
    private List<Noticia> listArt;

    @Override
    protected List<Noticia> doInBackground(ParamRequest... param) {
       if(listArt == null) {
           Retrofit retrofit = new Retrofit.Builder()
                   .baseUrl(SERVICE_TODAS_NOTICIAS).addConverterFactory(GsonConverterFactory.create())
                   .build();
           RequestApi requestApi = retrofit.create(RequestApi.class);
           try {
               Response<List<Noticia>> response = requestApi.getNoticias().execute();
               listArt = response.body();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
        return listArt;
    }

    @Override
    protected void onPostExecute(List<Noticia> listaNoticias) {
        super.onPostExecute(listaNoticias);
    }

    @Override
    protected void onPreExecute() {
    }

}
