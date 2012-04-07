package com.github.logview.regex;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.stringpart.type.RegexPart;
import com.github.logview.stringtable.StringTableDebugImpl;
import com.github.logview.util.Util;

public class RegexMultiMatcherTest {
	protected static RegexMultiMatcher parser;

	@BeforeClass
	public static void beforeClass() throws IOException {
		PartFactory factory = new PartFactory();
		parser = new RegexMultiMatcher(Util.loadProps(RegexMultiMatcherTest.class, "regexlineparser.properties"),
				factory);
	}

	@Test
	public void testNoMatch() throws ParseException, ExecutionException {
		RegexMatcher matcher = parser.get("testint");
		Assert.assertNotNull(matcher);
		Assert.assertNull(matcher.match("no match"));
		Assert.assertNull(matcher.match("test message '1'"));
		Assert.assertNull(matcher.match("test message 'x'!"));
	}

	@Test
	public void testInts() throws ParseException, ExecutionException {
		StringTableDebugImpl strings = new StringTableDebugImpl();
		RegexConfigMatcher matcher = parser.get("testint");
		Assert.assertNotNull(matcher);
		for(int i = 0; i < 100; i++) {
			String msg = String.format("test message '%d'!", i);
			Part p = matcher.match(msg);
			Assert.assertArrayEquals(new byte[] {
				(byte)PartType.REGEX.ordinal(), 1, (byte)PartType.INT.ordinal(), 0, 0, 0, (byte)i, -104, -34, 8, -90
			}, strings.toByteArray(p));
			Assert.assertNotNull(p);
			Assert.assertTrue(p instanceof RegexPart);
			RegexPart lp = (RegexPart)p;
			Collection<Part> ps = lp.getParts();
			Assert.assertEquals(1, ps.size());
			Assert.assertEquals(i, lp.getValue(0));
			Assert.assertEquals(msg, p.toString());
		}
		Set<String> s = strings.getStrings();
		Assert.assertNotNull(s);
		Assert.assertEquals(1, s.size());
		Assert.assertTrue(s.contains(matcher.getConfig().toString()));
	}

	@Test
	public void testLongs() throws ParseException, ExecutionException {
		StringTableDebugImpl strings = new StringTableDebugImpl();
		RegexConfigMatcher matcher = parser.get("testlong");
		Assert.assertNotNull(matcher);
		for(int i = 0; i < 100; i++) {
			String msg = String.format("test message '%d'!", i);
			Part p = matcher.match(msg);
			Assert.assertArrayEquals(new byte[] {
				(byte)PartType.REGEX.ordinal(),
				1,
				(byte)PartType.LONG.ordinal(),
				0,
				0,
				0,
				0,
				0,
				0,
				0,
				(byte)i,
				-7,
				-31,
				-121,
				29
			}, strings.toByteArray(p));
			Assert.assertNotNull(p);
			Assert.assertTrue(p instanceof RegexPart);
			RegexPart lp = (RegexPart)p;
			Collection<Part> ps = lp.getParts();
			Assert.assertEquals(1, ps.size());
			Assert.assertEquals((long)i, (long)(Long)lp.getValue(0));
			Assert.assertEquals(msg, p.toString());
		}
		Set<String> s = strings.getStrings();
		Assert.assertNotNull(s);
		Assert.assertEquals(1, s.size());
		Assert.assertTrue(s.contains(matcher.getConfig().toString()));
	}

	@Test
	public void testDates() throws ParseException, ExecutionException {
		StringTableDebugImpl strings = new StringTableDebugImpl();
		RegexMatcher matcher = parser.get("testdate");
		Assert.assertNotNull(matcher);

		Part p = matcher.match("2010-01-01");
		Assert.assertNotNull(p);
		Assert.assertTrue(p.value() instanceof Date);
		Assert.assertEquals(new Date(1262300400000L), p.value());
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.DATE.ordinal(), 0, 0, 1, 37, -26, -9, -119, -128
		}, strings.toByteArray(p));

		p = matcher.match("1999-12-31");
		Assert.assertNotNull(p);
		Assert.assertTrue(p.value() instanceof Date);
		Assert.assertEquals(new Date(946594800000L), p.value());
		Assert.assertArrayEquals(new byte[] {
			(byte)PartType.DATE.ordinal(), 0, 0, 0, -36, 101, 114, 97, -128
		}, strings.toByteArray(p));

		Set<String> s = strings.getStrings();
		Assert.assertNotNull(s);
		Assert.assertEquals(0, s.size());
	}

	@Test
	public void testBoolean() throws ParseException, ExecutionException {
		StringTableDebugImpl strings = new StringTableDebugImpl();

		// config 'boolean true/false'
		RegexMatcher matcher = parser.get("testboolean");
		Assert.assertNotNull(matcher);
		// true
		Part p = matcher.match("true");
		Assert.assertNotNull(p);
		Assert.assertEquals(true, p.value());
		// false
		p = matcher.match("false");
		Assert.assertNotNull(p);
		Assert.assertEquals(false, p.value());

		// config 'boolean 0/1'
		matcher = parser.get("testboolean01");
		Assert.assertNotNull(matcher);
		// 1
		p = matcher.match("1");
		Assert.assertNotNull(p);
		Assert.assertEquals(true, p.value());
		// 0
		p = matcher.match("0");
		Assert.assertNotNull(p);
		Assert.assertEquals(false, p.value());

		Set<String> s = strings.getStrings();
		Assert.assertNotNull(s);
		Assert.assertEquals(0, s.size());
	}
}
