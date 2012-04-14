package com.github.logview.value.api;

import java.util.Date;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryValueOfTest {
	@Test
	public void testNull() {
		testNull(new ValueFactory());
		testNull(ValueFactory.getDefault());
	}

	private void testNull(ValueFactory subject) {
		Assert.assertNull(subject.valueOf(null));
		Assert.assertEquals("", subject.valueOf(""));
	}

	@Test
	public void testDouble() {
		ValueFactory subject = new ValueFactory();
		subject.load("DOUBLE");
		testDouble(subject);
		testDouble(ValueFactory.getDefault());
	}

	private void testDouble(ValueFactory subject) {
		Object result = subject.valueOf("0.0");
		Assert.assertTrue(result instanceof Double);
		Assert.assertEquals(0.0, result);

		result = subject.valueOf("123.456");
		Assert.assertTrue(result instanceof Double);
		Assert.assertEquals(123.456, result);

		result = subject.valueOf("-123.456");
		Assert.assertTrue(result instanceof Double);
		Assert.assertEquals(-123.456, result);
	}

	@Test
	public void testLong() {
		ValueFactory subject = new ValueFactory();
		subject.load("LONG");
		testLong(subject);
		testLong(ValueFactory.getDefault());
	}

	private void testLong(ValueFactory subject) {
		Object result = subject.valueOf("0");
		Assert.assertTrue(result instanceof Long);
		Assert.assertEquals(0L, result);

		result = subject.valueOf("123451234512345");
		Assert.assertTrue(result instanceof Long);
		Assert.assertEquals(123451234512345L, result);

		result = subject.valueOf("-123451234512345");
		Assert.assertTrue(result instanceof Long);
		Assert.assertEquals(-123451234512345L, result);
	}

	@Test
	public void testDate() {
		ValueFactory subject = new ValueFactory();
		subject.load("DATE format:HH:mm:ss,SSS");
		testDate(subject);
		testDate(ValueFactory.getDefault());
	}

	private void testDate(ValueFactory subject) {
		String date = "12:34:56,789";
		Object result = subject.valueOf(date);
		Assert.assertTrue(result instanceof Date);
		Assert.assertEquals(new Date(45296789), result);
	}

	@Test
	public void testSession() {
		ValueFactory subject = new ValueFactory();
		subject.load("SESSION");
		testSession(subject);
		testSession(ValueFactory.getDefault());
	}

	private void testSession(ValueFactory subject) {
		String session = "00000000000000000000000000000000";
		Object result = subject.valueOf(session);
		Assert.assertTrue(result instanceof String);
		Assert.assertEquals(session, result);

		session = "0123456789ABCDEF0123456789ABCDEF";
		result = subject.valueOf(session);
		Assert.assertTrue(result instanceof String);
		Assert.assertEquals(session, result);
	}

	@Test
	public void testSessionHost() {
		ValueFactory subject = new ValueFactory();
		subject.load("SESSIONHOST");
		testSessionHost(subject);
		testSessionHost(ValueFactory.getDefault());
	}

	private void testSessionHost(ValueFactory subject) {
		String session = "00000000000000000000000000000000.ip-1-2-3-4";
		Object result = subject.valueOf(session);
		Assert.assertTrue(result instanceof String);
		Assert.assertEquals(session, result);

		session = "0123456789ABCDEF0123456789ABCDEF.ip-192-168-0-1";
		result = subject.valueOf(session);
		Assert.assertTrue(result instanceof String);
		Assert.assertEquals(session, result);
	}

	@Test
	public void testBoolean() {
		ValueFactory subject = new ValueFactory();
		subject.load("BOOLEAN");
		subject.load("BOOLEAN true:TRUE false:FALSE");
		testBoolean(subject);
		testBoolean(ValueFactory.getDefault());
	}

	private void testBoolean(ValueFactory subject) {
		Object result = subject.valueOf("true");
		Assert.assertTrue(result instanceof Boolean);
		Assert.assertTrue((Boolean)result);

		result = subject.valueOf("TRUE");
		Assert.assertTrue(result instanceof Boolean);
		Assert.assertTrue((Boolean)result);

		result = subject.valueOf("false");
		Assert.assertTrue(result instanceof Boolean);
		Assert.assertFalse((Boolean)result);

		result = subject.valueOf("FALSE");
		Assert.assertTrue(result instanceof Boolean);
		Assert.assertFalse((Boolean)result);
	}

	@Test
	public void testUuid() {
		ValueFactory subject = new ValueFactory();
		subject.load("UUID");
		subject.load("UUID uppercase:true");
		testUuid(subject);
		testUuid(ValueFactory.getDefault());
	}

	private void testUuid(ValueFactory subject) {
		UUID uuid = UUID.randomUUID();

		Object result = subject.valueOf(uuid.toString().toLowerCase());
		Assert.assertTrue(result instanceof UUID);
		Assert.assertEquals(uuid, result);

		result = subject.valueOf(uuid.toString().toUpperCase());
		Assert.assertTrue(result instanceof UUID);
		Assert.assertEquals(uuid, result);
	}
}
