package com.slantiz.epicgame;

import com.slantiz.epicgame.Input.InputController;
import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.rendering.Camera;
import com.slantiz.epicgame.rendering.Renderer;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class GameController {

	private SettingsData settings;
	private Group root;
	private Scene scene; 
	private Canvas canvas;
	private Camera mainCamera;
	private World world;
	private Player player;
	private Renderer renderer;
	private InputController inputController;
	private PlayerController playerController;
	private int score;

	public GameController(SettingsData settings) {
		this.settings = settings;
		root = new Group();
		scene = new Scene(root);

		initCanvas();
		initWorld();
		initEntities();
		initRenderer();
		initInput();
		startGameLoop();
	}

	private void initCanvas() {
		canvas = new Canvas(settings.resolution[0], settings.resolution[1]);
		root.getChildren().add(canvas);
	}

	private void initWorld() {
		world = new World(new Vec(16, 16));
		Image desertTile = AssetManager.getImage("desert-tile.png");
		world.setChunkSprite(desertTile);
	}

	private void initEntities() {
		EntityFactory.init(settings);

		// Create, init and add entities
		player = EntityFactory.spawnPlayer(world, Vec.zero());
	}

	private void initRenderer() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		renderer = new Renderer();
		renderer.setCanvas(canvas);
		mainCamera = new Camera(Vec.zero(), 24);
		mainCamera.setTarget(player);
		renderer.setCamera(mainCamera);

		Image pointerImg = AssetManager.getImage("pointer.png");
		scene.setCursor(new ImageCursor(pointerImg));
	}

	private void initInput() {
		inputController = new InputController(scene);
		playerController = new PlayerController(renderer, inputController);
		playerController.setPlayer(player);
	}

	private void startGameLoop() {
		final long startNanoTime = System.nanoTime();
		final double removeRadius = world.leastRemoveRadius(2) + 1;

		double canvasWidth = canvas.getWidth();
		score = 0;

		Image heartImg = AssetManager.getImage("heart.png");
		Image waterImg = AssetManager.getImage("water.png");
		Font font = AssetManager.getFont("mc-font.ttf", 64);

		new AnimationTimer() {
			private double lastT = 0;

			@Override
			public void handle(long currentNanoTime) {
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				double dt = t - lastT;

				// Update chunks
				world.generateChunks(player.getPos(), 2, null);
				world.removeChunks(player.getPos(), removeRadius);

				// Tick all entities
				world.update(dt);

				// Render
				renderer.getCamera().update(dt);
				renderer.renderWorld(world);
				
				// Render health
				renderer.renderSprite(new Vec(32, 32), new Vec(64, 64), heartImg);
				renderer.renderText(font, new Vec(96 + 16, 96), String.valueOf(player.getHealth()));

				// Render thirst
				renderer.renderSprite(new Vec(32, 128), new Vec(64, 64), waterImg);
				renderer.renderText(font, new Vec(96 + 16, 192), String.valueOf(player.getHydration()));

				// Render score
				renderer.renderText(font, new Vec(canvasWidth - 256, 96), String.valueOf(score));

				lastT = t;
			}
		}.start();
	}

	public Scene getScene() {
		return this.scene;
	}
}
