package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IHealth;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class Player extends Character implements IHealth, ICollidable {

	protected int maxHealth;
	protected int health;
	protected int maxThirst;
	protected int thirst;
	protected double thirstTimeCounter;

	/**
	 * Creates a new player entity.
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 * @return A player entity
	 */
	public Player(Vec pos, Vec size, Image sprite, int maxHealth, int health, int maxThirst, int thirst) {
		super(pos, size, sprite);
		thirstTimeCounter = 0;

		this.setMaxHealth(maxHealth);;
		this.setHealth(health);
		this.setMaxThirst(maxThirst);
		this.setThirst(thirst);
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
		this.health = Math.max(Math.min(health, this.maxHealth), 0);
		if (health <= 0) this.kill();
	}

	public int getMaxThirst() {
		return maxThirst;
	}

	public void setMaxThirst(int maxThirst) {
		this.maxThirst = maxThirst;
	}

	public int getThirst() {
		return thirst;
	}

	public void setThirst(int thirst) {
		// Clamp the thirst between 0 and maxThirst
		this.thirst = Math.max(Math.min(thirst, maxThirst), 0);
	}

	@Override
	public void update(double dt) {
		if (thirstTimeCounter > 1) {
			if (thirst > 0) thirst -= 1;
			else damage(1);
			System.out.println("yo");
			thirstTimeCounter = 0;
		}
		thirstTimeCounter += dt;

		super.update(dt);
	}

	@Override
	public void kill() {
		System.out.println("THIS DIED!");
	}

	@Override
	public void damage(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHealth(this.health - amount);
	}

	@Override
	public void heal(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHealth(this.health + amount);
	}

	@Override
	public void onCollision(Entity other) {
		
	}
}
