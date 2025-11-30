package br.feevale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CadastraUsuarioControle {

    @FXML
    private TextField codigo;
    @FXML
    private TextField nome;
    @FXML
    private TextField senha;
    @FXML
    private Label msg;

    @FXML
    public void confirma() {

        String cod = codigo.getText().trim();
        String nom = nome.getText().trim();
        String sen = senha.getText().trim();

        if (cod.isEmpty() || nom.isEmpty() || sen.isEmpty()) {
            msg.setText("Preencha todos os campos!");
            return;
        }

        ArrayList<String> linhas = new ArrayList<>();
        boolean repetido = false;
        int ultimoId = 0;


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        CadastraUsuarioControle.class.getResourceAsStream("/BPessoa.csv")))) {

            if (br == null) {
                msg.setText("ERRO: BPessoa.csv não encontrado!");
                return;
            }

            String linha;
            while ((linha = br.readLine()) != null) {

                if (linha.isBlank())
                    continue;

                String[] p = linha.split(",");

                int id = Integer.parseInt(p[0].trim());
                String codArq = p[1].trim();

                ultimoId = Math.max(ultimoId, id);

                if (cod.equals(codArq)) {
                    repetido = true;
                }

                linhas.add(linha);
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg.setText("Erro ao ler BPessoa.csv!");
            return;
        }

        if (repetido) {
            msg.setText("Código já existe!");
            return;
        }

        int novoId = ultimoId + 1;
        String novaLinha = novoId + "," + cod + "," + nom + "," + sen;

        linhas.add(novaLinha);

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("src/main/resources/BPessoa.csv"))) {

            for (String l : linhas) {
                bw.write(l);
                bw.newLine();
            }

            msg.setText("Usuário cadastrado!");

            codigo.clear();
            nome.clear();
            senha.clear();

        } catch (IOException e) {
            e.printStackTrace();
            msg.setText("Erro ao salvar BPessoa.csv!");
        }
    }

    @FXML
    private void irInicio() throws IOException {
        AppAdm.setRoot("inicioAdm");
    }

}
