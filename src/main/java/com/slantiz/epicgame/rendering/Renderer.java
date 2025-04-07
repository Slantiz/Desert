package com.slantiz.epicgame.rendering;

import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.Chunk;
import com.slantiz.epicgame.world.World;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Acts as an controller for a camera.
 */
public class Renderer {

	private Canvas canvas;
	private Camera camera;

	public Renderer() {

	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public double getPixelsInUnit() {
		return canvas.getWidth() / camera.getUnitsInWidth();
	}

	public Vec screenCoord(Vec pos) {
		return pos.sub(camera.getPos()).mul(getPixelsInUnit())
			.add(new Vec(canvas.getWidth(), canvas.getHeight()).div(2));
	}

	public Vec pos(Vec screenCoord) {
		Vec offset = screenCoord.sub(new Vec(canvas.getWidth(), canvas.getHeight()).div(2))
			.div(getPixelsInUnit());
		return camera.getPos().add(offset);
	}
	
	public void renderWorld(World world) {
		// Rendering order:
		// 1. Chunks
		// 2. Entities

		clearCanvas();

		for (Chunk chunk : world.getChunks().values()) {
			renderChunk(chunk, world);
		}

		// Render entities
		for (Entity entity : world.getEntities()) {
			renderEntity(entity, world);
		}
	}

	public void clearCanvas() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.rgb(50, 50, 60));
		gc.fillRect(0, 0, canvas.getWidth(),canvas.getHeight());
	}

	public void renderChunk(Chunk chunk, World world) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Vec coord = screenCoord(chunk.getPos());
		Vec chunkPixelSize = world.getChunkNumUnits().mul(getPixelsInUnit());
		gc.drawImage(chunk.getSprite(), coord.x, coord.y, chunkPixelSize.x, chunkPixelSize.y);
	}

	public void renderEntity(Entity entity, World world) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Vec posCoord = screenCoord(entity.getPos());
		Vec pivotOffsetCoord = entity.getPivot().mul(getPixelsInUnit());
		Vec drawCoord = posCoord.sub(pivotOffsetCoord);
		Vec entityPixelSize = entity.getSize().mul(getPixelsInUnit());

		if (entity.getRot() != 0) {
			// Perform rotated render
			gc.save();
			gc.translate(posCoord.x, posCoord.y);
			gc.rotate(entity.getRot());
			gc.drawImage(entity.getSprite(), -pivotOffsetCoord.x, -pivotOffsetCoord.y, entityPixelSize.x, entityPixelSize.y);
			gc.translate(-drawCoord.x, drawCoord.y);
			gc.restore();
		}
		else {
			// Perform non-rotated render
			gc.drawImage(entity.getSprite(), drawCoord.x, drawCoord.y, entityPixelSize.x, entityPixelSize.y);
		}
	}

	public void renderSprite(Vec coord, Vec size, Image sprite) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.drawImage(sprite, coord.x, coord.y, size.x, size.y);
	}

	public void renderText(Vec coord, int size, String text) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.save();
		Font f = Font.loadFont(Renderer.class.getResource("/mc_font.ttf").toExternalForm(), size);
		gc.setFont(f);
		gc.setFill(Color.BLACK);
		gc.fillText(text, coord.x, coord.y);
		gc.restore();
	}
}
