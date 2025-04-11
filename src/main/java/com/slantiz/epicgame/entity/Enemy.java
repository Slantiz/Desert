package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.DamageData;
import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.IHasScore;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Enemy extends Pawn implements ICollidable, IDamageable {

	protected Entity target;
	protected int maxHealth;
	protected int health;
	protected int damageAmount;
	protected int killScoreAmount;

	public Enemy(World world, Vec pos, Vec size, Image sprite, int maxHealth, int health) {
		super(world, pos, size, sprite);

		target = null;
		setMaxHealth(maxHealth);
		setHealth(health);
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public void onCollision(Entity other) {
		if (other instanceof Player) {
			((Player)other).damage(new DamageData(this, damageAmount));
		}
		if (other instanceof Pawn) {			
			((Pawn)other).knockBack(getPos(), 1, 15);
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		if (this.maxHealth < 0) {
			throw new IllegalArgumentException("maxHealth cannot be negative.");
		}
		this.maxHealth = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		// Clamp the health between 0 and maxHealth
		this.health = Math.max(Math.min(health, maxHealth), 0);
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(int amount) {
		this.damageAmount = amount;
	}

	public int getKillScoreAmount() {
		return killScoreAmount;
	}

	public void setKillScoreAmount(int amount) {
		this.killScoreAmount = amount;
	}

	public void damage(DamageData damageData) {
		if (damageData.amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}

		setHealth(health - damageData.amount);
		if (health <= 0) kill(damageData);
	}

	public void heal(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		setHealth(health + amount);
	}

	public void kill(DamageData damageData) {
		// Give score
		if (damageData.attacker != null && damageData.attacker instanceof IHasScore) {
			((IHasScore)damageData.attacker).changeScore(killScoreAmount);
		}

		// Remove enemy controller
		controller.unpossess();
		world.removePawnController(controller);

		// Remove this enemy
		world.queueEntityDestroy(this);
	}
}
