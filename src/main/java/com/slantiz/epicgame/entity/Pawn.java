package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.controllers.PawnController;
import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

/**
 * The base controllable entity class.
 */
public abstract class Pawn extends Entity {

	protected PawnController controller;
	protected Vec dir;
	protected Vec velocity;
	protected double facing;
	protected double acceleration;
	protected double speed;
	protected Item item;
	protected double interactRange;

	/**
	 * Initiates a new controllable entity.
	 * @param world The entity's world
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 */
	public Pawn(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);

		dir = Vec.zero();
		velocity = Vec.zero();
		facing = 0;
		acceleration = 1;
		speed = 2;
		item = null;
	}

	public PawnController getPawnController() {
		return controller;
	}

	public void setPawnController(PawnController controller) {
		this.controller = controller;
	}

	public Vec getDir() {
		return dir.copy();
	}

	/**
	 * Set the entity's movement direction.
	 * This is used by character controllers.
	 * @param dir Direction to move
	 */
	public void setDir(Vec dir) {
		this.dir = dir;
	}

	public double getFacing() {
		return facing;
	}

	/**
	 * Set the entity's facing angle.
	 * This is used by character controllers.
	 * @param facing Angle to face
	 */
	public void setFacing(double facing) {
		this.facing = facing;
	}

	public Vec getVelocity() {
		return velocity.copy();
	}

	/**
	 * Set the entity's velocity.
	 * This can be used by character controllers.
	 * @param velocity The character's velocity
	 */
	public void setVelocity(Vec velocity) {
		this.velocity = velocity;
	}

	public double getAcceleration() {
		return acceleration;
	}

	/**
	 * Set the entity's acceleration.
	 * This can be used by character controllers.
	 * @param acceleration The movement acceleration
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getSpeed() {
		return speed;
	}

	/**
	 * Set the entity's speed.
	 * This can be used by character controllers.
	 * @param speed The maximum movement speed
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getInteractRange() {
		return interactRange;
	}

	/**
	 * Set the entity's pick-up range.
	 * @param interactRange The pickUpRange
	 */
	public void setInteractRange(double range) {
		this.interactRange = range;
	}

	public Item getItem() {
		return item;
	}

	/**
	 * Set the entity's pick-up range.
	 * @param interactRange The pickUpRange
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	public void tryInteract() {
		IInteractable interactable = world.getNearestInteractable(this.getPos(), interactRange);
		if (interactable == null) return;
		interactable.interact(this);
	}

	public void tryUse() {
		if (item == null) return;
		item.use();
	}

	public void drop() {
		if (item != null) item.drop();
		item = null;
	}

	public void knockBack(Vec source, double minSpace, double force) {
		Vec direction = pos.sub(source);
		if (direction.len() > 0) direction = direction.normalized();
		setPos(source.add(direction).mul(minSpace));
		setVelocity(direction.mul(force));
	}

	public void knockBack(Vec direction, double force) {
		if (direction.len() > 0) direction = direction.normalized();
		setVelocity(direction.mul(force));
	}

	@Override
	public void update(double dt) {
		Vec targetVelocity = dir.mul(speed);
		Vec velocityDiff = targetVelocity.sub(velocity);
		velocity = velocity.add(velocityDiff.mul(acceleration * dt));
		pos = pos.add(velocity.mul(dt));
	}
}
