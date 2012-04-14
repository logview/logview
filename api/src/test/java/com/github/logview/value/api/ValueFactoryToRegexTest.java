package com.github.logview.value.api;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryToRegexTest {
	private final ValueFactory subject = ValueFactory.getDefault();

	private void testSame(String expected, String string) {
		Assert.assertEquals(expected, subject.toRegex(string));
	}

	@Test
	public void testNull() {
		testSame(null, null);
		testSame("", "");
		testSame("test123...", "test123...");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeNotFound() {
		subject.toRegex("$(test)");
	}

	@Test
	public void testDouble() {
		testSame("(\\-?\\d+\\.\\d+)", "$(DOUBLE)");
		testSame("(\\-?\\d+,\\d+)", "$(DOUBLE dot:false)");
	}

	@Test
	public void testMulti() {
		testSame("(\\-?\\d+\\.\\d+)-(\\-?\\d+)-(true|false)", "$(DOUBLE)-$(LONG)-$(BOOLEAN)");
	}

	@Test
	public void testLong() {
		testSame("(\\-?\\d+)", "$(LONG)");
	}

	@Test
	public void testDate() {
		testSame("(\\d{2}\\:\\d{2}\\:\\d{2},\\d{3})", "$(DATE format:HH:mm:ss,SSS)");
	}

	@Test
	public void testBoolean() {
		testSame("(true|false)", "$(BOOLEAN)");
		testSame("(TRUE|FALSE)", "$(BOOLEAN true:TRUE false:FALSE)");
	}

	@Test
	public void testUuid() {
		testSame("([0-9a-f]{8}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{4}\\-[0-9a-f]{12})", "$(UUID)");
		testSame("([0-9A-F]{8}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{4}\\-[0-9A-F]{12})", "$(UUID uppercase:true)");
	}

	@Test
	public void testSession() {
		testSame("([0-9a-f]{32})", "$(SESSION)");
		testSame("([0-9A-F]{32})", "$(SESSION uppercase:true)");
	}

	@Test
	public void testSessionHost() {
		testSame("([0-9a-f]{32}\\.[0-9a-z\\-]+)", "$(SESSIONHOST)");
		testSame("([0-9A-F]{32}\\.[0-9a-z\\-]+)", "$(SESSIONHOST uppercase:true)");
	}
}
