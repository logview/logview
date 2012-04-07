package com.github.logview.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class DefaultDataOutputFactory implements DataOutputFactory {
	private static DefaultDataOutputFactory instance;

	@Override
	public ByteArrayDataOutput create() {
		return ByteStreams.newDataOutput();
	}

	public static DataOutputFactory instance() {
		if(instance == null) {
			instance = new DefaultDataOutputFactory();
		}
		return instance;
	}
}
