package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.IHoldable;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Sword extends Entity implements IHoldable {

	public Sword(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);
	}

	@Override
	public void pickUp() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'pickUp'");
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'use'");
	}

	@Override
	public void drop() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'drop'");
	}

	@Override
	public void update(double deltaTime) {
		
	}
}
