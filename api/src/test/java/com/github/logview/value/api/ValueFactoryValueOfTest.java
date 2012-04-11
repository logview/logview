package com.github.logview.value.api;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryValueOfTest {
	private final ValueFactory subject = ValueFactory.getInstance();

	@Test
	public void testNull() {
		Assert.assertNull(subject.valueOf(null));
		Assert.assertEquals("", subject.valueOf(""));
	}

	@Test
	public void testDouble() {
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
	public void testSession() {
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
		UUID uuid = UUID.randomUUID();

		Object result = subject.valueOf(uuid.toString().toLowerCase());
		Assert.assertTrue(result instanceof UUID);
		Assert.assertEquals(uuid, result);

		result = subject.valueOf(uuid.toString().toUpperCase());
		Assert.assertTrue(result instanceof UUID);
		Assert.assertEquals(uuid, result);
	}
}
