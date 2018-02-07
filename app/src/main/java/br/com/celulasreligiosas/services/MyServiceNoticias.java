package br.com.celulasreligiosas.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;

import br.com.celulasreligiosas.entity.Noticia;
import br.com.celulasreligiosas.entity.ParamRequest;
import br.com.celulasreligiosas.task.NoticiaListener;
import br.com.celulasreligiosas.task.RequestApi;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyServiceNoticias extends Service implements NoticiaListener{

    private List<Noticia> listArt;
    private IBinder mBinder = new MyBinder();
    private NoticiaListener nListener = null;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void getNoticias(List<Noticia> listArt) {

    }

    public class MyBinder extends Binder {
        public MyServiceNoticias getService() {
            return MyServiceNoticias.this;
        }

        public void registerListener(NoticiaListener listener) {
            MyServiceNoticias.this.nListener = listener;
        }

        public void unregisterListener() {
            MyServiceNoticias.this.nListener = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RetrofitAsyncTask task = new RetrofitAsyncTask();
        task.execute();

        return START_STICKY;
    }

    public class RetrofitAsyncTask extends AsyncTask<ParamRequest, Void, List<Noticia>> {

        public static final String SERVICE_TODAS_NOTICIAS = "http://ws-celulasreligiosas-app-celulas-religiosas.a3c1.starter-us-west-1.openshiftapps.com/rest/serviceNoticia/";

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

            if (nListener != null) {
                nListener.getNoticias(listArt);
            }

            return listArt;
        }

    }

}
