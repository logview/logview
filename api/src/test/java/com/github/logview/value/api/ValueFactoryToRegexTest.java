package com.github.logview.value.api;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryToRegexTest {
	private final ValueFactory subject = ValueFactory.createDefault();

	private void testSame(String string) {
		testSame(string, string);
	}

	private void testSame(String string, boolean escape) {
		testSame(string, string, escape);
	}

	private void testSame(String expected, String string) {
		testSame(expected, string, true);
		testSame(expected, string, false);
	}

	private void testSame(String expected, String string, boolean escape) {
		Assert.assertEquals(expected, subject.toRegex(string, escape));
		if(expected != null) {
			Pattern p = Pattern.compile(expected);
			java.util.regex.Matcher m = p.matcher("");
			Assert.assertFalse(m.find());
			m.reset();
			Assert.assertFalse(m.matches());
		}
	}

	@Test
	public void testNull() {
		testSame(null);
		testSame(" ");
		testSame("test123...", false);
		testSame("test123\\.\\.\\.", "test123...", true);
	}

	@Test
	public void testRegex() {
		testSame("test - test", false);
		testSame("test \\- test", "test - test", true);
		testSame("test $ test", false);
		testSame("test \\$ test", "test $ test", true);
		testSame("test ^ test", false);
		testSame("test \\^ test", "test ^ test", true);
		testSame("test \\ test", false);
		testSame("test \\\\ test", "test \\ test", true);
	}

	@Test(expected = PatternSyntaxException.class)
	public void testRegexFail() {
		testSame("test [x-/] test", false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeNotFound1() {
		subject.toRegex("$(test)", true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeNotFound2() {
		subject.toRegex("$(test)", false);
	}

	@Test
	public void testDouble() {
		testSame("(\\-?\\d+\\.\\d+)", "$(DOUBLE)");
		testSame("(\\-?\\d+,\\d+)", "$(DOUBLE dot:false)");
	}

	@Test
	public void testMulti1() {
		testSame("(\\-?\\d+\\.\\d+)\\-(\\-?\\d+)\\-(true|false)", "$(DOUBLE)-$(LONG)-$(BOOLEAN)", true);
		testSame("(\\-?\\d+\\.\\d+)-(\\-?\\d+)-(true|false)", "$(DOUBLE)-$(LONG)-$(BOOLEAN)", false);
	}

	@Test
	public void testMulti2() {
		testSame("\\- (\\-?\\d+\\.\\d+) \\- (\\-?\\d+) \\- (true|false) \\-", "- $(DOUBLE) - $(LONG) - $(BOOLEAN) -",
			true);
		testSame("- (\\-?\\d+\\.\\d+) - (\\-?\\d+) - (true|false) -", "- $(DOUBLE) - $(LONG) - $(BOOLEAN) -", false);
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
