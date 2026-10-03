package com.slantiz.epicgame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveManager {
	private static Logger LOGGER = LoggerFactory.getLogger(SaveManager.class);

	public static class SaveData implements Serializable {
		public int highscore;
	}

	public static SaveData load(Path path) throws IOException, ClassNotFoundException {
		File file = path.toFile();
		// Create new data file if it does not already exist
		if (file.createNewFile() || file.length() == 0) {
			LOGGER.info("No save data at {} yet (starting with an empty highscore)", path);
			return new SaveData();
		}

		SaveData saveData = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream fileObjectStream = new ObjectInputStream(fileInputStream);
			saveData = (SaveData)fileObjectStream.readObject();
			fileObjectStream.close();
		} catch (Exception e) {
			LOGGER.warn("Save data is malformed (returning empty save data)");
			return new SaveData();
		}

		LOGGER.info("Successfully loaded data from {}", path);
		return saveData;
	}

	public static void save(Path path, SaveData saveData) throws IOException {
		File file = path.toFile();
		// Create new data file if it does not already exist
		if (file.createNewFile()) LOGGER.warn("Cannot find save data at {} (Creating new file)", path);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(saveData);
		objectOutputStream.close();
		LOGGER.info("Successfully saved data to {}", path);
	}
}
