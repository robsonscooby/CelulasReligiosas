package br.com.celulasreligiosas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import br.com.celulasreligiosas.entity.Noticia;
import br.com.celulasreligiosas.task.CadastarNoticaAsyncTask;

public class CadastroNoticiaActivity extends AppCompatActivity {

    private EditText autor, titulo, site, descricao;
    private Button btGaleria;
    private ImageView ivSelectedImage;
    private static final int REQUEST_CODE = 5678;
    private Button btCadastrar;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_noticia);

        autor = (EditText) findViewById(R.id.noticia_autor);
        titulo = (EditText) findViewById(R.id.noticia_titulo);
        site = (EditText) findViewById(R.id.noticia_site);
        descricao = (EditText) findViewById(R.id.noticia_descricao);

        ivSelectedImage = (ImageView) findViewById(R.id.noticia_imagem);
        btGaleria = (Button) findViewById(R.id.noticia_btn_galeria);

        btGaleria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(CadastroNoticiaActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CadastroNoticiaActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else{
                    registerForContextMenu(btGaleria);
                    openContextMenu(btGaleria);
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Selecione imagem"), REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Bitmap bitmap = ((BitmapDrawable) ivSelectedImage.getDrawable()).getBitmap();

        if (null != bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String imageString = Base64.encodeToString(byteArray,Base64.DEFAULT);

            Noticia noticia = new Noticia(autor.getText().toString().trim(),
                    titulo.getText().toString().trim(),
                    descricao.getText().toString().trim(),
                    site.getText().toString().trim(),imageString);

            CadastarNoticaAsyncTask task = new CadastarNoticaAsyncTask();
            task.execute(noticia);
            Toast.makeText(this, "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Favor adicionar uma foto.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {
            carregarImagemGaleria(data);
        }
    }

    public void carregarImagemGaleria(Intent data){
        InputStream stream = null;

        try {
            if(bitmap != null){
                bitmap.recycle();
            }
            stream = getContentResolver().openInputStream(data.getData());
            bitmap = BitmapFactory.decodeStream(stream);
            ivSelectedImage.setImageBitmap(bitmap);

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
