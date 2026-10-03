package com.slantiz.epicgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;

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
		public String fontsPath;
		public String stylesPath;

		public boolean debug;

		public int[] chunkSize;

		public double unitsInWidth;

		public int cactusDamangeAmount;
		public int cactusHydrationAmount;

		public int playerSpeed;
		public double playerAcceleration;
		public double playerInteractionRange;
		public int playerMaxHealth;
		public int playerInitHealth;
		public int playerMaxHydration;
		public int playerInitHydration;
		public double playerHydrationDecreaseTime;
		public int playerHydrationHealThreshold;

		public int swordDamage;
		public double swordDamageRadius;
		public double swordStabDist;
		public double swordRechargeTime;

		public int enemyDamageAmount;

		public int enemySpawnDistance;
		public double[] spawnTimeRange;
		public int[] spawnCountRange;
		public int difficultyIncreaseTime;
		public int[] difficultyCountIncrease;
		
		public int scoreKillIncrease;
		public int scoreDrinkIncrease;
		public int scorePassiveIncreaseTime;
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
		Yaml yaml = new Yaml();
		Map<String, Object> values = yaml.load(loadDefaultStream());

		// Merge the optional entries over the defaults, entry by entry
		try (InputStream optionalStream = loadOptionalStream(optionalPath)) {
			Map<String, Object> optionalValues = yaml.load(optionalStream);
			if (optionalValues != null) values.putAll(optionalValues);
			LOGGER.info("Successfully loaded optional settings from {}", optionalPath.toString());
		} catch (Exception e) {
			LOGGER.info("No optional settings at {} (using the defaults)", optionalPath.toString());
		}

		SettingsData settingsData = yaml.loadAs(yaml.dump(values), SettingsData.class);
		LOGGER.info("Successfully loaded settings");
		return settingsData;
	}
}
