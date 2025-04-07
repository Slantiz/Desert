package com.slantiz.epicgame.entity.components;

public interface IHealth {
	public void kill();
	public void damage(int amount);
	public void heal(int amount);
}
