package br.com.celulasreligiosas;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.celulasreligiosas.entity.Noticia;
import br.com.celulasreligiosas.task.RetrofitAsyncTask;

public class NoticiasActivity extends AppCompatActivity {

    private ListView listaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);

        RetrofitAsyncTask task = new RetrofitAsyncTask(this);
        task.execute();

        listaView = (ListView) findViewById(R.id.lista_noticias);
        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Noticia item = (Noticia) listaView.getItemAtPosition(i);
                if(!item.getUrl().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String site = item.getUrl();
                    if (!site.startsWith("http://")) {
                        site = "http://" + item.getUrl();
                    }
                    intent.setData(Uri.parse(site));
                    startActivity(intent);
                }
            }
        });
    }

}
