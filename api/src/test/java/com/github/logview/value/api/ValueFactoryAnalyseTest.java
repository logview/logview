package com.github.logview.value.api;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryAnalyseTest {

	private static final String TYPE_H1 = "SESSIONHOST";
	private static final String TYPE_H2 = "SESSIONHOST uppercase:true";
	private static final String TYPE_S1 = "SESSION";
	private static final String TYPE_S2 = "SESSION uppercase:true";
	private static final String TYPE_U1 = "UUID";
	private static final String TYPE_U2 = "UUID uppercase:true";
	private static final String TYPE_B1 = "BOOLEAN";
	private static final String TYPE_B2 = "BOOLEAN true:TRUE false:FALSE";
	private static final String TYPE_D1 = "DOUBLE";
	private static final String TYPE_D2 = "DOUBLE dot:false";
	private static final String TYPE_L = "LONG";

	private void testSame(String type, String string) {
		testSame(type, string, string, string);
	}

	private void testSame(String type, String expected, String string) {
		testSame(type, expected, expected, string);
	}

	private void testSame(String type, String expected1, String expected2, String string) {
		ValueFactory factory = new ValueFactory();
		Assert.assertEquals(string, factory.analyse(string));
		factory.load(type);
		Assert.assertEquals(expected1, factory.analyse(string));
		factory.reset();
		factory.loadDefaults();
		Assert.assertEquals(expected2, factory.analyse(string));
	}

	@Test
	public void testDouble() {
		testSame(TYPE_D1, "test double:$(DOUBLE)", "test double:0.0");
		testSame(TYPE_D1, "test double:$(DOUBLE)", "test double:10.0");
		testSame(TYPE_D1, "test double:$(DOUBLE)", "test double:-10.0");
		testSame(TYPE_D1, "test double:$(DOUBLE)", "test double:12345.123456");
		testSame(TYPE_D2, "test double:$(DOUBLE dot:false)", "test double:12345,123456");
		testSame(TYPE_D1, "test double:$(DOUBLE).123", "test double:$(DOUBLE).$(LONG)", "test double:123.123.123");
		testSame(TYPE_D2, "test double:$(DOUBLE dot:false),123", "test double:$(DOUBLE dot:false),$(LONG)",
			"test double:123,123,123");
	}

	@Test
	public void testLong() {
		testSame(TYPE_L, "test long:$(LONG)", "test long:0");
		testSame(TYPE_L, "test long:$(LONG)", "test long:10");
		testSame(TYPE_L, "test long:$(LONG)", "test long:-10");
		testSame(TYPE_L, "test long:$(LONG)$(LONG)$(LONG)", "test long:123-123-123");
	}

	@Test
	public void testBoolean() {
		testSame(TYPE_B1, "test boolean:$(BOOLEAN)", "test boolean:true");
		testSame(TYPE_B1, "test boolean:$(BOOLEAN)", "test boolean:false");
		testSame(TYPE_B2, "test boolean:$(BOOLEAN true:TRUE false:FALSE)", "test boolean:TRUE");
		testSame(TYPE_B2, "test boolean:$(BOOLEAN true:TRUE false:FALSE)", "test boolean:FALSE");

		testSame(TYPE_B1, "Xtrue ");
		testSame(TYPE_B1, "_true ");
		testSame(TYPE_B1, " Xtrue ");
		testSame(TYPE_B1, " _true ");
		testSame(TYPE_B1, "Xtrue");
		testSame(TYPE_B1, "_true");
		testSame(TYPE_B1, " Xtrue");
		testSame(TYPE_B1, " _true");

		testSame(TYPE_B1, "trueX ");
		testSame(TYPE_B1, "true_ ");
		testSame(TYPE_B1, " trueX ");
		testSame(TYPE_B1, " true_ ");
		testSame(TYPE_B1, "trueX");
		testSame(TYPE_B1, "true_");
		testSame(TYPE_B1, " trueX");
		testSame(TYPE_B1, " true_");

		testSame(TYPE_B1, "XtrueX ");
		testSame(TYPE_B1, "_true_ ");
		testSame(TYPE_B1, " XtrueX ");
		testSame(TYPE_B1, " _true_ ");
		testSame(TYPE_B1, "xtrueX");
		testSame(TYPE_B1, "_true_");
		testSame(TYPE_B1, " XtrueX");
		testSame(TYPE_B1, " _true_");

		testSame(TYPE_B1, "$(BOOLEAN)", "true");
		testSame(TYPE_B1, " $(BOOLEAN)", " true");
		testSame(TYPE_B1, "$(BOOLEAN) ", "true ");

		testSame(TYPE_B1, "$(BOOLEAN)", "false");
		testSame(TYPE_B1, " $(BOOLEAN)", " false");
		testSame(TYPE_B1, "$(BOOLEAN) ", "false ");
	}

	@Test
	public void testUuid() {
		UUID uuid = UUID.randomUUID();
		testSame(TYPE_U1, "test uuid:$(UUID)", "test uuid:" + uuid.toString());
		testSame(TYPE_U2, "test uuid:$(UUID uppercase:true)", "test uuid:" + uuid.toString().toUpperCase());
	}

	@Test
	public void testSession() {
		testSame(TYPE_S1, "$(SESSION)", "00000000000000000000000000000000");
		testSame(TYPE_S1, "$(SESSION)", "0123456789abcdef0123456789abcdef");
		testSame(TYPE_S2, "$(SESSION uppercase:true)", "0123456789ABCDEF0123456789ABCDEF");
		testSame(TYPE_S1, "0123456789abcdef0123456789ABCDEF");
	}

	@Test
	public void testSessionHost() {
		testSame(TYPE_H1, "$(SESSIONHOST)", "00000000000000000000000000000000.ip-1-2-3-4");
		testSame(TYPE_H1, "$(SESSIONHOST)", "0123456789abcdef0123456789abcdef.ip-192-168-0-1");
		testSame(TYPE_H2, "$(SESSIONHOST uppercase:true)", "0123456789ABCDEF0123456789ABCDEF.ip-192-168-0-1");
		testSame(TYPE_H1, "0123456789abcdef0123456789ABCDEF.ip-192-168-0-1",
			"0123456789abcdef0123456789ABCDEF.ip-$(LONG)$(LONG)$(LONG)$(LONG)",
			"0123456789abcdef0123456789ABCDEF.ip-192-168-0-1");
		testSame(TYPE_H1, "0123456789abcdef0123456789ABCDEF.ip.-192-168-0-1",
			"0123456789abcdef0123456789ABCDEF.ip.$(LONG)$(LONG)$(LONG)$(LONG)",
			"0123456789abcdef0123456789ABCDEF.ip.-192-168-0-1");
	}
}
