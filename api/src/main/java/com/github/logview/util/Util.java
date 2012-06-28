package com.github.logview.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.store.Store;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class Util {
	private final static String T(String t) {
		return "(" + t + "|\\(" + t + "\\))*";
	}

	private final static String T_0 = "[^\\(\\)]*";
	private final static String T_1 = T(T_0);
	private final static String T_2 = T(T_1);
	private final static String T_3 = T(T_2);
	private final static String TOKEN = "\\$\\((" + T_3 + ")\\)";
	private final static Pattern token = Pattern.compile(TOKEN);

	public static Matcher matchToken(String input) {
		return token.matcher(input);
	}

	public static Properties loadProps(String file) throws IOException {
		return loadProps(new FileInputStream(new File(file)), file);
	}

	public static Properties loadProps(Class<?> clazz, String resource) throws IOException {
		return loadProps(clazz.getResourceAsStream(resource), resource);
	}

	public static Properties loadProps(InputStream stream, String resource) throws IOException {
		Properties props = new Properties();
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

	public static void storeStrings(Store store, List<String> strings) {
//		System.err.println("storeStrings");
		store.storeLong(strings.size());
		for(String s : strings) {
			store.storeString(s);
		}
	}

	public static String escape(String string) {
		return string.replaceAll("([\\\\\\$\\^\\.\\(\\)\\-\\?\\*\\+\\{\\}\\[\\]])", "\\\\$1");
	}

	public static String escapeReplace(String string) {
		return string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
	}

	public static String escapeSpace(String string) {
		return string.replaceAll(" ", "\\\\_");
	}

	public static String unescapeSpace(String string) {
		return string.replaceAll("\\\\_", " ");
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

	public static List<String> loadList(Class<?> clazz, String resource) throws IOException {
		InputStream stream = clazz.getResourceAsStream(resource);
		if(stream == null) {
			throw new FileNotFoundException(resource);
		}

		Scanner scanner = new Scanner(clazz.getResourceAsStream(resource));
		List<String> ret = Lists.newLinkedList();

		try {
			while(scanner.hasNextLine()) {
				ret.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
		return ImmutableList.copyOf(ret);
	}

	public static String removeRegexSpaces(String match) {
		String ret = removeGroups(match);
		ret = ret.replaceAll("^\\^", "");
		ret = ret.replaceAll("\\$$", "");
		ret = ret.replaceAll("\\\\[sS][\\+\\*]?\\??", " ");
		ret = ret.replaceAll("\\\\(.)", "$1");
		return ret;
	}

	public static String trimLeft(String string) {
		char[] v = string.toCharArray();
		int from = 0;
		while((from < v.length) && (v[from] <= ' ')) {
			from++;
		}
		return string.substring(from);
	}

	public static String trimRight(String string) {
		char[] v = string.toCharArray();
		int to = v.length;
		while((to > 0) && (v[to - 1] <= ' ')) {
			to--;
		}
		return string.substring(0, to);
	}
}
