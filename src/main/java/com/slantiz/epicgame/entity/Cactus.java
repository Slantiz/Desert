package com.slantiz.epicgame.entity;

import java.util.Set;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IHealth;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class Cactus extends Entity implements ICollidable {

	public Cactus(Vec pos, Vec size, Image sprite) {
		super(pos, size, sprite);
	}

	@Override
	public void update(double deltaTime) {
		
	}

	@Override
	public void onCollision(Entity other) {
		if (Set.of(other.getClass().getInterfaces()).contains(IHealth.class)) {
			((IHealth)other).damage(10);
		}
		if ((other instanceof Character)) {
			Vec dir = other.getPos().sub(pos).normalized();
			other.setPos(pos.add(dir).mul(1));
			((Character)other).setVelocity(dir.mul(15));
		}
	}
}
