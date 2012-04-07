package com.github.logview.util;

import com.google.common.io.ByteArrayDataOutput;

public interface DataOutputFactory {
	ByteArrayDataOutput create();
}
