package com.github.logview.store;

import java.util.Date;
import java.util.UUID;

public interface Store {
	void storeBoolean(boolean b);

	void storeByte(int b);

	void storeShort(int s);

	void storeInteger(int i);

	void storeLong(long l);

	void storeDouble(double d);

	void storeString(String s);

	void storeDate(Date d);

	void storeUUID(UUID u);

	void storeObject(Object o);

	void close();

	long size();
}
