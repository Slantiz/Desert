package com.slantiz.epicgame;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import com.slantiz.epicgame.Settings.SettingsData;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
		// Load settings
		Path jarFolder = Path.of(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		SettingsData settings = Settings.load(jarFolder.resolve("settings.yaml"));

		// Init asset manager
		AssetManager.init(jarFolder, settings);

		// Init Game
		GameManager gameController = new GameManager(settings);

        stage.setTitle(settings.title);
        stage.setWidth(settings.resolution[0]);
        stage.setHeight(settings.resolution[1]);
		stage.setScene(gameController.getScene());

		stage.show();
    }

	@Override
	public void stop() throws Exception {
		System.out.println("Bye!");
	}
}
