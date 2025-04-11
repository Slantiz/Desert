package com.slantiz.epicgame;

import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.entity.Enemy;
import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.EntityFactory;
import com.slantiz.epicgame.entity.Pawn;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.entity.Sword;
import com.slantiz.epicgame.input.InputController;
import com.slantiz.epicgame.rendering.Camera;
import com.slantiz.epicgame.rendering.InfoDisplayer;
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

public class GameManager {

	private SettingsData settings;
	private Group root;
	private Scene scene; 
	private Canvas canvas;
	private Camera mainCamera;
	private World world;
	private Player player;
	private Renderer renderer;
	private InfoDisplayer infoDisplayer;
	private InputController inputController;
	private int score;

	public GameManager(SettingsData settings) {
		this.settings = settings;
		root = new Group();
		scene = new Scene(root);

		initCanvas();
		initInput();
		initRenderer();
		initWorld();
		initEntities();
		hookOnPlayer();
		startGameLoop();
	}

	private void initCanvas() {
		canvas = new Canvas(settings.resolution[0], settings.resolution[1]);
		root.getChildren().add(canvas);
	}

	private void initInput() {
		inputController = new InputController(scene);
	}

	private void initWorld() {
		world = new World(new Vec(16, 16));
		Image desertTile = AssetManager.getImage("desert-tile.png");
		world.setChunkSprite(desertTile);
	}

	private void initEntities() {
		EntityFactory.init(settings);

		// Add player
		player = EntityFactory.spawnPlayer(world, Vec.zero(), renderer, inputController);
	}

	private void initRenderer() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		renderer = new Renderer();
		renderer.setCanvas(canvas);
		mainCamera = new Camera(Vec.zero(), 24);
		renderer.setCamera(mainCamera);

		Image pointerImg = AssetManager.getImage("pointer.png");
		scene.setCursor(new ImageCursor(pointerImg));

		infoDisplayer = new InfoDisplayer(2);
	}

	private void hookOnPlayer() {
		mainCamera.setTarget(player);
		infoDisplayer.setTarget(player);
	}

	private void startGameLoop() {
		final long startNanoTime = System.nanoTime();
		final double removeRadius = world.leastRemoveRadius(2) + 1;

		double canvasWidth = canvas.getWidth();
		score = 0;

		Image heartImg = AssetManager.getImage("heart.png");
		Image waterImg = AssetManager.getImage("water.png");
		Font font = AssetManager.getFont("mc-font.ttf", 64);

		// for enemies
		double spawnDelay = 3;

		new AnimationTimer() {
			private double lastT = 0;

			// for enemies
			private double lastSpawnT = 0;

			@Override
			public void handle(long currentNanoTime) {
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				double dt = t - lastT;

				// Update chunks
				world.generateChunks(player.getPos(), 2, null);
				world.removeChunks(player.getPos(), removeRadius);

				// Create enemies
				if (t > lastSpawnT + spawnDelay) {
					Vec spawnOffset = new Vec(0, 1).rotated(Math.random() * 360).mul(5);
					Vec spawnPos = player.getPos().add(spawnOffset);
					EntityFactory.spawnEnemy(world, spawnPos, player);
					lastSpawnT = t;
				}

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

				// Render info
				infoDisplayer.infoNearestInteractable(world, renderer, font);

				// Show debug points
				// for (Entity entity : world.getEntities()) {
				// 	if (entity instanceof Sword) {
				// 		Sword sword = (Sword)entity;
				// 		renderer.drawDebugDot(world, sword.getHitPos(), sword.getDamageRadius());
				// 	}
				// 	else if (entity instanceof Pawn) {
				// 		renderer.drawDebugDot(world, entity.getPos());
				// 	}
				// }

				lastT = t;
			}
		}.start();
	}

	public Scene getScene() {
		return this.scene;
	}
}
