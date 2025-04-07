package com.slantiz.epicgame;

import com.slantiz.epicgame.Input.InputController;
import com.slantiz.epicgame.entity.Player;
import com.slantiz.epicgame.entity.Sword;
import com.slantiz.epicgame.rendering.Renderer;
import com.slantiz.epicgame.util.Vec;

public class PlayerController {

	private Player target;
	private Sword sword;

	/**
	 * Creates a new player controller.
	 * It requires the renderer because it needs to convert mouse coordinates to world coordinates.
	 * @param renderer The renderer of the world
	 * @param inputController An input controller
	 */
	public PlayerController(Renderer renderer, InputController inputController) {

		// Make player move on input
		inputController.registerMoveHandler((dir) -> {
			if (this.target == null) return;
			target.setDir(dir);
		});

		// Make sword point toward mouse
		inputController.registerMouseHandler((mousePos) -> {
			if (this.sword == null) return;
			Vec dir = renderer.pos(mousePos).sub(target.getPos());
			double angle = Math.copySign(1, dir.x) * dir.angleDeg(new Vec(0, -1));
			sword.setRot(angle);
		});
	}

	public Player getPlayer() {
		return this.target;
	}

	public void setPlayer(Player target) {
		this.target = target;
	}

	public Sword getSword() {
		return this.sword;
	}

	public void setSword(Sword sword) {
		this.sword = sword;
	}
}
