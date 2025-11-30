package br.feevale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class PerfilControle implements LerCSV {

    @FXML
    private Label nome;

    @FXML
    private GridPane tabela;

    private ArrayList<Livro> listaDeLivros = new ArrayList();

    @FXML
    private void initialize() {
        Usuario usuario = Sessao.getSessao(); // pega o usuário global

        if (usuario != null) {
            nome.setText(usuario.getNome());
        }

        carregarLivros();
    }

    @FXML
    private void irRetirada() throws IOException {
        App.setRoot("retirada");
    }

    @FXML
    private void irDevolver() throws IOException {
        App.setRoot("Devolucao");
    }

    @FXML
    private void sair() throws IOException {
        Sessao.setSessao(null);
        App.setRoot("login");
    }

    public void bancoDados() {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/BLivros.csv"), "UTF-8"))) {

            String linha;
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while ((linha = br.readLine()) != null) {
                Livro l = new Livro();
                String[] valores = linha.split(",");

                int idPessoa = Integer.parseInt(valores[6]);
                String dataCsv = valores[7];

                if (idPessoa == Sessao.getSessao().getIdPessoa()) {

                    l.setId(Integer.parseInt(valores[0]));
                    l.setCodigo(valores[1]);
                    l.setTitulo(valores[2]);
                    l.setAutor(valores[3]);
                    l.setLocal(valores[4]);
                    l.setImagem(valores[5]);
                    l.setIdPessoa(idPessoa);

                    // --- DATA ---
                    if (dataCsv.isEmpty()) {
                        l.setDataDevolucao(null);
                    } else {
                        l.setDataDevolucao(LocalDate.parse(dataCsv, formatador));
                    }

                    listaDeLivros.add(l);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bancoDados(int idLivro) {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/BLivros.csv"), "UTF-8"))) {

            ArrayList<String> linhas = new ArrayList<>();
            String linha;
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while ((linha = br.readLine()) != null) {

                String[] valores = linha.split(",");

                int id = Integer.parseInt(valores[0]);

                if (id == idLivro) {

                    String dataCsv = valores[7];
                    LocalDate novaData;

                    novaData = LocalDate.now().plusDays(14);

                    valores[7] = novaData.format(formatador);

                    linha = String.join(",", valores);
                }

                linhas.add(linha);
            }

            // reescreve csv
            java.nio.file.Files.write(
                    java.nio.file.Paths.get("src/main/resources/BLivros.csv"),
                    linhas,
                    java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void carregarLivros() {
        bancoDados();
        int linha = 1; 

        // Imagem
        for (Livro l : listaDeLivros) {
            ImageView img;

            String nomeImg = l.getImagem();

            if (nomeImg == null || nomeImg.trim().isEmpty()) {
                img = new ImageView();
            } else {
                img = new ImageView(new Image(
                        getClass().getResourceAsStream("/br/feevale/imgs/" + nomeImg)));
                img.setFitHeight(150);
                img.setFitWidth(100);
                img.setPreserveRatio(true);
                GridPane.setHalignment(img, HPos.CENTER);
            }
            
            Label titulo = new Label(l.getTitulo());
            titulo.setStyle("-fx-font-size: 15;");
            GridPane.setHalignment(titulo, HPos.CENTER);

            // Autor
            Label autor = new Label(l.getAutor());
            autor.setStyle("-fx-font-size: 15;");
            GridPane.setHalignment(autor, HPos.CENTER);

            // Data para devolução
            boolean atrasado = l.getDataDevolucao() != null && l.getDataDevolucao().isBefore(LocalDate.now());
            Label dataDevolucao;
            if (!atrasado) {
                dataDevolucao = new Label(l.getDataDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                dataDevolucao.setStyle("-fx-font-size: 15;");
            } else {
                dataDevolucao = new Label("Atrasado!");
                dataDevolucao.setStyle("-fx-font-size: 15; -fx-text-fill: red;");
            }

            GridPane.setHalignment(dataDevolucao, HPos.CENTER);

            Button renovar = new Button("Renovar");
            renovar.setPrefWidth(80);
            renovar.setDisable(atrasado);
            renovar.setId(String.valueOf(l.getId()));
            renovar.setId(String.valueOf(l.getId()));

            renovar.setUserData(dataDevolucao);

            renovar.setOnAction(e -> {
                Button btn = (Button) e.getSource();
                int id = Integer.parseInt(btn.getId());

                bancoDados(id);

                Label lblData = (Label) btn.getUserData();

                String novaData = LocalDate.now().plusDays(14)
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                lblData.setText(novaData);

            });
            GridPane.setHalignment(renovar, HPos.CENTER);

            // --- Adicionar na Grid ---
            tabela.add(img, 0, linha);
            tabela.add(titulo, 1, linha);
            tabela.add(autor, 2, linha);
            tabela.add(dataDevolucao, 3, linha);
            tabela.add(renovar, 4, linha);

            // Aumentar a altura da linha
            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(160);
            rc.setPrefHeight(160);
            tabela.getRowConstraints().add(rc);

            linha++;
        }
    }

}
