package br.com.celulasreligiosas.entity;

import java.io.Serializable;

/**
 * Created by robson.carlos.santos on 13/08/2017.
 */

public class Celula implements Serializable{

    private String uid;
    private String nome;
    private String endereco;
    private String telefone;
    private String site;
    private String descricao;
    private String caminhoFoto;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @Override
    public String toString() {
        return "Celula{" +
                "uid=" + uid +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", site='" + site + '\'' +
                ", descricao='" + descricao + '\'' +
                ", caminhoFoto='" + caminhoFoto + '\'' +
                '}';
    }

}
