package com.example.trytouse.model;

import com.example.trytouse.config.ConfiguracaoFirebas;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Conversa implements Serializable {
    private String idRemetente;
    private String idDestinatario;
    private String ultimamsg;
    private Usuario usuarioExib;

    public Conversa() {
    }

    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebas.getFirebaseDatabase();
        DatabaseReference conversasRef = database.child("conversas");

        conversasRef.child(this.getIdRemetente())
                .child(this.getIdDestinatario())
                .setValue(this);
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimamsg() {
        return ultimamsg;
    }

    public void setUltimamsg(String ultimamsg) {
        this.ultimamsg = ultimamsg;
    }

    public Usuario getUsuarioExib() {
        return usuarioExib;
    }

    public void setUsuarioExib(Usuario usuarioExib) {
        this.usuarioExib = usuarioExib;
    }
}
