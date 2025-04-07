package com.slantiz.epicgame.util;

public abstract class VecHasher {
	public static long szudzikHash(int a, int b) {
		int A = a >= 0 ? 2 * a : -2 * a - 1;
		int B = b >= 0 ? 2 * b : -2 * b - 1;
		long C = (A >= B ? A * A + A + B : A + B * B) / 2;
		return a < 0 && b < 0 || a >= 0 && b >= 0 ? C : -C - 1;
	} 
}
