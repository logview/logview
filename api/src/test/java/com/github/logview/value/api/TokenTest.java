package com.github.logview.value.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
	private final static String regex = "\\$\\(([^\\)]*?)\\)";

	@Test
	public void test1() {
		Matcher m = Pattern.compile(regex).matcher("test:$(A)");
		Assert.assertTrue(m.find());
		Assert.assertEquals("A", m.group(1));
		Assert.assertFalse(m.find());
	}

	@Test
	public void test2() {
		Matcher m = Pattern.compile(regex).matcher("test2:$(X) $(Y)");
		Assert.assertTrue(m.find());
		Assert.assertEquals("X", m.group(1));
		Assert.assertTrue(m.find());
		Assert.assertEquals("Y", m.group(1));
		Assert.assertFalse(m.find());
	}
}
