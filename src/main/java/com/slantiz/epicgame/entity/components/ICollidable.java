package com.slantiz.epicgame.entity.components;

import com.slantiz.epicgame.entity.Entity;

public interface ICollidable {
	/**
	 * Triggered when a collision occurs with another collidable entity.
	 * @param other The other entity in the collision
	 */
	public void onCollision(Entity other);
}
