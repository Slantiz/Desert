package com.slantiz.epicgame.rendering;

import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.util.Vec;

/**
 * A class containing information about a camera.
 */
public class Camera {

	private Vec pos;
	private double unitsInWidth;
	private Entity target;

	public Camera(Vec pos, double widthNumUnits) {
		this.pos = pos;
		this.unitsInWidth = widthNumUnits;
	}

	public Vec getPos() {
		return this.pos.copy();
	}

	public double getUnitsInWidth() {
		return unitsInWidth;
	}

	public void setWidthNumPixels(double widthNumUnits) {
		this.unitsInWidth = widthNumUnits;
	}

	public Entity getTarget() {
		return this.target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public void update(double dt) {
		Vec diff = this.target.getPos().sub(this.pos).mul(5 * dt);
		this.pos = this.pos.add(diff);
	}
}
