package com.slantiz.epicgame;

import com.slantiz.epicgame.Settings.SettingsData;
import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.EntityFactory;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

public class EnemyManager {

	private World world;
	private Entity target;
	private SettingsData settings;
	private double[] spawnTimeRange;
	private int[] spawnCountRange;
	
	private double timeTillNextSpawn;
	private double timeTillNextDifficulty;

	public EnemyManager(World world, Entity target, SettingsData settings) {
		this.world = world;
		this.target = target;
		this.settings = settings;

		this.spawnTimeRange = settings.spawnTimeRange;
		this.spawnCountRange = settings.spawnCountRange;
		timeTillNextSpawn = randomWaitTime();
		timeTillNextDifficulty = settings.difficultyIncreaseTime;
	}

	private int randomCount() {
		return spawnCountRange[0] + (int)(Math.random() * ((spawnCountRange[1] - spawnCountRange[0]) + 1));
	}

	private double randomWaitTime() {
		return spawnTimeRange[0] + Math.random() * (spawnTimeRange[1] - spawnTimeRange[0]);
	}

	private void spawnGroup(Vec pos) {
		int count = randomCount();
		for (int i = 0; i < count; i++) {
			EntityFactory.spawnEnemy(world, pos, target);
		}
	}

	public void update(double dt) {
		if (timeTillNextDifficulty <= 0) {
			spawnCountRange[0] += 1;
			spawnCountRange[1] += 2;
			timeTillNextDifficulty = settings.difficultyIncreaseTime;
		}
		else {
			timeTillNextDifficulty -= dt;
		}

		if (timeTillNextSpawn <= 0) {
			Vec spawnOffset = Vec.fromRot(Math.random() * 360).mul(settings.enemySpawnDistance);
			Vec spawnPos = target.getPos().add(spawnOffset);
			spawnGroup(spawnPos);
	
			timeTillNextSpawn = randomWaitTime();
		}
		else {
			timeTillNextSpawn -= dt;
			return;
		}
	}
}
