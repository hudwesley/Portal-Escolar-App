package com.example.portalescolar;

public class Aluno {
    private int idAluno;
    private String nome, email, nomeTurma;
    private int idTurma;

    public Aluno(String nome, String email, int idTurma) {
        this.nome = nome;
        this.email = email;
        this.idTurma = idTurma;
    }

    public Aluno(int idAluno, String nome, String email, int idTurma) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.idTurma = idTurma;
    }

    public Aluno(int idAluno, String nome, String email, String nomeTurma) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.nomeTurma = nomeTurma;
    }

    // Getter e Setter

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
}
