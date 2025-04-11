package com.slantiz.epicgame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slantiz.epicgame.SaveManager.SaveData;
import com.slantiz.epicgame.util.Vec;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Menu {
	private final Logger LOGGER = LoggerFactory.getLogger(Menu.class);

	private VBox root;
	private Scene scene;
	private Runnable playCallback;

	public Menu(Runnable playCallback) {
		this.playCallback = playCallback;
	}

	public Scene createView(SaveData saveData) {
		/* 
			Basic menu setup. Nothing fancy :P
		*/

		// Components
		Image titleImg = AssetManager.getImage("title.png", new Vec(800, 800), true, false);
		ImageView title = new ImageView(titleImg);
		title.setPreserveRatio(true);
		title.setSmooth(false);

		Text textBox = new Text("Highscore: ");
		Text scoreBox = new Text(String.valueOf(saveData.highscore));
		
		HBox hbox = new HBox(textBox, scoreBox);
		hbox.setPrefWidth(0);
		hbox.setAlignment(Pos.CENTER);

		Button startButton = new Button("Play");

		// Root setup
		root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(title, hbox, startButton);
		scene = new Scene(root);

		// Enables an outside class to respond to game start
		startButton.setOnAction((_) -> {
			playCallback.run();
		});

		// Retrieve stylesheet. This is not strictly necessary for the app to work
		try {
			root.getStylesheets().add(getClass().getResource("/styles/menu.css").toExternalForm());
		} catch (Exception e) {
			LOGGER.error("Could not load stylesheet from {}", "/styles/menu.css");
		}

		return scene;
	}

	public Scene getScene() {
		return scene;
	}
}
