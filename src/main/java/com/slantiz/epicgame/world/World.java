package com.slantiz.epicgame.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.slantiz.epicgame.controllers.PawnController;
import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.Item;
import com.slantiz.epicgame.entity.components.ICollidable;
import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.util.Noise;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.util.VecHasher;

import javafx.scene.image.Image;

public class World {

	private Vec chunkNumUnits;
	private Image chunkSprite;

	private ArrayList<Entity> entities;
	private HashMap<Long, Chunk> chunks;
	private ArrayList<PawnController> pawnControllers;

	public World(Vec chunkSize) {
		this.chunkNumUnits = chunkSize;

		this.entities = new ArrayList<>();
		this.chunks = new HashMap<>();
		this.pawnControllers = new ArrayList<>();
	}

	public Vec getChunkNumUnits() {
		return chunkNumUnits.copy();
	}

	public HashMap<Long, Chunk> getChunks() {
		return this.chunks;
	}

	public Image getChunkSprite() {
		return chunkSprite;
	}

	public void setChunkSprite(Image chunkSprite) {
		this.chunkSprite = chunkSprite;
	}

	public void generateChunk(int chunkX, int chunkY, Noise noise) {
		long hash = VecHasher.szudzikHash(chunkX, chunkY);
		if (chunks.containsKey(hash)) return;
		chunks.put(hash, new Chunk(chunkX, chunkY, chunkSprite, noise, this));
	}

	public void generateChunks(Vec pos, int generationRadius, Noise noise) {
		int curChunkX = pos.x >= 0.0 ? (int)(pos.x / chunkNumUnits.x) : (int)(pos.x / chunkNumUnits.x - 1);
		int curChunkY = pos.y >= 0.0 ? (int)(pos.y / chunkNumUnits.y) : (int)(pos.y / chunkNumUnits.y - 1);

		// Generate new chunks in a square around pos.
		for (int y = -generationRadius; y <= generationRadius; y++) {
			for (int x = -generationRadius; x <= generationRadius; x++) {
				generateChunk(curChunkX + x, curChunkY + y, noise);
			}
		}
	}

	public void removeChunk(int chunkX, int chunkY) {
		long hash = VecHasher.szudzikHash(chunkX, chunkY);
		chunks.remove(hash);
	}

	public void removeChunks(Vec pos, double radius) {
		ArrayList<Chunk> chunksToRemove = new ArrayList<>();

		// Append chunks outside radius to chunksToRemove
		for (Chunk chunk : this.chunks.values()) {
			if (pos.sqrDist(chunk.getPos()) > Math.pow(radius, 2)) chunksToRemove.add(chunk);
		}

		// Actually remove the chunks
		for (Chunk chunk : chunksToRemove) {
			this.chunks.remove(VecHasher.szudzikHash(chunk.getChunkX(), chunk.getChunkY()));
		}
	}

	public void update(double dt) {
		// Update every entity
		for (Entity entity : entities) {
			entity.update(dt);
		}

		// Trigger collisions
		for (Entity entity : entities) {
			 if (!(entity instanceof ICollidable)) continue;
			 var entitiesInRadius = getEntitiesInRadius(entity.getPos(), 1);
			 for (Entity other : entitiesInRadius) {
				if (!(other instanceof ICollidable)) continue;
				if (entity == other) continue;
				((ICollidable)other).onCollision(entity);
			 }
		}

		// Tick controllers
		for (PawnController controller : pawnControllers) {
			controller.update(dt);
		}
	}

	public IInteractable getNearestInteractable(Vec pos, double radius) {
		IInteractable interactable = null;
		double dist = Float.POSITIVE_INFINITY;
		for (Entity entity : getEntitiesInRadius(pos, radius)) {
			if (!(entity instanceof IInteractable)) continue;
			if (entity instanceof Item && ((Item)entity).getParent() != null) continue;
			double newDist = pos.sqrDist(entity.getPos());

			if (interactable == null) {
				interactable = (IInteractable)entity;
				dist = newDist;
				continue;
			}
			
			if (newDist < dist) {
				interactable = (IInteractable)entity;
				dist = newDist;
			}
		}

		return interactable;
	}

	public ArrayList<Entity> getEntitiesInRadius(Vec pos, double radius) {
		ArrayList<Entity> entitiesInRadius = new ArrayList<>();
		for (Entity entity : entities) {
			if (entity.getPos().sqrDist(pos) > radius * radius) continue;
			entitiesInRadius.add(entity);
		}
		return entitiesInRadius;
	}

	public double leastRemoveRadius(int generationRadius) {
		return Math.sqrt(Math.pow((generationRadius + 1) * Math.max(chunkNumUnits.x, chunkNumUnits.y), 2) * 2);
	}

	public ArrayList<Entity> getEntities() {
		return this.entities;
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		this.entities.remove(entity);
	}

	public ArrayList<PawnController> getPawnControllers() {
		return this.pawnControllers;
	}

	public void addPawnController(PawnController controller) {
		this.pawnControllers.add(controller);
	}

	public void removePawnController(PawnController controller) {
		this.pawnControllers.remove(controller);
	}
}
