package com.github.logview.store;

import java.util.Date;
import java.util.UUID;

public abstract class AbstractStore implements Store {
	@Override
	public void storeObject(Object o) {
		StoreHelper.storeObject(this, o);
	}

	@Override
	public void storeDate(Date d) {
		storeLong(d.getTime());
	}

	@Override
	public void storeUUID(UUID u) {
		storeLong(u.getMostSignificantBits());
		storeLong(u.getLeastSignificantBits());
	}

	@Override
	public void close() {
	}
}
