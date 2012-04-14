package com.github.logview.value.api;

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
}
