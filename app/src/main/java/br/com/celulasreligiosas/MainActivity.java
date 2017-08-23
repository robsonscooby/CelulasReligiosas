package br.com.celulasreligiosas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MainActivity extends AppCompatActivity {

    public static final int NUMERO_MONITOR_PHONE = 4844;
    private  String[] listaNome = {"Tela Mapa",
                                    "Tela Cadastro",
                                    "Tela Lista Celulas",
                                    "Tela Notícias"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},NUMERO_MONITOR_PHONE);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},NUMERO_MONITOR_PHONE);
            }
        }else{

        }

        CircleMenu circle = (CircleMenu) findViewById(R.id.circleTela);
        circle.setMainMenu(Color.parseColor("#00857c"),R.mipmap.add,R.mipmap.ic_remover)
                .addSubMenu(Color.parseColor("#00857c"),R.drawable.ic_mapa)
                .addSubMenu(Color.parseColor("#00857c"),R.drawable.ic_mapa)
                .addSubMenu(Color.parseColor("#00857c"),R.drawable.ic_mapa)
                .addSubMenu(Color.parseColor("#00857c"),R.drawable.ic_mapa).setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int i) {
                Toast.makeText(MainActivity.this,"Item selecionado: "+listaNome[i],Toast.LENGTH_SHORT).show();

                switch (i){
                    case 0 :
                        Intent telaMapa = new Intent(MainActivity.this, MapaActivity.class);
                        startActivity(telaMapa);
                        break;
                    case 1 :
                        Intent telaCadastro = new Intent(MainActivity.this, CadastroActivity.class);
                        startActivity(telaCadastro);
                        break;
                    case 2 :
                        Intent telaLista = new Intent(MainActivity.this, CelulasActivity.class);
                        startActivity(telaLista);
                        break;
                    case 3 :
                        Intent it = new Intent(MainActivity.this, NoticiasActivity.class);
                        startActivity(it);
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case NUMERO_MONITOR_PHONE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permição concedida!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"Permição negada!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

//    public void telaMapa(View view){
//        Intent telaMapa = new Intent(this, MapaActivity.class);
//        startActivity(telaMapa);
//    }

//    public void telaCadastro(View view){
//        Intent telaCadastro = new Intent(this, CadastroActivity.class);
//        startActivity(telaCadastro);
//    }

//    public void telaListaCelulas(View view){
//        Intent telaLista = new Intent(this, CelulasActivity.class);
//        startActivity(telaLista);
//    }

    public void telaNoticias(View view){
        Intent it = new Intent(this, NoticiasActivity.class);
        startActivity(it);
    }

}
