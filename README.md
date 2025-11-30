## Biblioteca Feevale (JavaFX)

Aplicacao desktop desenvolvida em JavaFX para gerenciamento simples de emprestimos de livros. Os dados de usuarios e acervo sao carregados a partir de arquivos CSV e exibidos em telas desenhadas em FXML. Ha fluxos separados para usuarios finais e administradores.

### Funcionalidades principais
- Autenticacao de usuarios por codigo e senha (arquivo `BPessoa.csv`).
- Visualizacao de dados do perfil e emprestimos ativos.
- Busca de livros por codigo, com retirada e devolucao controladas via `BLivros.csv`.
- Cadastro de novos usuarios e livros (fluxo administrativo).
- Sessao em memoria para limitar operacoes ao usuario logado.

### Stack e requisitos
- Java 11 ou superior.
- Maven 3.6+.
- JavaFX 13 (adicionado como dependencia no `pom.xml`).

### Estrutura de pastas
```
src/
	main/
		java/
			br/feevale/
				App.java               # Ponto de entrada para o fluxo do usuario
				AppAdm.java             # Ponto de entrada para o painel administrativo
				*.java                  # Controles JavaFX e classes de dominio/sessao
		resources/
			BLivros.csv              # Base de livros
			BPessoa.csv              # Base de usuarios
			br/feevale/*.fxml        # Telas JavaFX
			br/feevale/*.css         # Estilos das telas
pom.xml                        # Configuracao Maven/JavaFX
```

### Como executar
1. Certifique-se de ter o JDK 11+ e Maven instalados e configurados no `PATH`.
2. Dentro da raiz do projeto, instale as dependencias e execute a aplicacao de usuario:
	 ````
	 mvn clean javafx:run
	 ````
	 O plugin `javafx-maven-plugin` ja aponta para `br.feevale.App`, que abre a tela de login do usuario.
3. Para abrir diretamente o painel administrativo (classe `br.feevale.AppAdm`), execute:
	 ````
	 mvn clean javafx:run -Djavafx.mainClass=br.feevale.AppAdm
	 ````

### Executaveis e builds
- Gerar artefato: `mvn clean package` cria o jar em `target/demo-1.jar`. Para executar fora do Maven, lembre-se de incluir o modulo JavaFX no `module-path` ou utilize o plugin para criar um runtime image.

### Dados e persistencia
- `BLivros.csv`: guarda livros com campos `id,codigo,titulo,autor,local,imagem,idPessoa,dataDevolucao`.
- `BPessoa.csv`: guarda usuarios com campos `id,codigo,nome,senha`.
- Alteracoes de retirada/devolucao e cadastros atualizam os CSVs em `src/main/resources`. Os arquivos em `target/classes` sao copias geradas na build; edite sempre os originais em `src/main/resources`.

### Tests e melhorias sugeridas
- Adicionar validacoes nos cadastros (campos obrigatorios, formatos).
- Criar testes unitarios para os controles que manipulam arquivos CSV.
- Evoluir o armazenamento para um banco de dados relacional ou embedado para evitar conflitos de escrita em disco.
