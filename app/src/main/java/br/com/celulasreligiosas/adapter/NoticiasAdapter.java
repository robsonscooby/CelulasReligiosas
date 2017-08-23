package br.com.celulasreligiosas.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Article;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class NoticiasAdapter extends BaseAdapter {

    private List<Article> listaNoticias;
    private Context context;

    public NoticiasAdapter(List<Article> listaNoticias, Context context) {
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
        Article item = listaNoticias.get(i);

        LayoutInflater inflater = LayoutInflater.from(context);
        View viewNoticia = view;
        if(null == viewNoticia) {
            viewNoticia = inflater.inflate(R.layout.lista_item_noticia, viewGroup, false);
        }

        TextView nome = (TextView) viewNoticia.findViewById(R.id.item_nome);
        nome.setText(item.getTitle());

        TextView fone = (TextView) viewNoticia.findViewById(R.id.item_telefone);
        fone.setText(item.getAuthor());

        TextView end = (TextView) viewNoticia.findViewById(R.id.item_endereco);
        if(null != end) {
            end.setText(item.getDescription());
        }

        ImageView imagem = (ImageView) viewNoticia.findViewById(R.id.item_foto);
        Picasso.with(context)
                .load(item.getUrlToImage())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(imagem);

        return viewNoticia;
    }
}
