package com.slantiz.epicgame.entity.components;

import com.slantiz.epicgame.entity.Entity;

public interface ICollidable {
	public void onCollision(Entity other);
}
