package br.feevale;

public class Sessao {

    private static Usuario usuarioLogado;

    public static Usuario getSessao() {
        return usuarioLogado;
    }

    public static void setSessao(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void limpar() {
        usuarioLogado = null;
    }
}
