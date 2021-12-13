package com.example.trytouse.model;

import com.example.trytouse.activity.ConfiguracoesActivityWhats;
import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String id;
    private String Nome;
    private String Senha;
    private String Email;
    private String Telefone;
    private String foto;
    private String email_insta;
    private String senha_insta;
    private String caminhoFoto;
    private int seguidores = 0;
    private int seguindo = 0;
    private int postagens = 0;

    public Usuario() {
    }


    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference usuario = firebaseRef.child("usuarios").child(getId());

        usuario.setValue(this);
    }

    public void salvar_insta(){
        DatabaseReference firebase = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference insta_infos = firebase.child("infos_insta").child(getId());

        insta_infos.setValue(this);
    }

    public void atualizarQtdPostagem(){

        DatabaseReference firebaseRef = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef
                .child("usuarios")
                .child( getId() );

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("postagens", getPostagens() );

        usuariosRef.updateChildren( dados );

    }

    public void atualizar(){
        Usuario usuarioLogado = UsuarioFirebaseInfo.getUsuarioLogadoDados();
        DatabaseReference database = ConfiguracaoFirebas.getFirebaseDatabase();

        DatabaseReference usuariosRef = database.child("usuarios")
                .child(usuarioLogado.getId());
        Map<String, Object> valoresUser = converterParaMap();

        usuariosRef.updateChildren(valoresUser);
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("senha", getSenha());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("caminhoFoto", getCaminhoFoto() );
        usuarioMap.put("seguidores", getSeguidores() );
        usuarioMap.put("seguindo", getSeguindo() );
        usuarioMap.put("postagens", getPostagens() );
        usuarioMap.put("id", getId());
        return usuarioMap;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getPostagens() {
        return postagens;
    }

    public void setPostagens(int postagens) {
        this.postagens = postagens;
    }

    public String getEmail_insta() {
        return email_insta;
    }

    public void setEmail_insta(String email_insta) {
        this.email_insta = email_insta;
    }

    public String getSenha_insta() {
        return senha_insta;
    }

    public void setSenha_insta(String senha_insta) {
        this.senha_insta = senha_insta;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome.toUpperCase();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }
}
