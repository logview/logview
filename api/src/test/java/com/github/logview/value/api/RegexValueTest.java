package com.github.logview.value.api;

import junit.framework.Assert;

import org.junit.Test;

import com.github.logview.regex.Match;

public class RegexValueTest {
	private final ValueFactory subject = ValueFactory.getInstance();

	@Test
	public void testDouble() {
		String string = "test double:0.0";
		Match result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Double);
		Assert.assertEquals(0.0, result.getValue(0));

		string = "test double:123456789.987654321";
		result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Double);
		Assert.assertEquals(123456789.987654321, result.getValue(0));

		string = "test double:-987654321.123456789";
		result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Double);
		Assert.assertEquals(-987654321.123456789, result.getValue(0));
	}

	@Test
	public void testLong() {
		String string = "test long:0";
		Match result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Long);
		Assert.assertEquals(0L, result.getValue(0));

		string = "test long:123451234512345";
		result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1L, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Long);
		Assert.assertEquals(123451234512345L, result.getValue(0));

		string = "test long:-987654321098765";
		result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(1, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Long);
		Assert.assertEquals(-987654321098765L, result.getValue(0));
	}

	@Test
	public void testMulti() {
		String string = "test long:0 double:0.0 boolean:true";
		Match result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(3, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Long);
		Assert.assertEquals(0L, result.getValue(0));
		Assert.assertTrue(result.getValue(1) instanceof Double);
		Assert.assertEquals(0.0, result.getValue(1));
		Assert.assertTrue(result.getValue(2) instanceof Boolean);
		Assert.assertEquals(true, result.getValue(2));

		string = "test boolean:FALSE boolean:true double:123.456 long:-55555";
		result = subject.parse(subject.analyse(string), string);
		Assert.assertNotNull(result);
		Assert.assertEquals(4, result.size());
		Assert.assertTrue(result.getValue(0) instanceof Boolean);
		Assert.assertEquals(false, result.getValue(0));
		Assert.assertTrue(result.getValue(1) instanceof Boolean);
		Assert.assertEquals(true, result.getValue(1));
		Assert.assertTrue(result.getValue(2) instanceof Double);
		Assert.assertEquals(123.456, result.getValue(2));
		Assert.assertTrue(result.getValue(3) instanceof Long);
		Assert.assertEquals(-55555L, result.getValue(3));
	}

	@Test
	public void testNull() {
		Assert.assertNull(subject.parse("\\d", "xyz"));
	}
}
