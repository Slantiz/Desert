package com.slantiz.epicgame;

import com.slantiz.epicgame.entity.Cactus;
import com.slantiz.epicgame.entity.Enemy;
import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.entity.Sword;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class EntityFactory {
	public static final Image playerImg = new Image("player.png", 16, 16, true, false);
	public static final Image enemyImg = new Image("enemy.png", 16, 16, true, false);
	public static final Image cactusImg = new Image("cactus.png", 16, 16, true, false);
	public static final Image swordImg = new Image("sword.png", 16, 16, true, false);

	public static Cactus makeCactus(Vec pos) {
		return new Cactus(pos, Vec.one(), cactusImg);
	}

	public static Player makePlayer(Vec pos) {
		return new Player(pos, Vec.one(), playerImg, 100, 100, 100, 10);
	}

	public static Sword makeSword(Vec pos) {
		return new Sword(pos, Vec.one(), swordImg);
	}

	public static Enemy makeEnemy(Vec pos, Entity target) {
		Enemy enemy = new Enemy(pos, Vec.one(), enemyImg);
		enemy.setTarget(target);
		enemy.setSpeed(3 + Math.random() * 2);
		return enemy;	
	}
}
