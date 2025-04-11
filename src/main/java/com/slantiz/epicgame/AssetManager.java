package com.slantiz.epicgame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

/**
 * Class for managing assets.
 */
public class AssetManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetManager.class);

	private static Path assetFolder;
	private static String texturesPath;
	private static String audioPath;
	private static String fontsPath;
	private static String stylesPath;

	public static void init(Path jarFolder, SettingsData settings) {
		assetFolder = jarFolder.resolve(settings.assetsFolder);
		texturesPath = settings.texturesPath;
		audioPath = settings.audioPath;
		fontsPath = settings.fontsPath;
		stylesPath = settings.stylesPath;
	}

	public static InputStream getResourceStream(String path) {
		if (path == null) return null;
		return AssetManager.class.getResourceAsStream("/" + path);
	}

	public static InputStream getFileStream(Path path) throws FileNotFoundException {
		return new FileInputStream(path.toFile());
	}

	/**
	 * Returns an asset as a stream. It tries to get the file stream first.
	 * If it fails, it will get the resource stream.
	 * @param path Path relative from asset folder
	 * @return An {@code InputStream} of the asset
	 */
	public static InputStream getAsset(String path) {
		try {
			return getFileStream(assetFolder.resolve(Path.of(path)));
		} catch (Exception e) {
			LOGGER.debug("Could not find {} (trying default)", assetFolder.resolve(Path.of(path)));
		}

		InputStream resourceStream = getResourceStream(path);
		if (resourceStream == null) LOGGER.error("Could not find {} (default)", "/" + path);
		return resourceStream;
	}

	public static Image getImage(String name) {
		InputStream stream = getAsset(String.format("%s/%s", texturesPath, name));
		Image image = new Image(stream);
		return image;
	}

	public static Image getImage(String name, Vec size, boolean preserveRatio, boolean smooth) {
		InputStream stream = getAsset(String.format("%s/%s", texturesPath, name));
		Image image = new Image(stream, size.x, size.y, preserveRatio, smooth);
		return image;
	}

	public static Font getFont(String name, int size) {
		InputStream stream = getAsset(String.format("%s/%s", fontsPath, name));
		Font font = Font.loadFont(stream, size);
		return font;
	}

	// public static void main(String[] args) throws URISyntaxException {
	// 	Path jarFolder = Path.of(AssetManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
	// 	SettingsData settings = Settings.load(jarFolder.resolve("settings.yaml"));
	// 	AssetManager.init(jarFolder, settings);
	// 	Image image = getImage("desert-tile.png");
	// 	System.out.println(image);
	// }
}
