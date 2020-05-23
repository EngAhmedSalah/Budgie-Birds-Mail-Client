package MailClient;

import MailClient.view.ViewFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ViewFactory viewFactory = ViewFactory.defaultFactory;
		Scene scene = viewFactory.getMainScene();
		primaryStage.setTitle("Budgie Birds Mail Client");
		primaryStage.getIcons().add(new Image("MailClient/view/images/bird.png"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}