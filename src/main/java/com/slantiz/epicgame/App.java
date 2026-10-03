package com.slantiz.epicgame;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.URI;
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
    public void start(Stage stage) {
		// Load settings (This should crash if it fails)
		Path jarFolder = resolveBaseFolder();
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
		stage.setFullScreen(false);
		stage.centerOnScreen();

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

	/**
	 * Resolves the folder that {@code settings.yaml} and {@code data.txt} are read from,
	 * which is the folder holding the jar (or the build output folder when run from a checkout).
	 * Code inside a runtime image has no file location, so the working directory is used there.
	 * @return The base folder
	 */
	private static Path resolveBaseFolder() {
		try {
			URI location = App.class.getProtectionDomain().getCodeSource().getLocation().toURI();
			if ("file".equals(location.getScheme())) return Path.of(location).getParent();
			LOGGER.info("Code source {} is not a file (using the working directory)", location);
		} catch (Exception e) {
			LOGGER.warn("Could not resolve the code source location (using the working directory)", e);
		}
		return Path.of(System.getProperty("user.dir")).toAbsolutePath();
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("Bye!");
	}
}
