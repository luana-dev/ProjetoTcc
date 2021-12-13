package com.example.trytouse.model;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class Mensagens {
  private String idUsuario;
  private String mensagem;
  private String img;

    public Mensagens() {
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

