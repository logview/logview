package com.github.logview.store;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ByteStore extends AbstractStore {
	private final ByteArrayDataOutput out = ByteStreams.newDataOutput();

	@Override
	public long size() {
		return out.toByteArray().length;
	}

	@Override
	public void storeBoolean(boolean b) {
//		System.err.printf("[boolean:%02X]", b ? 1 : 0);
		out.writeBoolean(b);
	}

	@Override
	public void storeByte(int b) {
//		System.err.printf("[byte:%02X]", b);
		out.write(b);
	}

	@Override
	public void storeShort(int s) {
//		System.err.printf("[short:%04X]", s);
		out.writeShort(s);
	}

	@Override
	public void storeInteger(int i) {
//		System.err.printf("[int:%08X]", i);
		out.writeInt(i);
	}

	@Override
	public void storeLong(long l) {
//		System.err.printf("[long:%016X]", l);
		out.writeLong(l);
	}

	@Override
	public void storeDouble(double d) {
//		System.err.printf("[double:%016X]", Double.doubleToRawLongBits(d));
		out.writeDouble(d);
	}

	@Override
	public void storeString(String s) {
//		System.err.printf("'%s'", s);
		out.writeUTF(s);
	}

	public byte[] getBytes() {
		return out.toByteArray();
	}
}
