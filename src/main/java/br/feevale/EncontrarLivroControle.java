package br.feevale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class EncontrarLivroControle implements LerCSV {

    @FXML
    private GridPane nRetirados; 

    @FXML
    private GridPane retirados; 

    @FXML
    private void initialize() {
        bancoDados();
    }

    @FXML
    private void irInicio() throws IOException {
        AppAdm.setRoot("inicioAdm");
    }

    @Override
    public void bancoDados() {

        List<Usuario> usuarios = carregarUsuarios();
        List<String[]> livros = carregarLivros();

        int rowLivres = 1;
        int rowReservados = 1;

        for (String[] dados : livros) {

            String nome = dados[2];
            String local = dados[4];
            String img = dados[5];
            int idUsuario = Integer.parseInt(dados[6]);
            String nomeReservou = "desconhecido";
            ImageView imageView;

            if (img != null && !img.isBlank()) {
                    imageView = new ImageView(new Image(getClass().getResourceAsStream("/br/feevale/imgs/" + img)));
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(200);
            }
            else {
                imageView = new ImageView();
                imageView.setFitWidth(100);
                    imageView.setFitHeight(200);
            }

            Label nomeLabel = new Label(nome);
            Label localLabel = new Label(local);

            if (idUsuario == 0) {

                nRetirados.add(imageView, 0, rowLivres);
                nRetirados.add(nomeLabel, 1, rowLivres);
                nRetirados.add(localLabel, 2, rowLivres);
                rowLivres++;

            } else {
                for (Usuario u : usuarios) {
                   if(u.getIdPessoa() == idUsuario){
                    nomeReservou = u.getNome();
                   } 
                }

                Label quem = new Label("Reservado por: " + nomeReservou);

                retirados.add(imageView, 0, rowReservados);
                retirados.add(nomeLabel, 1, rowReservados);
                retirados.add(quem, 2, rowReservados);
                rowReservados++;
            }
        }
    }

    private List<Usuario> carregarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        try {
            InputStream input = getClass().getResourceAsStream("/BPessoa.csv");

            if (input == null) {
                throw new IOException("BPessoas.csv não encontrado no resources/");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            String linha;
            while ((linha = br.readLine()) != null) {

                String[] dados = linha.split(",");

                // CSV: id,codigo,nome
                int idPessoa = Integer.parseInt(dados[0]);
                String codigo = dados[1];
                String nome = dados[2];

                lista.add(new Usuario(codigo, idPessoa, nome));
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler BPessoas.csv");
            e.printStackTrace();
        }

        return lista;
    }
    
    private List<String[]> carregarLivros() {
        List<String[]> lista = new ArrayList<>();

        try {
            InputStream input = getClass().getResourceAsStream("/BLivros.csv");

            if (input == null) {
                throw new IOException("BLivros.csv não encontrado no resources/");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(input));

            String linha;
            while ((linha = br.readLine()) != null) {
                lista.add(linha.split(","));
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler BLivros.csv");
            e.printStackTrace();
        }

        return lista;
    }

}
