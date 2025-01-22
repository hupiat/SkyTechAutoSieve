package com.skytechautosieve.utils;

public abstract class InternalTools {

	public static void waitForTicks(Runnable callback, int ticks) {
		for (int i = 0; i < ticks; i++) {
			if (i < ticks) {
				continue;
			} else {
				callback.run();
			}
		}
	}
}
