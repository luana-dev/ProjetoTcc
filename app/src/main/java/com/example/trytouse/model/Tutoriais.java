package com.example.trytouse.model;

public class Tutoriais {
    private String tutorial;
    private String andamento;
    private String ano;

    public Tutoriais(String tutorial, String andamento, String ano) {
        this.tutorial = tutorial;
        this.andamento = andamento;
        this.ano = ano;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getAndamento() {
        return andamento;
    }

    public void setAndamento(String andamento) {
        this.andamento = andamento;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
