package com.github.logview.util;

import com.google.common.io.ByteArrayDataOutput;

public class ByteArrayDataOutputWrapper implements ByteArrayDataOutput {
	private final ByteArrayDataOutput wrapped;

	public ByteArrayDataOutputWrapper(ByteArrayDataOutput wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void write(int b) {
		wrapped.write(b);
	}

	@Override
	public void write(byte[] b) {
		wrapped.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		wrapped.write(b, off, len);
	}

	@Override
	public void writeBoolean(boolean v) {
		wrapped.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) {
		wrapped.writeByte(v);
	}

	@Override
	public void writeShort(int v) {
		wrapped.writeShort(v);
	}

	@Override
	public void writeChar(int v) {
		wrapped.writeChar(v);
	}

	@Override
	public void writeInt(int v) {
		wrapped.writeInt(v);
	}

	@Override
	public void writeLong(long v) {
		wrapped.writeLong(v);
	}

	@Override
	public void writeFloat(float v) {
		wrapped.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) {
		wrapped.writeDouble(v);
	}

	@Override
	public void writeChars(String s) {
		wrapped.writeChars(s);
	}

	@Override
	public void writeUTF(String s) {
		wrapped.writeUTF(s);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void writeBytes(String s) {
		wrapped.writeBytes(s);
	}

	@Override
	public byte[] toByteArray() {
		return wrapped.toByteArray();
	}
}
