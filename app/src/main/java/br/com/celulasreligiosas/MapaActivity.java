package br.com.celulasreligiosas;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MapaActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Celula> listaCelulas = new ArrayList<Celula>();
    private ProgressBar barra;

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
        getMenuInflater().inflate(R.menu.menu_inicial,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_inicial:
                Intent intentCadastro = new Intent(this,CadastroActivity.class);
                startActivity(intentCadastro);
                break;
        }

        return super.onOptionsItemSelected(item);
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
                tx.replace(R.id.frame_mapa, new MapaFragment(listaCelulas));
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