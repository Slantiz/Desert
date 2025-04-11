package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.DamageData;
import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.IHasScore;
import com.slantiz.epicgame.entity.components.IThirstable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Player extends Pawn implements IDamageable, IThirstable, ICollidable, IHasScore {

	protected int score;
	protected int maxHealth;
	protected int health;
	protected int maxHydration;
	protected int hydration;
	protected double hydrationDecreaseTime;
	protected double timeTillHydrationDecrease;
	protected int playerHydrationHealThreshold;
	protected Runnable killListener;

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

		score = 0;
		timeTillHydrationDecrease = 0;

		setMaxHealth(maxHealth);;
		setHealth(health);
		setMaxHydration(maxThirst);
		setHydration(thirst);
	}

	public int getScore() {
		return score;
	}

	public void changeScore(int amount) {
		this.score += amount;
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

	public double getHydrationDecreaseTime() {
		return hydrationDecreaseTime;
	}

	public void setHydrationDecreaseTime(double hydrationDecreaseTime) {
		this.hydrationDecreaseTime = hydrationDecreaseTime;
	}

	public int getPlayerHydrationHealThreshold() {
		return playerHydrationHealThreshold;
	}

	public void setPlayerHydrationHealThreshold(int threshold) {
		this.playerHydrationHealThreshold = threshold;
	}

	public Runnable getKillListener() {
		return killListener;
	}

	public void setKillListener(Runnable killListener) {
		this.killListener = killListener;
	}

	public void thirst(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		setHydration(hydration - amount);
	}

	public void hydrate(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHydration(this.hydration + amount);
	}

	public void damage(DamageData damageData) {
		if (damageData.amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}

		this.setHealth(this.health - damageData.amount);
		if (health <= 0) kill(damageData);
	}

	public void heal(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount cannot be negative.");
		}
		this.setHealth(this.health + amount);
	}

	public void kill(DamageData damageData) {
		if (killListener == null) return;
		killListener.run();
	}

	public void onCollision(Entity other) {
		
	}

	@Override
	public void update(double dt) {
		// Decrease hydration
		if (timeTillHydrationDecrease <= 0) {
			if (hydration <= 0) damage(new DamageData(null, 1));
			else if (hydration >= playerHydrationHealThreshold) heal(1);
			thirst(1);
			timeTillHydrationDecrease = hydrationDecreaseTime;
		} else {
			timeTillHydrationDecrease -= dt;
		}

		super.update(dt);
	}
}
