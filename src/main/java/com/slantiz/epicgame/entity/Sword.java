package com.slantiz.epicgame.entity;

import com.slantiz.epicgame.entity.components.IDamageable;
import com.slantiz.epicgame.entity.components.InteractData;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.image.Image;

public class Sword extends Item {

	protected double rechargeTime;
	protected double stabDist;
	protected double damageRadius;
	protected double rechargeTimer;
	protected int damage;
	protected Vec hitPivot;

	public Sword(World world, Vec pos, Vec size, Image sprite) {
		super(world, pos, size, sprite);

		rechargeTime = 0.5;
		stabDist = 0.5;
		damageRadius = 0.2;
		hitPivot = new Vec(0, 0);
	}

	public double getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(double rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public double getStabDist() {
		return stabDist;
	}

	public void setStabDist(double stabDist) {
		this.stabDist = stabDist;
	}

	public double getDamageRadius() {
		return damageRadius;
	}

	public void setDamageRadius(double damageRadius) {
		this.damageRadius = damageRadius;
	}

	public double getRechargeTimer() {
		return rechargeTimer;
	}

	public void setRechargeTimer(double rechargeTimer) {
		this.rechargeTimer = rechargeTimer;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public Vec getHitPivot() {
		return hitPivot;
	}

	public void setHitPivot(Vec hitPivot) {
		this.hitPivot = hitPivot;
	}

	public Vec getTargetPos() {
		if (parent == null) return null;
		Vec offset = new Vec(0, -1).rotated(parent.getFacing());
		return parent.getPos().add(offset); 
	}

	// testing
	public Vec getHitPos() {
		return pos.add(hitPivot.rotated(rot));
	}

	@Override
	public void use() {
		if (rechargeTimer > 0) return;
		
		Vec hitPos = pos.add(hitPivot.rotated(rot));

		for (Entity entity : world.getEntitiesInRadius(hitPos, damageRadius)) {
			if (!(entity instanceof IDamageable)) continue;
			((IDamageable)entity).damage(damage);
			if (!(entity instanceof Pawn)) continue;
			((Pawn)entity).knockBack(Vec.fromRot(rot), 15);
		}

		rechargeTimer = rechargeTime;
	}

	@Override
	public InteractData getInteractData() {
		return new InteractData("Sword", this.getPos());
	}

	@Override
	public void interact(Pawn pawn) {
		if (pawn.getItem() != null) pawn.drop();
		parent = pawn;
		pawn.setItem(this);
	}

	@Override
	public void update(double dt) {
		if (parent == null) return;
		Vec offset = Vec.fromRot(parent.getFacing());
		
		if (rechargeTimer > 0) {
			offset = offset.add(offset.normalized().mul(stabDist * (rechargeTimer / rechargeTime)));
			rechargeTimer -= dt;
		}

		setRot(parent.getFacing());
		setPos(parent.getPos().add(offset));
	}
}
