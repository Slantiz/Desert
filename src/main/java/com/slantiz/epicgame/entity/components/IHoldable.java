package com.slantiz.epicgame.entity.components;

public interface IHoldable {
	/**
	 * Triggered when the entity is picked up.
	 */
	public void pickUp();

	/**
	 * Triggered when the entity is used.
	 */
	public void use();

	/**
	 * Triggered when the entity is dropped.
	 */
	public void drop();
}
