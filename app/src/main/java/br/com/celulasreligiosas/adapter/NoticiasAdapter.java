package br.com.celulasreligiosas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Noticia;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class NoticiasAdapter extends BaseAdapter {

    private List<Noticia> listaNoticias;
    private Context context;

    public NoticiasAdapter(List<Noticia> listaNoticias, Context context) {
        this.listaNoticias = listaNoticias;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaNoticias.size();
    }

    @Override
    public Object getItem(int i) {
        return listaNoticias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Noticia item = listaNoticias.get(i);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewNoticia = view;
        if(null == viewNoticia) {
            viewNoticia = inflater.inflate(R.layout.lista_item_noticia, viewGroup, false);
        }

        TextView nome = (TextView) viewNoticia.findViewById(R.id.item_nome);
        nome.setText(item.getTitulo());

        TextView fone = (TextView) viewNoticia.findViewById(R.id.item_telefone);
        fone.setText(item.getAutor());

        TextView end = (TextView) viewNoticia.findViewById(R.id.item_endereco);
        if(null != end) {
            end.setText(item.getDescricao());
        }

        if(null != item.getFoto()) {
            ImageView imagem = (ImageView) viewNoticia.findViewById(R.id.item_foto);
            byte[] bt = Base64.decode(item.getFoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            imagem.setImageBitmap(bitmap);
        }
        return viewNoticia;
    }
}
