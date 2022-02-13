package app.passwordmanager.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Classe de Application do JavaFX
 */
public class Main extends Application {
	
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Carrega o arquivo de layout
		String separator = System.getProperty("file.separator");
		Pane root = FXMLLoader.load(this.getClass().getClassLoader().getResource(
				"app" + separator + 
				"passwordmanager" + separator + 
				"fxml" + separator + 
				"Layout.fxml"));
		
		// Cria a cena
		Scene scene = new Scene(root, 850, 400);
		
		// Define largura e altura mínima para a janela
		primaryStage.setMinWidth(850);
		primaryStage.setMinHeight(400);
		
		// Define o título da janela
		primaryStage.setTitle("Password Manager");
		
		// Associa a cena à janela
		primaryStage.setScene(scene);
		
		// Exibe a janela
		primaryStage.show();
	}

	/**
	 * Método main(): onde tudo começa
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Application.launch(args);
	}
}
