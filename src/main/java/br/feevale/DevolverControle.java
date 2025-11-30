package br.feevale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class DevolverControle extends Navegar implements LerCSV {

    @FXML
    private TextField codLivro;

    @FXML
    private Label msg;

    private PauseTransition pause;

    private boolean confirma = false;

    @FXML
    public void initialize() {
        // Cria o PauseTransition
        pause = new PauseTransition(Duration.millis(500));

        // Define a ação quando o tempo expira
        pause.setOnFinished(event -> {
            String texto = codLivro.getText().trim();
            if (!texto.isEmpty()) {
                bancoDados();
            } else {
                msg.setText("");
            }
        });

        codLivro.textProperty().addListener((obs, antigo, novo) -> {
            pause.stop(); // reinicia o timer
            pause.playFromStart(); // começa a contar 500ms
        });
    }

    @Override
    public void bancoDados() {
        Livro livro = new Livro();
        ArrayList linhas = new ArrayList();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(DevolverControle.class.getResourceAsStream("/BLivros.csv")))) {

            if (br == null) {
                msg.setText("ERRO: BLivros.csv não encontrado!");
                return;
            }

            String codigoDigitado = codLivro.getText().trim();
            String linha;
            boolean encontrado = false;

            while ((linha = br.readLine()) != null) {
                String[] valores = linha.split(",");

                String codArquivo = valores[1].trim();

                // Tratamento para null ou vazio
                String idPessoaStr = valores[6].trim();
                int idPessoaLivro = 0;
                if (!idPessoaStr.equalsIgnoreCase("null") && !idPessoaStr.isEmpty()) {
                    idPessoaLivro = Integer.parseInt(idPessoaStr);
                }

                if (codigoDigitado.equals(codArquivo) && Sessao.getSessao().getIdPessoa() == idPessoaLivro) {
                    livro.setId(Integer.parseInt(valores[0].trim()));
                    livro.setCodigo(valores[1].trim());
                    livro.setTitulo(valores[2].trim());
                    livro.setAutor(valores[3].trim());
                    livro.setLocal(valores[4].trim());
                    livro.setImagem(valores[5].trim());
                    livro.setIdPessoa(idPessoaLivro);

                    msg.setText("Livro: " + livro.getTitulo());
                    encontrado = true;

                    if (confirma) {
                        valores[6] = "0";
                        valores[7] = " ";
                    }
                    linha = String.join(",", valores);
                }
                linhas.add(linha);
            }

            if (!encontrado) {
                msg.setText("Código inválido ou livro não pertence à sua sessão!");
                confirma = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            msg.setText("Erro ao ler o arquivo!");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            msg.setText("Erro nos dados do CSV!");
        }
        if (confirma && livro.getId() != 0) {
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("src/main/resources/BLivros.csv"))) {

                for (Object l : linhas) {
                    bw.write(l.toString());
                    bw.newLine();
                }
                msg.setText("Reservado com sucesso!");

            } catch (IOException e) {
                e.printStackTrace();
                msg.setText("Erro ao salvar alterações!");
            }
            confirma = false; // evita reescrever toda vez
        }
    }

    public void devolver() {
        confirma = true;
        bancoDados();
    }

}
