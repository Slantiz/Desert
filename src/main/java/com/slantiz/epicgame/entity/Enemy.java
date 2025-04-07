package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class Enemy extends Character implements ICollidable {

	private Entity target;

	public Enemy(Vec pos, Vec size, Image sprite) {
		super(pos, size, sprite);
		target = null;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public void update(double dt) {
		dir = target.getPos().sub(pos);
		if (dir.len() > 0) dir = dir.normalized();
		super.update(dt);
	}

	@Override
	public void onCollision(Entity other) {
		if (other instanceof Player) {
			((Player)other).damage(10);
		}
		if (other instanceof Character) {			
			Vec colDir = other.getPos().sub(pos);
			if (colDir.len() > 0) colDir = colDir.normalized();
			other.setPos(pos.add(colDir).mul(1));
			((Character)other).setVelocity(colDir.mul(15));
		}
	}

}
