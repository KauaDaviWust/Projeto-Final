package br.feevale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControle implements LerCSV{
    @FXML
    private Label msgErro;

    @FXML
    private TextField codigo;

    @FXML
    private PasswordField senha;

    private String txtCodigo;
    private String txtSenha;


    @FXML
    public void fazerLogin() {
        this.txtCodigo = codigo.getText().trim();
        this.txtSenha = senha.getText().trim();
        bancoDados();
    }

    @Override
    public void bancoDados() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(LoginControle.class.getResourceAsStream("/BPessoa.csv")))) {

            if (br == null) {
                System.out.println("ERRO: BPessoa.csv não foi encontrado na pasta resources!");
                return;
            }

            String linha;

            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");

                String codArquivo = valores[1].trim();
                String senhaArquivo = valores[3].trim();

                if (txtCodigo.equals(codArquivo) && txtSenha.equals(senhaArquivo)) {
                    Usuario p = new Usuario(valores[1], Integer.parseInt(valores[0]), valores[2]);
                    Sessao.setSessao(p);
                    App.setRoot("perfil");
                    return;
                }
            }

            msgErro.setText("Senha ou código invalidos!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
