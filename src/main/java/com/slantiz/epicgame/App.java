package com.slantiz.epicgame;

import javafx.application.Application;

import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
		GameController gameController = new GameController();

        stage.setTitle("Epic Game");
        stage.setWidth(1920);
        stage.setHeight(1080);
		stage.setScene(gameController.getScene());

		stage.show();
    }

	@Override
	public void stop() throws Exception {
		System.out.println("Bye!");
	}

    public static void main(String[] args) {
        launch();
    }
}
