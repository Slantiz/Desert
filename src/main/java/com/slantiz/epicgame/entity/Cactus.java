package com.slantiz.epicgame.entity;

import java.util.Set;

import com.slantiz.epicgame.entity.components.DamageData;
import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.IHasScore;
import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.entity.components.IThirstable;
import com.slantiz.epicgame.entity.components.InteractData;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Cactus extends Entity implements ICollidable, IInteractable {

	private int damageAmount;
	private int hydrationAmount;
	private int hydrateScoreAmount;

	public Cactus(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);
	}

	public int getDamageAmount() {
		return damageAmount; 
	}

	public void setDamangeAmount(int amount) {
		this.damageAmount = amount;
	}

	public int getHydrationAmount() {
		return hydrationAmount;
	}

	public void setHydrationAmount(int amount) {
		this.hydrationAmount = amount;
	}

	public int getHydrateScoreAmount() {
		return hydrateScoreAmount;
	}

	public void setHydrateScoreAmount(int amount) {
		this.hydrateScoreAmount = amount;
	}

	@Override
	public void update(double deltaTime) {
		
	}

	@Override
	public void onCollision(Entity other) {
		if (Set.of(other.getClass().getInterfaces()).contains(IDamageable.class)) {
			((IDamageable)other).damage(new DamageData(this, damageAmount));
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
			((IThirstable)entity).hydrate(hydrationAmount);
			if (entity instanceof IHasScore) {
				((IHasScore)entity).changeScore(hydrateScoreAmount);
			}
		}

		world.queueEntityDestroy(this);
	}
}
