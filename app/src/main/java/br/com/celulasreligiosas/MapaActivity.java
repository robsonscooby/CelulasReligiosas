package br.com.celulasreligiosas;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.entity.Celula;
import br.com.celulasreligiosas.fragment.MapaFragment;

public class MapaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Celula> listaCelulas = new ArrayList<Celula>();
    private ProgressBar barra;
    private static String mTextFilter = "";
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private MenuItem mClearSearchMenuItem;
    private static final String MAPA_FRAGMENT_TAG = "MapaDasCelulas";
    public static final int CODIGO_CEL = 4850;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        barra = (ProgressBar) findViewById(R.id.mapa_load);
        barra.setVisibility(View.VISIBLE);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        inicializarFirebase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        mSearchMenuItem = (MenuItem)menu.findItem(R.id.mn_search);
        mClearSearchMenuItem = (MenuItem)menu.findItem(R.id.mn_clear_filter);
        mClearSearchMenuItem.setVisible(mTextFilter.length() > 0 && !mTextFilter.equals("*") && !mTextFilter.equals("%"));
        mSearchView = (SearchView)mSearchMenuItem.getActionView();
        mSearchView.setQueryHint(mSearchMenuItem.getTitle());
        mSearchView.setOnQueryTextListener(this);
        mClearSearchMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                limparBusca();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mTextFilter = query;
        atualizeClearSearch();
        if (mClearSearchMenuItem.isVisible()) {
            getMapaFragment().buscarCelulas(query,databaseReference);
        }
        minimizarBarraDeBusca();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void limparBusca(){
        minimizarBarraDeBusca();
        mTextFilter = "";
        mClearSearchMenuItem.setVisible(false);
        getMapaFragment().buscarCelulas();
    }

    private void minimizarBarraDeBusca(){
        mSearchView.setQuery("",false);
        mSearchView.clearFocus();
        mSearchView.setIconified(true);
    }

    private MapaFragment getMapaFragment(){
        FragmentManager fm = getSupportFragmentManager();
        MapaFragment fragment = (MapaFragment)fm.findFragmentByTag(MAPA_FRAGMENT_TAG);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_inicial:
                Intent intentCadastro = new Intent(this,CadastroActivity.class);
                startActivityForResult(intentCadastro,CODIGO_CEL);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Metodo responsavel por recuperar a nova celula criada
       na tela de cadastro e exibir no mapa
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == MapaActivity.RESULT_OK && requestCode == CODIGO_CEL) {
            Celula cel = (Celula) data.getSerializableExtra("celula");
            listaCelulas.add(cel);
            getMapaFragment().recarregarMapa();
        }
    }

    private void atualizeClearSearch(){
        mClearSearchMenuItem.setVisible(mTextFilter.length() > 0 && !mTextFilter.equals("*") && !mTextFilter.equals("%"));
    }

    private void inicializarFirebase(){

        //chamada que dispensa a criacao de uma nova asynctask para consulta no banco firebase
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                listaCelulas.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Celula c = item.getValue(Celula.class);
                    listaCelulas.add(c);
                }

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction tx = manager.beginTransaction();
                tx.replace(R.id.frame_mapa, new MapaFragment(listaCelulas),MAPA_FRAGMENT_TAG);
                tx.commit();

                barra.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}