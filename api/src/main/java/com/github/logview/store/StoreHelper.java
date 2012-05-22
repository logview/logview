package com.github.logview.store;

import java.util.Date;
import java.util.UUID;

public class StoreHelper {
	public static void storeObject(Store store, Object o) {
		if(o instanceof Boolean) {
			store.storeByte(1);
			store.storeBoolean((Boolean)o);
		} else if(o instanceof Short) {
			store.storeByte(2);
			store.storeShort((Short)o);
		} else if(o instanceof Integer) {
			store.storeByte(3);
			store.storeInteger((Integer)o);
		} else if(o instanceof Long) {
			store.storeByte(4);
			store.storeLong((Long)o);
		} else if(o instanceof Double) {
			store.storeByte(5);
			store.storeDouble((Double)o);
		} else if(o instanceof String) {
			store.storeByte(6);
			store.storeString((String)o);
		} else if(o instanceof Date) {
			store.storeByte(7);
			store.storeDate((Date)o);
		} else if(o instanceof UUID) {
			store.storeByte(8);
			store.storeUUID((UUID)o);
		} else {
			store.storeByte(0);
		}
	}
}
