package br.feevale;

import java.io.IOException;

import javafx.fxml.FXML;

public class Navegar {
  @FXML
  private void abrirPerfil() throws IOException {
    App.setRoot("perfil");
  }
}
