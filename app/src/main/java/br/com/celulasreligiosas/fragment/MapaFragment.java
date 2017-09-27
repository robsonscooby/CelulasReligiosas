package br.com.celulasreligiosas.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.entity.Celula;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String MATRIZ = "R. Trinta e Sete,237,Maranguape II, Paulista - PE" ;
    private List<Celula> listaCelulas = new ArrayList<Celula>();

    public MapaFragment(List<Celula> listaCelulas) {
        this.listaCelulas= listaCelulas;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = localizaNoMapa(MATRIZ);

        if(null != latLng) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
            googleMap.moveCamera(cameraUpdate);
        }

        if(!listaCelulas.isEmpty()){
            for(Celula item : listaCelulas){
                LatLng coordenada = localizaNoMapa(item.getEndereco());
                if(null != coordenada){
                    MarkerOptions marcador = new MarkerOptions();
                    marcador.position(coordenada);
                    marcador.title(item.getNome());
                    marcador.snippet(item.getDescricao());
                    googleMap.addMarker(marcador);
                }
            }
        }
    }

    private LatLng localizaNoMapa(String endereco) {
        Geocoder geo = new Geocoder(getContext());
        try {
            List<Address> local = geo.getFromLocationName(endereco, 1);
            if(!local.isEmpty()){
                return new LatLng(local.get(0).getLatitude(),local.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

