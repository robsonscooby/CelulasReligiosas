package br.com.celulasreligiosas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Celula;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class CelulaAdapter extends BaseAdapter {


    private final Context context;
    private final List<Celula> celulas;

    public CelulaAdapter(Context context, List<Celula> celulas) {
        this.context = context;
        this.celulas = celulas;
    }

    @Override
    public int getCount() {
        return celulas.size();
    }

    @Override
    public Object getItem(int i) {
        return celulas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return celulas.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Celula celula = celulas.get(i);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewCelula = view;
        if(null == viewCelula) {
            viewCelula = inflater.inflate(R.layout.lista_item, viewGroup, false);
        }
        TextView nome = (TextView) viewCelula.findViewById(R.id.item_nome);
        nome.setText(celula.getNome());

        TextView fone = (TextView) viewCelula.findViewById(R.id.item_telefone);
        fone.setText(celula.getTelefone());

        TextView end = (TextView) viewCelula.findViewById(R.id.item_endereco);
        if(null != end) {
            end.setText(celula.getEndereco());
        }
        TextView site = (TextView) viewCelula.findViewById(R.id.item_site);
        if(null != site) {
            site.setText(celula.getSite());
        }

        ImageView imagem = (ImageView) viewCelula.findViewById(R.id.item_foto);
        String caminhoFoto = celula.getCaminhoFoto();
        if(caminhoFoto != null) {
            Bitmap bm = BitmapFactory.decodeFile(celula.getCaminhoFoto());
            Bitmap bmReduzido = Bitmap.createScaledBitmap(bm, 100, 100, true);
            imagem.setImageBitmap(bmReduzido);
            imagem.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return viewCelula;
    }
}
