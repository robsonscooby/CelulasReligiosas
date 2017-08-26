package br.com.celulasreligiosas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.celulasreligiosas.dao.CelulasDAO;
import br.com.celulasreligiosas.entity.Celula;
import br.com.celulasreligiosas.helper.CadastroHelper;
import br.com.celulasreligiosas.task.HttpUrlConnectionAsyncTask;

public class CadastroActivity extends AppCompatActivity {

    public static final String URL_WEBSERVICE = "https://viacep.com.br/ws/##cep##/json/";
    public static final int CODIGO_FOTO = 4845;
    private CadastroHelper cadastroHelper;
    private String caminhoFoto;
    private TextView cep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastroHelper = new CadastroHelper(this);

        Intent intent = getIntent();
        Celula celula = (Celula) intent.getSerializableExtra("celula");
        cadastroHelper.preencheCadastro(celula);

        cep = (TextView) findViewById(R.id.cadastro_cep);

        Button botaoFoto = (Button) findViewById(R.id.cadastro_btn_foto);
        botaoFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null)+"/"+ System.currentTimeMillis()+".jpg";
                File file = new File(caminhoFoto);
                tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(tirarFoto,CODIGO_FOTO);
            }
        });
    }

    public void buscaEndereco(View view){
        HttpUrlConnectionAsyncTask task = new HttpUrlConnectionAsyncTask(this);
        String prepara = URL_WEBSERVICE;
        prepara = prepara.replaceAll("##cep##",cep.getText().toString());
        task.execute(prepara);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK && requestCode == CODIGO_FOTO){
            cadastroHelper.carregaImagem(caminhoFoto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Celula celula = cadastroHelper.pegaCelula();

        if(null != celula) {
            switch (item.getItemId()) {
                case R.id.menu_cadastro_ok:

                    CelulasDAO dao = new CelulasDAO(this);

                    if (celula.getId() != null) {
                        dao.update(celula);
                    } else {
                        dao.insert(celula);
                    }

                    dao.close();

                    Toast.makeText(this, "Celula " + celula.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

            }
        } else {
            Toast.makeText(this, "Favor preencher todos os campos.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
