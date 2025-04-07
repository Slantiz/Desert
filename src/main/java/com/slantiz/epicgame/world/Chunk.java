package com.slantiz.epicgame.world;

import com.slantiz.epicgame.EntityFactory;
import com.slantiz.epicgame.entity.Cactus;
import com.slantiz.epicgame.util.Noise;
import com.slantiz.epicgame.util.Vec;

import javafx.scene.image.Image;

public class Chunk {

	final private int chunkX;
	final private int chunkY;
	final private Image sprite;
	final World world;

	public Chunk(int chunkX, int chunkY, Image sprite, Noise noise, World world) {
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.sprite = sprite;
		this.world = world;

		generate(noise, world);
	}

	private void generate(Noise noise, World world) {
		if (Math.random() < 0.5) {
			// spawn cactus
			Vec randomOffset = new Vec(Math.random() * world.getChunkNumUnits().x, Math.random() * world.getChunkNumUnits().y);
			Cactus cactus = EntityFactory.makeCactus(this.getPos().add(randomOffset));
			world.addEntity(cactus);
		}
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkY() {
		return chunkY;
	}

	public Image getSprite() {
		return sprite;
	}

	public Vec getPos() {
		Vec chunkNumUnits = world.getChunkNumUnits();
		return new Vec(chunkX * chunkNumUnits.x, chunkY * chunkNumUnits.y);
	}
}
