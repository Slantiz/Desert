package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

/** 
 * The base entity class.
 */
public abstract class Entity {
	
	protected World world;
	protected Vec pos;
	protected Vec size;
	protected Image sprite;
	protected double rot;
	protected Vec pivot;

	/**
	 * Initiates a new entity.
	 * @param world The entity's world
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 */
	public Entity(World world, Vec pos, Vec size, Image sprite) {
		this.world = world;
		this.pos = pos;
		this.sprite = sprite;
		this.size = size;

		rot = 0;

		// Center pivot
		pivot = size.copy().div(2);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vec getPos() {
		return pos.copy();
	}

	public void setPos(Vec pos) {
		this.pos = pos;
	}

	public Vec getSize() {
		return size.copy();
	}

	public void setSize(Vec size) {
		this.size = size;
	}

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	public double getRot() {
		return rot;
	}

	public void setRot(double rot) {
		this.rot = rot;
	}

	public Vec getPivot() {
		return pivot.copy();
	}

	public void setPivot(Vec pivot) {
		this.pivot = pivot;
	}

	/**
	 * This runs when the entity is destroyed.
	 */
	public void destroy() {

	}

	/**
	 * Updates the entity's state.
	 * This is called every frame.
	 * @param deltaTime Time (seconds) since last frame
	 */
	public abstract void update(double deltaTime);
}
