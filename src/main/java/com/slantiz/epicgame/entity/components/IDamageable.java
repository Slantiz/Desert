package com.slantiz.epicgame.entity.components;

public interface IDamageable {
	/**
	 * Triggered when the entity is damaged.
	 * @param amount Damage amount
	 */
	public void damage(int amount);

	/**
	 * Triggered when the entity is healed.
	 * @param amount Healing amount
	 */
	public void heal(int amount);

	/**
	 * Triggered when the entity is killed.
	 */
	public void kill();
}
