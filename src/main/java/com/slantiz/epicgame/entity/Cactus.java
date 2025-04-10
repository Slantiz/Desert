package com.slantiz.epicgame.entity;

import java.util.Set;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Cactus extends Entity implements ICollidable {

	public Cactus(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);
	}

	@Override
	public void update(double deltaTime) {
		
	}

	@Override
	public void onCollision(Entity other) {
		if (Set.of(other.getClass().getInterfaces()).contains(IDamageable.class)) {
			((IDamageable)other).damage(10);
		}
		if ((other instanceof Character)) {
			Vec dir = other.getPos().sub(pos).normalized();
			other.setPos(pos.add(dir).mul(1));
			((Character)other).setVelocity(dir.mul(15));
		}
	}
}
