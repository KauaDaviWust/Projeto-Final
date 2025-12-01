package br.feevale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CadastraLivroControle {

    @FXML
    private TextField codigo;
    @FXML
    private TextField nome;
    @FXML
    private TextField autor;
    @FXML
    private TextField local;
    @FXML
    private Label msg;
    private static final String ARQUIVO_LOCAL = "src/main/resources/BLivros.csv";

    @FXML
    public void confirma() {
        // valida se os campos estão vazios
        try {

            String cod = codigo.getText().trim();
            String nom = nome.getText().trim();
            String aut = autor.getText().trim();
            String loc = local.getText().trim();

            if (cod.isEmpty() || nom.isEmpty() || aut.isEmpty() || loc.isEmpty()) {
                msg.setText("Preencha todos os campos!");
                return;
            }

            boolean codigoRepetido = false;
            int ultimoId = 0;

            // Lê BLivros na pasta resources
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            CadastraLivroControle.class.getResourceAsStream("/BLivros.csv")))) {

                String linha;
                while ((linha = br.readLine()) != null && !linha.isBlank()) {

                    String[] partes = linha.split(",");

                    int idAtual = Integer.parseInt(partes[0].trim());
                    ultimoId = Math.max(ultimoId, idAtual);

                    String codigoCSV = partes[1].trim();

                    if (codigoCSV.equals(cod)) {
                        codigoRepetido = true;
                    }
                }
            }
            // caso o código for repetido
            if (codigoRepetido) {
                msg.setText("Código já existe!");
                return;
            }

            int novoId = ultimoId + 1;

            String novaLinha = novoId + "," +
                    cod + "," +
                    nom + "," +
                    aut + "," +
                    loc + "," +
                    " ," +
                    "0," +
                    " ";

            // escreve no arquivo BLivros
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_LOCAL, true))) {
                bw.write(novaLinha);
                bw.newLine();
            }

            msg.setText("Livro cadastrado com sucesso!");

            // limpa os campos
            codigo.clear();
            nome.clear();
            autor.clear();
            local.clear();

        } catch (Exception e) {
            e.printStackTrace();
            msg.setText("Erro ao salvar no CSV!");
        }
    }
    //retorna ao inicio
    @FXML
    private void irInicio() throws IOException {
        AppAdm.setRoot("inicioAdm");
    }
}
