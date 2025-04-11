package com.slantiz.epicgame.util;

/**
 * A 2D vector class making 2D computations easier.
 */
public class Vec {
	public double x;
	public double y;

	public Vec(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static Vec zero(){
		return new Vec(0, 0);
	}

	public static Vec one() {
		return new Vec(1, 1);
	}

	public Vec copy() {
		return new Vec(this.x, this.y);
	}

	public Vec add(Vec other) {
		return new Vec(this.x + other.x, this.y + other.y);
	}

	public Vec sub(Vec other) {
		return new Vec(this.x - other.x, this.y - other.y);
	}

	public Vec mul(double k) {
		return new Vec(this.x * k, this.y * k);
	}

	public Vec div(double k) {
		return new Vec(this.x / k, this.y / k);
	}

	public Vec floorDiv(double k) {
		return new Vec(Math.floor(this.x / k), Math.floor(this.y / k));
	}

	public double sqrLen() {
		return Math.pow(this.x, 2) + Math.pow(this.y, 2);
	}

	public double sqrDist(Vec other) {
		return Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2);
	}

	public double len() {
		return Math.sqrt(this.sqrLen());
	}

	public double dist(Vec other) {
		return Math.sqrt(sqrDist(other));
	}

	public Vec normalized() {
		return this.copy().div(this.len());
	}

	public double dot(Vec other) {
		return this.x * other.x + this.y * other.y;
	}

	public double angleDeg(Vec other) {
		return Math.acos(this.dot(other) / (this.len() * other.len())) * 180 / Math.PI;
	}

	public double signedAngleDeg(Vec other) {
		return -Math.atan2(this.x * other.y - this.y * other.x, this.x * other.x + this.y * other.y) * 180 / Math.PI;
	}

	public Vec rotated(double angle) {
		double a = angle * Math.PI / 180;
		return new Vec(Math.cos(a) * this.x - Math.sin(a) * this.y, Math.sin(a) * this.x + Math.cos(a) * this.y);	
	}

	public static Vec fromRot(double angle) {
		return new Vec(0, -1).rotated(angle);
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", this.x, this.y);
	}

	@Override
	public boolean equals(Object obj) {
		return this.x == ((Vec)obj).x && this.y == ((Vec)obj).y;
	}
}
