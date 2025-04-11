package com.slantiz.epicgame.entity.components;

import com.slantiz.epicgame.entity.Entity;

public class DamageData {

	public Entity attacker;
	public int amount;

	public DamageData(Entity attacker, int amount) {
		this.attacker = attacker;
		this.amount = amount;
	}
}
