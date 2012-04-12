package com.github.logview.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Util {
	public static Properties loadProps(Class<?> clazz, String resource) throws IOException {
		Properties props = new Properties();
		InputStream stream = clazz.getResourceAsStream(resource);
		if(stream == null) {
			throw new FileNotFoundException(resource);
		}
		props.load(stream);
		return props;
	}

	public static String[] readStringArray(ByteArrayDataInput data) {
		String[] ret = new String[data.readByte()];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = data.readUTF();
		}
		return ret;
	}

	public static void writeStringArray(ByteArrayDataOutput data, String[] strings) {
		data.writeByte(strings.length);
		for(int i = 0; i < strings.length; i++) {
			data.writeUTF(strings[i]);
		}
	}

	public static String escape(String string) {
		return string.replaceAll("\\.", "\\.").replaceAll("\\(", "\\(").replaceAll("\\)", "\\)");
	}

	public static String escapeReplace(String string) {
		return string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
	}

	public static String space(String string) {
		return string.replaceAll("[^_]?_", " ").replaceAll("__", "_");
	}

	public static String toHex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		return String.format("%0" + (bytes.length << 1) + "x", bi);
	}

	public static String removeGroups(String regex) {
		return regex.replaceAll("([^\\\\]|^)\\(", "$1").replaceAll("([^\\\\]|^)\\)", "$1");
	}

	public static Map<String, String> loadMap(Class<?> clazz, String resource) throws IOException {
		Map<String, String> tmp = Maps.newHashMap();
		for(Entry<Object, Object> e : loadProps(clazz, resource).entrySet()) {
			tmp.put(e.getKey().toString(), e.getValue().toString());
		}
		return ImmutableMap.copyOf(tmp);
	}

	public static String loadString(Class<?> clazz, String resource, String key) throws IOException {
		for(Entry<Object, Object> e : loadProps(clazz, resource).entrySet()) {
			if(key.equals(e.getKey())) {
				return e.getValue().toString();
			}
		}
		return null;
	}
}
