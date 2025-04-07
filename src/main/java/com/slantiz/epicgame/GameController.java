package com.slantiz.epicgame;

import com.slantiz.epicgame.Input.InputController;
import com.slantiz.epicgame.entity.Enemy;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.entity.Sword;
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

public class GameController {

	private Group root;
	private Scene scene; 
	private Canvas canvas;
	private Camera mainCamera;
	private World world;
	private Player player;
	private Sword sword;
	private Renderer renderer;
	private InputController inputController;
	private PlayerController playerController;
	private int score;

	public GameController() {
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
		canvas = new Canvas(1920, 1080);
		root.getChildren().add(canvas);
	}

	private void initWorld() {
		world = new World(new Vec(16, 16));
	}

	private void initEntities() {
		// Init sprite data
		Image desertTile = new Image("desert-tile.png", 256, 256, true, false);

		world.setChunkSprite(desertTile);

		// Create and init entities
		player = EntityFactory.makePlayer(Vec.zero());
		player.setSpeed(4);
		sword = EntityFactory.makeSword(Vec.zero());
		sword.setTarget(player);
		sword.setPivot(new Vec(0.5, 1.5));

		// Add entities
		world.addEntity(player);
		world.addEntity(sword);
	}

	private void initRenderer() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		renderer = new Renderer();
		renderer.setCanvas(canvas);
		mainCamera = new Camera(Vec.zero(), 24);
		mainCamera.setTarget(player);
		renderer.setCamera(mainCamera);

		Image pointerImg = new Image("pointer.png", 16, 16, true, false);
		scene.setCursor(new ImageCursor(pointerImg));
	}

	private void initInput() {
		inputController = new InputController(scene);
		playerController = new PlayerController(renderer, inputController);
		playerController.setPlayer(player);
		playerController.setSword(sword);
	}

	private void startGameLoop() {
		final long startNanoTime = System.nanoTime();
		final double removeRadius = world.leastRemoveRadius(2) + 1;

		double canvasWidth = canvas.getWidth();
		score = 0;

		new AnimationTimer() {
			private double lastT = 0;
			private double nextSpawnT = 5;

			@Override
			public void handle(long currentNanoTime) {
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				double dt = t - lastT;

				// Update chunks
				world.generateChunks(player.getPos(), 2, null);
				world.removeChunks(player.getPos(), removeRadius);

				// Tick all entities
				world.update(dt);

				// Spawn new entities around player
				if (t >= nextSpawnT) {
					Enemy enemy = EntityFactory.makeEnemy(Vec.zero(), player);
					world.addEntity(enemy);
					nextSpawnT += 5;
				}


				// Render
				renderer.getCamera().update(dt);
				renderer.renderWorld(world);
				
				// Render health
				renderer.renderSprite(new Vec(32, 32), new Vec(64, 64), new Image("heart.png"));
				renderer.renderText(new Vec(96 + 16, 96), 64, String.valueOf(player.getHealth()));

				// Render thirst
				renderer.renderSprite(new Vec(32, 128), new Vec(64, 64), new Image("water.png"));
				renderer.renderText(new Vec(96 + 16, 192), 64, String.valueOf(player.getThirst()));

				// Render score
				renderer.renderText(new Vec(canvasWidth - 256, 96), 64, String.valueOf(score));

				// Render score

				lastT = t;
			}
		}.start();
	}

	public Scene getScene() {
		return this.scene;
	}
}
