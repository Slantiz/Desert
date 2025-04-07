package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

/** 
 * The base entity class.
 */
public abstract class Entity {
	
	protected Vec pos;
	protected double rot;
	protected Vec size;
	protected Vec pivot;
	protected Image sprite;

	/**
	 * Initiates a new entity.
	 * @param pos An initial position
	 * @param size An initial size
	 * @param sprite An image for rendering
	 */
	public Entity(Vec pos, Vec size, Image sprite) {
		this.pos = pos;
		this.sprite = sprite;
		this.size = size;

		rot = 0;
		pivot = size.copy().div(2);
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

	public Image getSprite() {
		return sprite;
	}

	public void setSprite(Image sprite) {
		this.sprite = sprite;
	}

	/**
	 * Updates the entity's state.
	 * This should run every frame.
	 * @param deltaTime Time (seconds) since last frame
	 */
	public abstract void update(double deltaTime);
}
