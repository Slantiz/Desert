package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.AssetManager;
import com.slantiz.epicgame.Settings;
import com.slantiz.epicgame.Input.InputController;
import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.controllers.EnemyController;
import com.slantiz.epicgame.controllers.PlayerController;
import com.slantiz.epicgame.rendering.Renderer;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class EntityFactory {
	private static SettingsData settings;
	private static Image playerImg = AssetManager.getImage("player.png");
	private static Image enemyImg = AssetManager.getImage("enemy.png");
	private static Image cactusImg = AssetManager.getImage("cactus.png");
	private static Image swordImg = AssetManager.getImage("sword.png");

	public static void init(SettingsData settings) {
		EntityFactory.settings = settings;
	}

	public static Cactus spawnCactus(World world, Vec pos) {
		Cactus cactus = new Cactus(world, pos, Vec.one(), cactusImg);
		world.addEntity(cactus);
		return cactus;
	}

	public static Player spawnPlayer(World world, Vec pos, Renderer renderer, InputController inputController) {
		// Init player
		Player player = new Player(world, pos, Vec.one(), playerImg, 100, 100, 100, 10);
		player.setAcceleration(settings.playerAcceleration);
		player.setSpeed(settings.playerSpeed);
		player.setInteractRange(2);

		// Init controller
		PlayerController controller = new PlayerController(renderer, inputController);
		controller.possess(player);

		// Add to world
		world.addEntity(player);
		world.addPawnController(controller);
		return player;
	}

	public static Sword spawnSword(World world, Vec pos) {
		Sword sword = new Sword(world, pos, Vec.one(), swordImg);
		sword.setDamage(20);
		sword.setDamageRadius(0.4);
		sword.setStabDist(0.5);
		sword.setRechargeTime(0.5);
		sword.setHitPivot(new Vec(-1.0/32, -0.25));
		world.addEntity(sword);
		return sword;
	}

	public static Enemy spawnEnemy(World world, Vec pos, Entity target) {
		// Init enemy
		Enemy enemy = new Enemy(world, pos, Vec.one(), enemyImg, 100, 40);
		enemy.setTarget(target);
		enemy.setAcceleration(15);
		enemy.setSpeed(3 + Math.random() * 2);

		// Init controller
		EnemyController controller = new EnemyController(world);
		controller.possess(enemy);
		enemy.setPawnController(controller);;

		// Add to world
		world.addEntity(enemy);
		world.addPawnController(controller);
		return enemy;
	}
}
