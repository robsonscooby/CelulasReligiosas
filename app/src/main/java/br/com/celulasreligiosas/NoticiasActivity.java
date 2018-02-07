package br.com.celulasreligiosas;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.adapter.NoticiasAdapter;
import br.com.celulasreligiosas.dao.AppDatabase;
import br.com.celulasreligiosas.entity.Noticia;
import br.com.celulasreligiosas.model.NoticiaViewModel;
import br.com.celulasreligiosas.services.MyServiceNoticias;
import br.com.celulasreligiosas.task.NoticiaListener;

public class NoticiasActivity extends AppCompatActivity implements ServiceConnection, NoticiaListener{

    private MyServiceNoticias mService;
    private ServiceConnection conexao;
    private AppDatabase db;
    private LiveData<List<Noticia>> listArt;
    private List<Noticia> listNoticias = new ArrayList<Noticia>();

    private ProgressBar barra;
    private TextView textInfo;
    private MyServiceNoticias.MyBinder myBinder;

    private boolean mServiceBound = false;
    private boolean statusInternet = false;

    private NoticiaViewModel viewModel;
    private RecyclerView recyclerView;
    private NoticiasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_noticias);
        textInfo = (TextView) findViewById(R.id.txt_info);
        barra = (ProgressBar) findViewById(R.id.load_progress);
        db = AppDatabase.getInstance(this.getApplication().getApplicationContext());

        //verifica o status da internet para realizar a chamada do service ou banco de dados
        statusInternet = verificaConexao();
        if(statusInternet) {
            textInfo.setVisibility(View.VISIBLE);
            barra.setVisibility(View.VISIBLE);

            conexao = this;
            iniciarService();
        } else {
            viewModel = ViewModelProviders.of(this).get(NoticiaViewModel.class);
            viewModel.getNoticias().observe(this, this::updateView);
        }
    }

    private void setupRecyclerView(List<Noticia> noticias) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);

            adapter = new NoticiasAdapter(new NoticiasAdapter.OnTaskClickedListener() {
                @Override
                public void onClick(Noticia noticia) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String site = noticia.getUrl();
                    if (!site.startsWith("http://")) {
                        site = "http://" + noticia.getUrl();
                    }
                    intent.setData(Uri.parse(site));
                    startActivity(intent);
                }

                @Override
                public void onLongClick(Noticia noticia) {

                }
            });
            adapter.replaceItems(noticias);
            recyclerView.setAdapter(adapter);

    }

    private void updateView(List<Noticia> noticias) {
        adapter.replaceItems(noticias);
        adapter.notifyDataSetChanged();
    }

    public void iniciarService(){
        Intent intent = new Intent(NoticiasActivity.this, MyServiceNoticias.class);
        intent.resolveType(this);

        bindService(intent , conexao, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mServiceBound = true;
        myBinder = (MyServiceNoticias.MyBinder) iBinder;
        myBinder.registerListener(NoticiasActivity.this);
        mService = myBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService = null;
        mServiceBound = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (statusInternet && myBinder != null) {
            myBinder.unregisterListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusInternet && myBinder != null) {
            myBinder.registerListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(statusInternet) {
            unbindService(conexao);
            myBinder.unregisterListener();
        }
    }

    @Override
    public void getNoticias(List<Noticia> noticias) {
        if(null != noticias) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    barra.setVisibility(View.INVISIBLE);
                    textInfo.setVisibility(View.INVISIBLE);

                    setupRecyclerView(noticias);
                }
            });
        }
    }

    /* Função para verificar existência de conexão com a internet
	 */
    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

}
