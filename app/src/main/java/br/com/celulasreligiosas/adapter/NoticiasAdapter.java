package br.com.celulasreligiosas.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Noticia;

/**
 * Created by robson.carlos.santos on 19/08/2017.
 */

public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private List<Noticia> listaNoticias = new ArrayList<>();
    private final OnTaskClickedListener listener;

    public NoticiasAdapter(OnTaskClickedListener onTaskClickedListener) {
        this.listener = onTaskClickedListener;
    }

    public void replaceItems(List<Noticia> listaNoticias) {
        this.listaNoticias = listaNoticias;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lista_item_noticia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Noticia noticia = listaNoticias.get(position);
        holder.nome.setText(noticia.getTitulo());
        holder.fone.setText(noticia.getAutor());
//        if(null != holder.end) {
//            holder.end.setText(noticia.getDescricao());
//        }

        if(null != noticia.getFoto()) {
            byte[] bt = Base64.decode(noticia.getFoto(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
            holder.imagem.setImageBitmap(bitmap);
        }

        holder.view.setOnClickListener(view -> listener.onClick(noticia));
        holder.view.setOnLongClickListener(view -> {
            listener.onLongClick(noticia);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaNoticias.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView nome;
        TextView fone;
        //TextView end;
        ImageView imagem;

        public ViewHolder(View v) {
            super(v);
            view = v;
            nome = (TextView) v.findViewById(R.id.item_nome);
            fone = (TextView) v.findViewById(R.id.item_telefone);
            //end = (TextView) v.findViewById(R.id.item_endereco);
            imagem = (ImageView) v.findViewById(R.id.item_foto);
        }

    }

    public interface OnTaskClickedListener {
        public void onClick(Noticia noticia);
        public void onLongClick(Noticia noticia);
    }
}
