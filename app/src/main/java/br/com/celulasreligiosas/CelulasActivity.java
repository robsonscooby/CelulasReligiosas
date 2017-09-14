package br.com.celulasreligiosas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.celulasreligiosas.adapter.CelulaAdapter;
import br.com.celulasreligiosas.dao.CelulasDAO;
import br.com.celulasreligiosas.entity.Celula;

public class CelulasActivity extends AppCompatActivity {

    private ListView listaCelulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celulas);

        listaCelulas = (ListView) findViewById(R.id.celula_lista);

        listaCelulas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Celula celula = (Celula) listaCelulas.getItemAtPosition(i);

                Intent intent = new Intent(CelulasActivity.this, CadastroActivity.class);
                intent.putExtra("celula", celula);
                startActivity(intent);
            }
        });

        registerForContextMenu(listaCelulas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_celula,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_mapa:
                Intent intentMapa = new Intent(this, MapaActivity.class);
                startActivity(intentMapa);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addCelula(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Celula celula = (Celula) listaCelulas.getItemAtPosition(info.position);

        MenuItem ligar = menu.add("Ligar");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(ActivityCompat.checkSelfPermission(CelulasActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CelulasActivity.this,new String[]{Manifest.permission.CALL_PHONE},123);
                }else{
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:"+celula.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });

        MenuItem enviarSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:"+celula.getTelefone()));
        enviarSMS.setIntent(intentSMS);

        MenuItem verMapa = menu.add("Localização Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q="+celula.getEndereco()));
        verMapa.setIntent(intentMapa);

        MenuItem visitaSite = menu.add("Visitar site");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String site = celula.getSite();
        if(!site.startsWith("http://")){
            site = "http://"+celula.getSite();
        }
        intent.setData(Uri.parse(site));
        visitaSite.setIntent(intent);

        MenuItem deletar = menu.add("Deletar");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                CelulasDAO dao = new CelulasDAO(CelulasActivity.this);
                dao.delete(celula);
                dao.close();

                carregarLista();
                return false;
            }
        });
    }

    private void carregarLista(){
        CelulasDAO dao = new CelulasDAO(this);
        List<Celula> celulas = dao.findAll();
        dao.close();

        CelulaAdapter adapter = new CelulaAdapter(this, celulas);
        listaCelulas.setAdapter(adapter);
    }
}
