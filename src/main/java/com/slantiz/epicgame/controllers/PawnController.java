package com.slantiz.epicgame.controllers;

import com.slantiz.epicgame.entity.Pawn;

public abstract class PawnController {

	protected Pawn target;
	protected boolean enabled;

	public Pawn getTarget() {
		return target;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void possess(Pawn target) {
		this.target = target;
	}

	public void unpossess() {
		this.target = null;
	}

	public abstract void onDeath();
	public abstract void update(double dt);
}
