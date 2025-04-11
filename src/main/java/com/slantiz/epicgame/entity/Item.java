package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public abstract class Item extends Entity implements IInteractable {

	protected Pawn parent;

	public Item(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);
	}

	public Pawn getParent() {
		return parent;
	}

	public void setParent(Pawn parent) {
		this.parent = parent;
	}

	public abstract void use();

	public void drop() {
		parent = null;
	}
}
