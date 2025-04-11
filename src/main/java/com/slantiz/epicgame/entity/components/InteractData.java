package com.slantiz.epicgame.entity.components;

import com.slantiz.epicgame.util.Vec;

public class InteractData {
	public String text; 
	public Vec textPos;

	public InteractData(String text, Vec textPos) {
		this.text = text;
		this.textPos = textPos;
	}
}
