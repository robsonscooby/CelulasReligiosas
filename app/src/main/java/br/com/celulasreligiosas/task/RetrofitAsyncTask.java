package br.com.celulasreligiosas.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.celulasreligiosas.NoticiasActivity;
import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.adapter.NoticiasAdapter;
import br.com.celulasreligiosas.entity.Article;
import br.com.celulasreligiosas.entity.ParamRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class RetrofitAsyncTask extends AsyncTask<ParamRequest, Void, List<Article>> {

    private NoticiasActivity context;
    private ListView listaView;
    private ProgressBar barra;
    private TextView textInfo;

    public RetrofitAsyncTask(NoticiasActivity context) {
        this.context = context;
        listaView = (ListView) context.findViewById(R.id.lista_noticias);
        barra = (ProgressBar) context.findViewById(R.id.load_progress);
        textInfo = (TextView) context.findViewById(R.id.txt_info);
    }

    @Override
    protected List<Article> doInBackground(ParamRequest... param) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v1/").addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestApi requestApi = retrofit.create(RequestApi.class);
        ParamRequest paramRequest = param[0];
        try {
            Response<ResultNewsApi> response = requestApi.getNewsApi(paramRequest.source,paramRequest.sortBy,paramRequest.apiKey).execute();
            List<Article> listArt = response.body().getArticles();
            return listArt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Article> listaNoticias) {
        super.onPostExecute(listaNoticias);
        barra.setVisibility(View.GONE);
        textInfo.setVisibility(View.GONE);

        if(null != listaNoticias){
            NoticiasAdapter adapter = new NoticiasAdapter(listaNoticias, context);
            listaView.setAdapter(adapter);
        }
    }

    @Override
    protected void onPreExecute() {
        barra.setVisibility(View.VISIBLE);
        textInfo.setVisibility(View.VISIBLE);
    }

}
