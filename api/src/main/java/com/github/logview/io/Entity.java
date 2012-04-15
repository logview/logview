package com.github.logview.io;

public class Entity<T> {
	public final long key;
	public final T value;

	public Entity(long key, T value) {
		this.key = key;
		this.value = value;
	}
}
