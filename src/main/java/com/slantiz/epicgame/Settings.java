package com.slantiz.epicgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Class for loading settings. This class is meant to be used statically.
*/
public class Settings {
	private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);
	
	public static class SettingsData {
		public int[] resolution;
		public String title;
		public String assetsFolder;
		public String texturesPath;
		public String audioPath;
		public String fontsPath;
		public int playerSpeed;
		public double playerAcceleration;
	}

	/**
	 * Loads the default settings YAML resource as a stream.
	 * @return The settings data stream
	 */
	public static InputStream loadDefaultStream() {
		return Settings.class.getResourceAsStream("/config/defaults.yaml");
	}

	/**
	 * Loads the optional settings YAML file as a stream.
	 * @param path The absolute file path
	 * @return The settings data stream
	 */
	public static InputStream loadOptionalStream(Path path) throws FileNotFoundException {
		return new FileInputStream(path.toFile());
	}

	/**
	 * Loads the default settings YAML resource.
	 * @return The settings data
	 */
	public static SettingsData loadDefault() throws YAMLException {
		Yaml yaml = new Yaml();
		InputStream stream = loadDefaultStream();
		SettingsData settingsData = yaml.loadAs(stream, SettingsData.class);
		if (settingsData == null) return new SettingsData();
		LOGGER.info("Successfully loaded default settings");
		return settingsData;
	}

	/**
	 * Loads the optional settings YAML file.
	 * @param path The absolute file path
	 * @return The settings data
	 */
	public static SettingsData loadOptional(Path path) throws FileNotFoundException {
		Yaml yaml = new Yaml();
		InputStream stream = loadOptionalStream(path);
		SettingsData settingsData = yaml.loadAs(stream, SettingsData.class);
		LOGGER.info("Successfully loaded optional settings from {}", path.toString());
		if (settingsData == null) return new SettingsData();
		return settingsData;
	}

	/**
	 * Loads the optional settings YAML file and defaults unspecified entries.
	 * If the file cannot be loaded, this returns the default settings.
	 * @param optionalPath The absolute file path of the optional settings YAML file
	 * @return The settings data
	 */
	public static SettingsData load(Path optionalPath) throws YAMLException {
		InputStream defaultStream = loadDefaultStream();
		InputStream optionalStream = null;

		try {
			optionalStream = loadOptionalStream(optionalPath);
		} catch (FileNotFoundException e) {
			LOGGER.warn("Could not load optional settings from {}", optionalPath.toString());
		}

		Yaml yaml = new Yaml();
		InputStream combined = new SequenceInputStream(defaultStream, optionalStream);
		SettingsData settingsData = yaml.loadAs(combined, SettingsData.class);
		LOGGER.info("Successfully loaded settings");
		return settingsData;
	}
}
