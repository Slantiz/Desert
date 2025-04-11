package com.slantiz.epicgame;

import com.slantiz.epicgame.SaveManager.SaveData;
import com.slantiz.epicgame.Settings.SettingsData;
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
	private IEndConsumer endConsumer;
	private Group root;
	private Scene scene; 
	private Canvas canvas;
	private Camera mainCamera;
	private World world;
	private Player player;
	private Renderer renderer;
	private InfoDisplayer infoDisplayer;
	private InputController inputController;
	private EnemyManager enemyManager;
	private AnimationTimer gameLoop;
	private boolean hasEnded;

	public GameManager(SettingsData settings, IEndConsumer endConsumer) {
		this.settings = settings;
		this.endConsumer = endConsumer;

		root = new Group();
		scene = new Scene(root);
		hasEnded = false;

		initCanvas();
		initInput();
		initRenderer();
		initWorld();
		initEntities();
		initEnemyManager();
		hookOnPlayer();
	}

	private void initCanvas() {
		canvas = new Canvas(settings.resolution[0], settings.resolution[1]);
		root.getChildren().add(canvas);
	}

	private void initInput() {
		inputController = new InputController(scene);
	}

	private void initRenderer() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setImageSmoothing(false);
		renderer = new Renderer();
		renderer.setCanvas(canvas);
		mainCamera = new Camera(Vec.zero(), settings.unitsInWidth);
		renderer.setCamera(mainCamera);

		Image pointerImg = AssetManager.getImage("pointer.png");
		scene.setCursor(new ImageCursor(pointerImg));

		infoDisplayer = new InfoDisplayer();
	}

	private void initWorld() {
		world = new World(new Vec(settings.chunkSize[0], settings.chunkSize[1]));
		Image desertTile = AssetManager.getImage("desert-tile.png");
		world.setChunkSprite(desertTile);
	}

	private void initEntities() {
		EntityFactory.init(settings);

		player = EntityFactory.spawnPlayer(world, Vec.zero(), renderer, inputController);
		player.setKillListener(() -> endGame());
	}

	private void initEnemyManager() {
		enemyManager = new EnemyManager(world, player, settings);
	}

	private void hookOnPlayer() {
		mainCamera.setTarget(player);
		infoDisplayer.setTarget(player);
	}

	private void endGame() {
		if (hasEnded) return;
		hasEnded = true;
		SaveData saveData = new SaveData();
		saveData.highscore = player.getScore();
		endConsumer.onEnd(saveData);
		gameLoop.stop();
	}

	public void startGameLoop() {
		final long startNanoTime = System.nanoTime();
		final double removeRadius = world.leastRemoveRadius(2) + 1;

		double canvasWidth = canvas.getWidth();

		Image heartImg = AssetManager.getImage("heart.png");
		Image waterImg = AssetManager.getImage("water.png");
		Font font = AssetManager.getFont("mc-font.ttf", 64);

		gameLoop = new AnimationTimer() {
			private double lastT = 0;
			private double lastScorePassiveIncreaseT = settings.scorePassiveIncreaseTime;

			@Override
			public void handle(long currentNanoTime) {
				double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				double dt = t - lastT;

				// Update chunks
				world.generateChunks(player.getPos(), 2, null);
				world.removeChunks(player.getPos(), removeRadius);

				// Passively increase player score
				if (t > lastScorePassiveIncreaseT + settings.scorePassiveIncreaseTime) {
					player.changeScore(1);
					lastScorePassiveIncreaseT = t;
				}

				// Tick all entities
				world.update(dt);

				// Tick enemy manager
				enemyManager.update(dt);

				// Tick camera and render world
				renderer.getCamera().update(dt);
				renderer.renderWorld(world);
				
				// Render health
				renderer.renderSprite(new Vec(32, 32), new Vec(64, 64), heartImg);
				renderer.renderText(font, new Vec(96 + 16, 96), String.valueOf(player.getHealth()));

				// Render thirst
				renderer.renderSprite(new Vec(32, 128), new Vec(64, 64), waterImg);
				renderer.renderText(font, new Vec(96 + 16, 192), String.valueOf(player.getHydration()));

				// Render score
				renderer.renderText(font, new Vec(canvasWidth - 256, 96), String.valueOf(player.getScore()));

				// Render info
				infoDisplayer.infoNearestInteractable(world, renderer, font);

				// Show debug points
				if (settings.debug) {
					for (Entity entity : world.getEntities()) {
						if (entity instanceof Sword) {
							Sword sword = (Sword)entity;
							renderer.drawDebugDot(world, sword.getHitPos(), sword.getDamageRadius());
						}
						else if (entity instanceof Pawn) {
							renderer.drawDebugDot(world, entity.getPos());
						}
					}
				}

				lastT = t;
			}
		};

		gameLoop.start();
	}

	public Scene getScene() {
		return this.scene;
	}
}
