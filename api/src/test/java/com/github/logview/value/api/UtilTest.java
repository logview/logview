package com.github.logview.value.api;

import java.util.regex.Matcher;

import junit.framework.Assert;

import org.junit.Test;

import com.github.logview.util.Util;

public class UtilTest {
	@Test
	public void test1() {
		String msg = "test$test";
		Assert.assertEquals("test\\$test", Util.escape(msg));
		Assert.assertEquals("test\\\\\\$test", Util.escape(Util.escape(msg)));
	}

	public void testToken(String token) {
		testToken("", token, "");
		testToken("abc", token, "");
		testToken("", token, "abc");
		testToken("abc", token, "abc");
	}

	public void testToken(String prefix, String token, String suffix) {
		Matcher m = Util.matchToken(prefix + "$(" + token + ")" + suffix);
		Assert.assertTrue(m.find());
		Assert.assertEquals(token, m.group(1));
	}

	@Test
	public void testToken1() {
		testToken("");
		testToken("abc");
		testToken("[abc]");
		testToken("(abc)");
	}
}
