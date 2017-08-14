package br.com.celulasreligiosas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.celulasreligiosas.entity.Celula;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class CelulasDAO extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "celulas_religiosas";

    public CelulasDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE celula (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, descricao TEXT, caminho_foto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void insert(Celula celula){
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues dados = setCelula(celula);
        db.insert("celula",null,dados);
    }

    @NonNull
    private ContentValues setCelula(Celula celula) {
        ContentValues dados = new ContentValues();

        dados.put("nome",celula.getNome());
        dados.put("endereco",celula.getEndereco());
        dados.put("telefone",celula.getTelefone());
        dados.put("site",celula.getSite());
        dados.put("descricao",celula.getDescricao());
        dados.put("caminho_foto",celula.getCaminhoFoto());
        return dados;
    }

    public List<Celula> findAll(){
        List<Celula> listaCelula = new ArrayList<Celula>();
        SQLiteDatabase db =  getWritableDatabase();
        String sql = "SELECT * FROM celula;";
        Cursor c = db.rawQuery(sql,null);
        Celula celula;
        while(c.moveToNext()){
            celula = new Celula();
            celula.setId(c.getLong(c.getColumnIndex("id")));
            celula.setNome(c.getString(c.getColumnIndex("nome")));
            celula.setEndereco(c.getString(c.getColumnIndex("endereco")));
            celula.setTelefone(c.getString(c.getColumnIndex("telefone")));
            celula.setSite(c.getString(c.getColumnIndex("site")));
            celula.setDescricao(c.getString(c.getColumnIndex("descricao")));
            celula.setCaminhoFoto(c.getString(c.getColumnIndex("caminho_foto")));
            listaCelula.add(celula);
        }
        c.close();

        return listaCelula;
    }

    public void delete(Celula celula){
        SQLiteDatabase db =  getWritableDatabase();
        String[] params = {celula.getId().toString()};
        db.delete("celula","id = ?",params);
    }

    public void update(Celula celula) {
        SQLiteDatabase db =  getWritableDatabase();
        ContentValues dados = setCelula(celula);
        String[] params = {celula.getId().toString()};
        db.update("celula",dados,"id = ?",params);
    }

    public boolean findPhone(String telefone) {
        SQLiteDatabase db =  getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM celula WHERE telefone = ?",new String[]{telefone});
        int tamanho = c.getCount();
        c.close();
        return tamanho > 0 ;
    }

}
