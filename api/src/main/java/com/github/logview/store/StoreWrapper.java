package com.github.logview.store;

import java.util.Date;
import java.util.UUID;

public class StoreWrapper implements Store {
	protected final Store wrapped;

	public StoreWrapper(Store wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void storeBoolean(boolean b) {
		wrapped.storeBoolean(b);
	}

	@Override
	public void storeByte(int b) {
		wrapped.storeByte(b);
	}

	@Override
	public void storeShort(int s) {
		wrapped.storeShort(s);
	}

	@Override
	public void storeInteger(int i) {
		wrapped.storeInteger(i);
	}

	@Override
	public void storeLong(long l) {
		wrapped.storeLong(l);
	}

	@Override
	public void storeDouble(double d) {
		wrapped.storeDouble(d);
	}

	@Override
	public void storeString(String s) {
		wrapped.storeString(s);
	}

	@Override
	public void storeDate(Date d) {
		wrapped.storeDate(d);
	}

	@Override
	public void storeUUID(UUID u) {
		wrapped.storeUUID(u);
	}

	@Override
	public void storeObject(Object o) {
		wrapped.storeObject(o);
	}

	@Override
	public void close() {
		wrapped.close();
	}

	@Override
	public long size() {
		return wrapped.size();
	}
}
