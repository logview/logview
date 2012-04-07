package com.github.logview.stringpart.api;

import com.google.common.io.ByteArrayDataInput;

public interface PartCreator {
	Part createInstance(ByteArrayDataInput data);

	Part createInstance(String data);
}
