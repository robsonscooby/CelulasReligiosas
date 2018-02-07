package br.com.celulasreligiosas.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by robson.carlos.santos on 29/08/2017.
 */

@Entity
public class Noticia implements Serializable {

	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "id")
	private int codigo;
	private String autor;
	private String titulo;
	private String descricao;
	private String url;
	private String foto;

	public Noticia(){

	}

	@Ignore
	public Noticia(int codigo, String autor, String titulo, String descricao, String url, String foto ) {
		super();
		this.codigo = codigo;
		this.autor = autor;
		this.titulo = titulo;
		this.descricao = descricao;
		this.url = url;
		this.foto = foto;
	}

	@Ignore
	public Noticia(String autor, String titulo, String descricao, String url, String foto) {
		super();
		this.autor = autor;
		this.titulo = titulo;
		this.descricao = descricao;
		this.url = url;
		this.foto = foto;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}
