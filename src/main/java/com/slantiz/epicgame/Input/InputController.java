package com.slantiz.epicgame.Input;

import java.util.ArrayList;
import java.util.HashSet;

import com.slantiz.epicgame.util.Vec;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InputController {

	private HashSet<KeyCode> activeKeys;
	private ArrayList<IMoveHandle> moveHandles;
	private ArrayList<IMouseHandler> mouseHandles;
	private Vec moveDir;
	private Vec cursorPos;

	public InputController(Scene scene) {
		this.activeKeys = new HashSet<>();
		this.moveHandles = new ArrayList<>();
		this.mouseHandles = new ArrayList<>();
		this.moveDir = Vec.zero();
		this.cursorPos = Vec.zero();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode k = event.getCode();
				activeKeys.add(k);
				
				updateMove();
            }
        });

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode k = event.getCode();
				activeKeys.remove(k);

				updateMove();
			}
		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vec mousePos = new Vec(event.getX(), event.getY());

				updateMouse(mousePos);
			}

		});
    }

	private void updateMove() {
		Vec dir = Vec.zero();

		if (this.activeKeys.contains(KeyCode.W) || this.activeKeys.contains(KeyCode.UP)) dir.y -= 1;
		if (this.activeKeys.contains(KeyCode.A) || this.activeKeys.contains(KeyCode.LEFT)) dir.x -= 1;
		if (this.activeKeys.contains(KeyCode.S) || this.activeKeys.contains(KeyCode.DOWN)) dir.y += 1;
		if (this.activeKeys.contains(KeyCode.D) || this.activeKeys.contains(KeyCode.RIGHT)) dir.x += 1;

		if (this.moveDir.equals(dir)) return;

		for (IMoveHandle handler : this.moveHandles) {
			handler.handleMove(dir);
		}

		this.moveDir = dir;
	}

	public void registerMoveHandler(IMoveHandle handler) {
		this.moveHandles.add(handler);
	}

	private void updateMouse(Vec mousePos) {
		for (IMouseHandler handler : this.mouseHandles) {
			handler.handleMouseMove(mousePos);
		}
	}

	public void registerMouseHandler(IMouseHandler handler) {
		this.mouseHandles.add(handler);
	}

}
