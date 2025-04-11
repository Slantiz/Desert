package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Enemy extends Pawn implements ICollidable, IDamageable {

	private Entity target;
	private int maxHealth;
	private int health;

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
			((Player)other).damage(10);
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
		if (health <= 0) kill();
	}

	public void damage(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHealth(this.health - amount);
	}

	public void heal(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHealth(this.health + amount);
	}

	public void kill() {
		System.out.println(String.format("%s JUST DIED!", this.getClass().getName()));

		// Remove enemy controller
		controller.unpossess();
		world.removePawnController(controller);

		// Remove this enemy
		world.removeEntity(this);
	}
}
