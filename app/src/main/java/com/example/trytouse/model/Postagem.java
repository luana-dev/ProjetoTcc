package com.example.trytouse.model;

import com.example.trytouse.config.ConfiguracaoFirebas;
import com.example.trytouse.helper.UsuarioFirebaseInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Postagem {
    private String id;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem() {

        DatabaseReference firebaseRef = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference postagemRef = firebaseRef.child("postagens");
        String idPostagem = postagemRef.push().getKey();
        setId( idPostagem );

    }

    public void salvar(){
        String uid = UsuarioFirebaseInfo.getIdUsuario();
        DatabaseReference database = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference postagemRef = database.child("postagens").child(uid).child(getId());

        Map<String, Object> valoresPostagem = converterParaMap();

        postagemRef.updateChildren(valoresPostagem);
    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> postagemMap = new HashMap<>();
        postagemMap.put("caminhoFoto", getCaminhoFoto());
        postagemMap.put("descricao", getDescricao());
        postagemMap.put("id", getId());
        return postagemMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
}
