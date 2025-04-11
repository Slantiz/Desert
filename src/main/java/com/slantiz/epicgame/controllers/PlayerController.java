package com.slantiz.epicgame.controllers;

import com.slantiz.epicgame.input.InputController;
import com.slantiz.epicgame.rendering.Renderer;
import com.slantiz.epicgame.util.Vec;

public class PlayerController extends PawnController {

	/**
	 * Creates a new player controller.
	 * It requires the renderer because it needs to convert mouse coordinates to world coordinates.
	 * @param inputController An input controller
	 */
	public PlayerController(Renderer renderer, InputController inputController) {
		// Make player move on input
		inputController.registerMoveHandler((dir) -> {
			if (this.target == null) return;
			if (dir.sqrLen() != 0) dir = dir.normalized();
			target.setDir(dir);
		});

		inputController.registerInteractHandler(() -> {
			if (target == null) return;
			target.tryInteract();
		});

		inputController.registerUseHandler(() -> {
			if (target == null) return;
			target.tryUse();
		});

		// Set facing direction towards mouse
		inputController.registerMouseHandler((coord) -> {
			if (target == null) return;
			Vec dir = renderer.pos(coord).sub(target.getPos());
			double angle = dir.signedAngleDeg(new Vec(0, -1));
			target.setFacing(angle);
		});
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onDeath'");
	}

	@Override
	public void update(double dt) {

	}
}
