package com.slantiz.epicgame.entity.components;

import com.slantiz.epicgame.entity.Pawn;

public interface IInteractable {
	public InteractData getInteractData();
	public void interact(Pawn entity);
}
