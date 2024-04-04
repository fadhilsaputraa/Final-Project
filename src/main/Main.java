package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.HomePage;

public class Main extends Application {
	
	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	
	@Override
	public void start(Stage stage) throws Exception {
		new HomePage(stage);
		stage.setTitle("ChipiChapa");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


}
