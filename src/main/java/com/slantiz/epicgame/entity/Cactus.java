package com.slantiz.epicgame.entity;

import java.util.Set;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.entity.components.IThirstable;
import com.slantiz.epicgame.entity.components.InteractData;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Cactus extends Entity implements ICollidable, IInteractable {

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
		if ((other instanceof Pawn)) {
			Vec dir = other.getPos().sub(pos).normalized();
			other.setPos(pos.add(dir).mul(1));
			((Pawn)other).setVelocity(dir.mul(15));
		}
	}

	@Override
	public InteractData getInteractData() {
		return new InteractData("Drink", this.getPos());
	}

	@Override
	public void interact(Pawn entity) {
		if (entity instanceof IThirstable) {
			((IThirstable)entity).hydrate(20);
		}

		world.removeEntity(this);
	}
}
