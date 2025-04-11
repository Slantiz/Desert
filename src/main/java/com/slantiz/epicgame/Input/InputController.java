package com.slantiz.epicgame.input;

import java.util.ArrayList;
import java.util.HashSet;

import com.slantiz.epicgame.util.Vec;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class InputController {

	private HashSet<KeyCode> activeKeys;
	private ArrayList<IMoveHandle> moveHandlers;
	private ArrayList<IMouseHandler> mouseHandlers;
	private ArrayList<IButtonHandler> interactHandlers;
	private ArrayList<IButtonHandler> useHandlers;
	private Vec moveDir;

	public InputController(Scene scene) {
		this.activeKeys = new HashSet<>();
		this.moveHandlers = new ArrayList<>();
		this.mouseHandlers = new ArrayList<>();
		this.interactHandlers = new ArrayList<>();
		this.useHandlers = new ArrayList<>();
		this.moveDir = Vec.zero();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode k = event.getCode();
				activeKeys.add(k);
				
				updateMove();

				if (k == KeyCode.E) {
					triggerInteract();
				}
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

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Vec mousePos = new Vec(event.getX(), event.getY());

				updateMouse(mousePos);
			}
		});

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MouseButton k = event.getButton();

				if (k == MouseButton.PRIMARY) {
					triggerUse();
				}
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

		for (IMoveHandle handler : this.moveHandlers) {
			handler.handleMove(dir);
		}

		this.moveDir = dir;
	}

	public void registerMoveHandler(IMoveHandle handler) {
		this.moveHandlers.add(handler);
	}

	private void updateMouse(Vec mousePos) {
		for (IMouseHandler handler : this.mouseHandlers) {
			handler.handleMouseMove(mousePos);
		}
	}

	public void registerMouseHandler(IMouseHandler handler) {
		this.mouseHandlers.add(handler);
	}

	public void triggerInteract() {
		for (IButtonHandler handler : this.interactHandlers) {
			handler.handle();
		}
	}

	public void registerInteractHandler(IButtonHandler handler) {
		this.interactHandlers.add(handler);
	}

	public void triggerUse() {
		for (IButtonHandler handler : this.useHandlers) {
			handler.handle();
		}
	}

	public void registerUseHandler(IButtonHandler handler) {
		this.useHandlers.add(handler);
	}

}
