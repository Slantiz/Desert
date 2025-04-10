package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.IThirstable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Player extends Character implements IDamageable, IThirstable, ICollidable {

	protected int maxHealth;
	protected int health;
	protected int maxHydration;
	protected int hydration;

	/**
	 * Creates a new player entity.
	 * @param world The entity's world
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 * @return A player entity
	 */
	public Player(World world, Vec pos, Vec size, Image sprite, int maxHealth, int health, int maxThirst, int thirst) {
		super(world, pos, size, sprite);

		setMaxHealth(maxHealth);;
		setHealth(health);
		setMaxHydration(maxThirst);
		setHydration(thirst);
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

	public int getMaxHydration() {
		return maxHydration;
	}

	public void setMaxHydration(int maxHydration) {
		this.maxHydration = maxHydration;
	}

	public int getHydration() {
		return hydration;
	}

	public void setHydration(int hydration) {
		// Clamp the hydration between 0 and maxThirst
		this.hydration = Math.max(Math.min(hydration, maxHydration), 0);
	}

	public void thirst(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.hydration = Math.max(Math.min(hydration, maxHydration), 0);
	}

	public void hydrate(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.hydration = Math.max(Math.min(hydration, maxHydration), 0);
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
	}

	public void onCollision(Entity other) {
		
	}

	@Override
	public void update(double dt) {
		super.update(dt);
	}
}
