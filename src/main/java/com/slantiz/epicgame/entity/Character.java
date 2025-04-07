package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

/**
 * The base controllable entity class.
 */
public abstract class Character extends Entity {

	protected Vec dir;
	protected double facing;
	protected Vec velocity;
	protected double speed;

	/**
	 * Initiates a new controllable entity.
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 */
	public Character(Vec pos, Vec size, Image sprite) {
		super(pos, size, sprite);

		this.dir = Vec.zero();
		this.facing = 0;
		this.velocity = Vec.zero();
	}

	public Vec getDir() {
		return this.dir.copy();
	}

	/**
	 * Set the entity's movement direction.
	 * This is used by controllers.
	 * @param dir Direction to move
	 */
	public void setDir(Vec dir) {
		this.dir = dir;
	}

	public double getFacing() {
		return this.facing;
	}

	/**
	 * Set the entity's facing angle.
	 * This is used by controllers.
	 * @param facing Angle to face
	 */
	public void setFacing(double facing) {
		this.facing = facing;
	}

	public Vec getVelocity() {
		return this.velocity.copy();
	}

	public void setVelocity(Vec velocity) {
		this.velocity = velocity;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		if (speed < 0) {
			throw new IllegalArgumentException("speed cannot be negative.");
		}
		this.speed = speed;
	}

	@Override
	public void update(double dt) {
		// Simply translate
		Vec velocityDiff = dir.mul(speed).sub(velocity);
		velocity = velocity.add(velocityDiff.mul(0.1));
		pos = this.pos.add(velocity.mul(dt));
	}
}
