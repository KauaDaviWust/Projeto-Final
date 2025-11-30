package br.feevale;

public class Usuario {
  private int idPessoa;
  private String nome;
  private String codigo;


    public Usuario(String codigo, int idPessoa, String nome) {
        this.codigo = codigo;
        this.idPessoa = idPessoa;
        this.nome = nome;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
