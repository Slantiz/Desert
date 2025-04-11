package com.slantiz.epicgame.controllers;

import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

public class EnemyController extends PawnController {

	private World world;
	private Entity huntTarget;

	public EnemyController(World world) {
		this.world = world;

		// Find player
		for (Entity entity : world.getEntities()) {
			if (!(entity instanceof Player)) continue;
			huntTarget = entity;
			break;
		}
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onDeath'");
	}

	@Override
	public void update(double dt) {
		if (target == null) return;
		if (huntTarget == null) return;
		Vec dir = huntTarget.getPos().sub(target.getPos()).normalized();
		target.setDir(dir);
	}
}
