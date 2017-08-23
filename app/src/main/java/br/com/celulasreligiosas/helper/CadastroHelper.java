package br.com.celulasreligiosas.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.celulasreligiosas.CadastroActivity;
import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Celula;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class CadastroHelper {

    private final EditText nome;
    private final EditText end;
    private final EditText tel;
    private final EditText site;
    private final EditText desc;
    private final ImageView foto;
    private Celula celula;

    public CadastroHelper(CadastroActivity activity){
        celula = new Celula();
        nome = (EditText) activity.findViewById(R.id.cadastro_nome);
        end = (EditText) activity.findViewById(R.id.cadastro_endereco);
        tel = (EditText) activity.findViewById(R.id.cadastro_telefone);
        site = (EditText) activity.findViewById(R.id.cadastro_site);
        desc = (EditText) activity.findViewById(R.id.cadastro_descricao);
        foto = (ImageView) activity.findViewById(R.id.cadastro_foto);
    }

    public Celula pegaCelula(){
        if(validaPreencimentoCampos()) {
            celula.setNome(nome.getText().toString());
            celula.setEndereco(end.getText().toString());
            celula.setTelefone(tel.getText().toString());
            celula.setSite(site.getText().toString());
            celula.setDescricao(desc.getText().toString());
            celula.setCaminhoFoto((String) foto.getTag());
            return celula;
        } else {
            return null;
        }
    }

    public void preencheCadastro(Celula celula) {
        if(null != celula) {
            this.celula = celula;
            nome.setText(celula.getNome());
            end.setText(celula.getEndereco());
            tel.setText(celula.getTelefone());
            site.setText(celula.getSite());
            desc.setText(celula.getDescricao());
            carregaImagem(celula.getCaminhoFoto());
        }
    }

    public void carregaImagem(String caminhoFoto) {
        if(null != caminhoFoto) {
            Bitmap bm = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bmReduzido = Bitmap.createScaledBitmap(bm, 300, 300, true);
            foto.setImageBitmap(bmReduzido);
            foto.setScaleType(ImageView.ScaleType.FIT_XY);
            foto.setTag(caminhoFoto);
        }
    }

    private boolean validaPreencimentoCampos(){

        if(nome.getText().toString().trim().isEmpty()
        || end.getText().toString().trim().isEmpty()
        || tel.getText().toString().trim().isEmpty()
        || site.getText().toString().trim().isEmpty()
        || desc.getText().toString().trim().isEmpty()){
            return false;
        } else{
            return true;
        }
    }

}

