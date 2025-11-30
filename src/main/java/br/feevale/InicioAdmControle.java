package br.feevale;

import java.io.IOException;

import javafx.fxml.FXML;

public class InicioAdmControle {
   @FXML
    private void cadUs() throws IOException {
        AppAdm.setRoot("cadastraUsuario");
    }

   @FXML
    private void cadLi() throws IOException {
        AppAdm.setRoot("cadastraLivro");
    }

    @FXML
    private void encLi() throws IOException {
        AppAdm.setRoot("EncontrarLivro");
    }



}
