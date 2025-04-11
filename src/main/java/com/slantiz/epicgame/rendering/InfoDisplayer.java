package com.slantiz.epicgame.rendering;

import com.slantiz.epicgame.entity.Entity;
import com.slantiz.epicgame.entity.components.IInteractable;
import com.slantiz.epicgame.entity.components.InteractData;
import com.slantiz.epicgame.util.Vec;
import com.slantiz.epicgame.world.World;

import javafx.scene.text.Font;

public class InfoDisplayer {
	
	private Entity target;
	
	public InfoDisplayer() {

	}
	
	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public void infoNearestInteractable(World world, Renderer renderer, Font font) {
		IInteractable interactable = world.getNearestInteractable(target.getPos(), 2);
		if (interactable == null) return;
		InteractData interactData = interactable.getInteractData();

		Vec coord = renderer.screenCoord(interactData.textPos);
		renderer.renderText(font, coord, interactData.text);
	}
}
