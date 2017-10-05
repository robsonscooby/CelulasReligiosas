package br.com.celulasreligiosas.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.entity.Celula;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String MATRIZ = "R. Trinta e Sete,237,Maranguape II, Paulista - PE" ;
    private static String mGlobalBusca = "";
    private GoogleMap mGoogleMap = null;
    private CameraPosition mCameraPosition = null;
    private List<Celula> listaCelulas = new ArrayList<Celula>();
    private DatabaseReference databaseReference;

    public MapaFragment(List<Celula> listaCelulas) {
        this.listaCelulas = listaCelulas;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        atualizarMap();
    }


    private LatLng localizaNoMapa(String endereco) {
        if(null != endereco) {
            Geocoder geo = new Geocoder(getContext());
            try {
                List<Address> local = geo.getFromLocationName(endereco, 1);
                if (!local.isEmpty()) {
                    return new LatLng(local.get(0).getLatitude(), local.get(0).getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void buscarCelulas(String busca,DatabaseReference databaseReference){
        this.databaseReference = databaseReference;
        mGlobalBusca = busca.toLowerCase().replace("%", "").replace("*", "")
                .replace("delete", "")
                .replace("select", "")
                .replace("update", "")
                .replace("insert", "")
                .replace("where", "");
        mCameraPosition = mGoogleMap.getCameraPosition();
        atualizarMap(mCameraPosition);
    }

    public void buscarCelulas(){
        mGlobalBusca = null;
        mCameraPosition = mGoogleMap.getCameraPosition();
        atualizarMap(mCameraPosition);
    }

    private void atualizarMap(){
        atualizarMap(null, false);
    }

    private void atualizarMap(CameraPosition cameraPosition){
        atualizarMap(cameraPosition, true);
    }

    private void atualizarMap(CameraPosition cameraPosition, boolean clearFirst){
        LatLng latLng = localizaNoMapa(MATRIZ);

        if (clearFirst) {
            if (mGoogleMap != null){
                mGoogleMap.clear();
            }
        }
        if (cameraPosition == null) {
            if (null != latLng) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                mGoogleMap.moveCamera(cameraUpdate);
            }
        }else{
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        }

        if (mGlobalBusca == null || mGlobalBusca.isEmpty()) {
            findAllCell();
        }else{
            findCell(mGlobalBusca);
        }
    }

    private void findCell(final String busca){
        if(null != databaseReference) {
            //chamada que dispensa a criacao de uma nova asynctask para consulta no banco firebase
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    listaCelulas.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Celula c = item.getValue(Celula.class);
                        String nomeCelula = c.getNome().toLowerCase();
                        if (nomeCelula.contains(busca)) {
                            listaCelulas.add(c);
                            LatLng coordenada = localizaNoMapa(c.getEndereco());
                            if(null != coordenada){
                                MarkerOptions marcador = new MarkerOptions();
                                marcador.position(coordenada);
                                marcador.title(c.getNome());
                                marcador.snippet(c.getDescricao());
                                mGoogleMap.addMarker(marcador);
                            }
                        }
                    }
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
    public void findAllCell() {
        if (null != databaseReference) {
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    listaCelulas.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Celula c = item.getValue(Celula.class);
                        listaCelulas.add(c);
                        LatLng coordenada = localizaNoMapa(c.getEndereco());
                        if (null != coordenada) {
                            MarkerOptions marcador = new MarkerOptions();
                            marcador.position(coordenada);
                            marcador.title(c.getNome());
                            marcador.snippet(c.getDescricao());
                            mGoogleMap.addMarker(marcador);
                        }
                    }
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
        }else{
            if(!listaCelulas.isEmpty()){
                for(Celula item : listaCelulas){
                    LatLng coordenada = localizaNoMapa(item.getEndereco());
                    if(null != coordenada){
                        MarkerOptions marcador = new MarkerOptions();
                        marcador.position(coordenada);
                        marcador.title(item.getNome());
                        marcador.snippet(item.getDescricao());
                        mGoogleMap.addMarker(marcador);
                    }
                }
            }
        }
    }

    public void recarregarMapa() {
        if(!listaCelulas.isEmpty()){
            for(Celula item : listaCelulas){
                LatLng coordenada = localizaNoMapa(item.getEndereco());
                if(null != coordenada){
                    MarkerOptions marcador = new MarkerOptions();
                    marcador.position(coordenada);
                    marcador.title(item.getNome());
                    marcador.snippet(item.getDescricao());
                    mGoogleMap.addMarker(marcador);
                }
            }
        }
    }

}

