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
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.celulasreligiosas.adapter.CelulaAdapter;
import br.com.celulasreligiosas.entity.Celula;

public class CelulasActivity extends AppCompatActivity {

    private ListView listaCelulas;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Celula> celulas = new ArrayList<Celula>();
    private ProgressBar barra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celulas);

        listaCelulas = (ListView) findViewById(R.id.celula_lista);
        barra = (ProgressBar) findViewById(R.id.celulas_load);
        barra.setVisibility(View.VISIBLE);

        inicializarFirebase();
        eventoDataBase();

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
//        inicializarFirebase();
//        eventoDataBase();
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
                //deleta celula
                databaseReference.child("Celula").child(celula.getUid()).removeValue();
                //carregarLista();
                return false;
            }
        });
    }

//    private void carregarLista(){
//        eventoDataBase();
//
//    }

    //Seleciona todas as celulas
    private void eventoDataBase() {
        databaseReference.child("Celula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                celulas.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Celula c = item.getValue(Celula.class);
                    celulas.add(c);
                }
                CelulaAdapter adapter = new CelulaAdapter(CelulasActivity.this, celulas);
                listaCelulas.setAdapter(adapter);
                barra.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void populaCelulas(){
//
//        Celula celula1 = new Celula();
//        celula1.setNome("Célula da FÉ");
//        celula1.setEndereco("Rua Resplendor,171,Vasco da Gama,Recife,PE");
//        celula1.setTelefone("081999999999");
//        celula1.setSite("www.google.com.br");
//        celula1.setDescricao("Segunda-Feira ás 20:00 - Líder: Jefferson - Vice Líder: Simone");
//        celula1.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula1.getUid()).setValue(celula1);
//
//        Celula celula2 = new Celula();
//        celula2.setNome("Romper em FÉ");
//        celula2.setEndereco("Avenida José Américo de Almeida,383,Macaxeira,Recife,PE");
//        celula2.setTelefone("081999999999");
//        celula2.setSite("www.google.com.br");
//        celula2.setDescricao("Quarta-Feira ás 20:00 - Líder: Monica - Vice Líder: Hanna");
//        celula2.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula2.getUid()).setValue(celula2);
//
//        Celula celula3 = new Celula();
//        celula3.setNome("Resgate");
//        celula3.setEndereco("Rua da Pérola,415,Nova Descoberta,Recife,PE");
//        celula3.setTelefone("081999999999");
//        celula3.setSite("www.google.com.br");
//        celula3.setDescricao("Quinta-Feira ás 20:00 - Líder: Simone - Vice Líder: Jefferson");
//        celula3.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula3.getUid()).setValue(celula3);
//
//        Celula celula4 = new Celula();
//        celula4.setNome("Filhos da Promessa");
//        celula4.setEndereco("Rua Tacina,89,Nova Descoberta,Recife,PE");
//        celula4.setTelefone("081999999999");
//        celula4.setSite("www.google.com.br");
//        celula4.setDescricao("Quarta-Feira ás 20:00 - Líder: Marcela - Vice Líder: Cleide");
//        celula4.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula4.getUid()).setValue(celula4);
//
//        Celula celula5 = new Celula();
//        celula5.setNome("Família de DEUS");
//        celula5.setEndereco("Rua José Rebouças,SN,Vasco da Gama,Recife,PE");
//        celula5.setTelefone("081999999999");
//        celula5.setSite("www.google.com.br");
//        celula5.setDescricao("Terça-Feira ás 20:00 - Líder: Pastora Iracema - Vice Líder: Marcela");
//        celula5.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula5.getUid()).setValue(celula5);
//
//        Celula celula6 = new Celula();
//        celula6.setNome("Unção que multiplica");
//        celula6.setEndereco("Rua Cambuquira,10,Vasco da Gama,Recife,PE");
//        celula6.setTelefone("081999999999");
//        celula6.setSite("www.google.com.br");
//        celula6.setDescricao("Quarta-Feira ás 20:00 - Líder: Diego - Vice Líder: Débora");
//        celula6.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula6.getUid()).setValue(celula6);
//
//        Celula celula7 = new Celula();
//        celula7.setNome("Plantando amor");
//        celula7.setEndereco("4ª Travessa Frederico Ozanan,134,Recife,PE");
//        celula7.setTelefone("081999999999");
//        celula7.setSite("www.google.com.br");
//        celula7.setDescricao("Quinta-Feira ás 20:00 - Líder: Estevão - Vice Líder: Cleide");
//        celula7.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula7.getUid()).setValue(celula7);
//
//        Celula celula8 = new Celula();
//        celula8.setNome("Livres para adorar");
//        celula8.setEndereco("Rua do Morro da Conceição,487,Morro da Conceição,Recife,PE");
//        celula8.setTelefone("081999999999");
//        celula8.setSite("www.google.com.br");
//        celula8.setDescricao("Quarta-Feira ás 20:00 - Líder: Thiago - Vice Líder: Jefferson");
//        celula8.setUid(UUID.randomUUID().toString());
//        //insert
//        databaseReference.child("Celula").child(celula8.getUid()).setValue(celula8);
//    }

    //Conexao
    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Serve para salvar e alterar na nuvem e no app
        //firebaseDatabase.setPersistenceEnabled(true);

        databaseReference = firebaseDatabase.getReference();
    }
}