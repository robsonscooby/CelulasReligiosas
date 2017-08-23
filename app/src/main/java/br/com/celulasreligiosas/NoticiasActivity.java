package br.com.celulasreligiosas;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.celulasreligiosas.entity.Article;
import br.com.celulasreligiosas.entity.ParamRequest;
import br.com.celulasreligiosas.task.RetrofitAsyncTask;

public class NoticiasActivity extends AppCompatActivity {

    private ListView listaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        ParamRequest paramRequest = new ParamRequest();
        paramRequest.source = "google-news";
        paramRequest.sortBy = "top";
        paramRequest.apiKey = "d5c4573f4a4c4febb31b4e3fe2c0bc5c";

        RetrofitAsyncTask task = new RetrofitAsyncTask(this);
        task.execute(paramRequest);


        listaView = (ListView) findViewById(R.id.lista_noticias);
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article item = (Article) listaView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String site = item.getUrl();
                if(!site.startsWith("http://")){
                    site = "http://"+item.getUrl();
                }
                intent.setData(Uri.parse(site));
                startActivity(intent);
            }
        });


    }

}
