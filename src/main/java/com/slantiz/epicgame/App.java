package com.slantiz.epicgame;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slantiz.epicgame.SaveManager.SaveData;
import com.slantiz.epicgame.Settings.SettingsData;

public class App extends Application {
	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	private Menu menu;
	private SaveData saveData;

    @Override
    public void start(Stage stage) throws URISyntaxException {
		// Load settings (This should crash if it fails)
		Path jarFolder = Path.of(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		SettingsData settings = Settings.load(jarFolder.resolve("settings.yaml"));

		// Load save data
		Path saveDataPath = jarFolder.resolve("data.txt");
		try {
			saveData = SaveManager.load(saveDataPath);
		} catch (Exception e) {
			LOGGER.error("Failed to load data from {}", saveDataPath);
		}

		// Init asset manager
		AssetManager.init(jarFolder, settings);

		// Window setup
        stage.setTitle(settings.title);
        stage.setWidth(settings.resolution[0]);
        stage.setHeight(settings.resolution[1]);
		stage.setFullScreen(true);

		menu = new Menu(() -> {
			// Callback for play button press
			
			// Init game manager
			GameManager gameManager = new GameManager(settings, (newSaveData) -> {
				// Callback for when game stops

				// Save score if it is a new highscore
				if (newSaveData.highscore > saveData.highscore) {
					try {
						saveData = newSaveData;
						SaveManager.save(saveDataPath, newSaveData);
					} catch (Exception e) {
						LOGGER.error("Failed to save save data to {}", saveDataPath);
					}
				}

				menu.createView(saveData);
				stage.setScene(menu.getScene());
			});

			gameManager.startGameLoop();
			stage.setScene(gameManager.getScene());
		});

		menu.createView(saveData);
		stage.setScene(menu.getScene());
		stage.show();
    }

	@Override
	public void stop() throws Exception {
		LOGGER.info("Bye!");
	}
}
