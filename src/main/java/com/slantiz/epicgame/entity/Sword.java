package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class Sword extends Entity {
	
	public Character target;

	public Sword(Vec pos, Vec size, Image sprite) {
		super(pos, size, sprite);
	}

	public Character getTarget() {
		return this.target;
	}

	public void setTarget(Character target) {
		this.target = target;
	}

	@Override
	public void update(double deltaTime) {
		if (target != null) {
			this.pos = this.target.getPos();
		}
	}
	
}
