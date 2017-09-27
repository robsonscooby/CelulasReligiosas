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
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MainActivity extends AppCompatActivity {

    public static final int NUMERO_MONITOR_PHONE = 4844;
    private  String[] listaNome = {"Mapa",
                                    "Cadastro",
                                    "Lista todas Celulas cadastradas",
                                    "Notícias mundo",
                                    "Notícias Evangelícas"};

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
        }

        //Botoes da tela inicial
        CircleMenu circle = (CircleMenu) findViewById(R.id.circleTela);
        circle.setMainMenu(Color.parseColor("#80e8dd"),R.drawable.ic_add,R.drawable.ic_remove)
                .addSubMenu(Color.parseColor("#FFEA5752"),R.drawable.ic_mundo)
                .addSubMenu(Color.parseColor("#FFEABA52"),R.drawable.ic_cadastro)
                .addSubMenu(Color.parseColor("#FFEADE52"),R.drawable.ic_celulas)
                .addSubMenu(Color.parseColor("#FF5B7ADF"),R.drawable.ic_news)
                .addSubMenu(Color.parseColor("#FFA25BDF"),R.drawable.ic_action_name).setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int i) {
                Toast.makeText(MainActivity.this,listaNome[i],Toast.LENGTH_SHORT).show();

                Intent it;
                switch (i){
                    case 0 :
                        it = new Intent(MainActivity.this, MapaActivity.class);
                        startActivity(it);
                        break;
                    case 1 :
                        it = new Intent(MainActivity.this, CadastroActivity.class);
                        startActivity(it);
                        break;
                    case 2 :
                        it = new Intent(MainActivity.this, CelulasActivity.class);
                        startActivity(it);
                        break;
                    case 3 :
                        it = new Intent(MainActivity.this, NoticiasActivity.class);
                        startActivity(it);
                        break;
                    case 4 :
                        it = new Intent(MainActivity.this, CadastroNoticiaActivity.class);
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

}
