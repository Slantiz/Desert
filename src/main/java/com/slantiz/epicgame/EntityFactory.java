package com.slantiz.epicgame;

import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.entity.Cactus;
import com.slantiz.epicgame.entity.Enemy;
import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.entity.Sword;
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

	public static Player spawnPlayer(World world, Vec pos) {
		Player player = new Player(world, pos, Vec.one(), playerImg, 100, 100, 100, 10);
		player.setAcceleration(settings.playerAcceleration);
		player.setSpeed(settings.playerSpeed);
		world.addEntity(player);
		return player;
	}

	public static Sword spawnSword(World world, Vec pos) {
		Sword sword = new Sword(world, pos, Vec.one(), swordImg);
		world.addEntity(sword);
		return sword;
	}

	public static Enemy spawnEnemy(World world, Vec pos, Entity target) {
		Enemy enemy = new Enemy(world, pos, Vec.one(), enemyImg);
		enemy.setTarget(target);
		enemy.setSpeed(3 + Math.random() * 2);
		world.addEntity(enemy);
		return enemy;	
	}
}
