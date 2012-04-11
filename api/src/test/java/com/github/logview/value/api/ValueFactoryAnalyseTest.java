package com.github.logview.value.api;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class ValueFactoryAnalyseTest {
	private final ValueFactory subject = ValueFactory.getInstance();

	private void testSame(String string) {
		Assert.assertEquals(string, subject.analyse(string));
	}

	private void testSame(String expected, String string) {
		Assert.assertEquals(expected, subject.analyse(string));
	}

	@Test
	public void testDouble() {
		testSame("test double:$(DOUBLE)", "test double:0.0");
		testSame("test double:$(DOUBLE)", "test double:10.0");
		testSame("test double:$(DOUBLE)", "test double:-10.0");
		testSame("test double:$(DOUBLE)", "test double:12345.123456");
		testSame("test double:$(DOUBLE dot:false)", "test double:12345,123456");
		testSame("test double:$(DOUBLE).$(LONG)", "test double:123.123.123");
		testSame("test double:$(DOUBLE dot:false),$(LONG)", "test double:123,123,123");
	}

	@Test
	public void testLong() {
		testSame("test long:$(LONG)", "test long:0");
		testSame("test long:$(LONG)", "test long:10");
		testSame("test long:$(LONG)", "test long:-10");
		testSame("test long:$(LONG)$(LONG)$(LONG)", "test long:123-123-123");
	}

	@Test
	public void testBoolean() {
		testSame("test boolean:$(BOOLEAN)", "test boolean:true");
		testSame("test boolean:$(BOOLEAN)", "test boolean:false");
		testSame("test boolean:$(BOOLEAN true:TRUE false:FALSE)", "test boolean:TRUE");
		testSame("test boolean:$(BOOLEAN true:TRUE false:FALSE)", "test boolean:FALSE");

		testSame("Xtrue ");
		testSame("_true ");
		testSame(" Xtrue ");
		testSame(" _true ");
		testSame("Xtrue");
		testSame("_true");
		testSame(" Xtrue");
		testSame(" _true");

		testSame("trueX ");
		testSame("true_ ");
		testSame(" trueX ");
		testSame(" true_ ");
		testSame("trueX");
		testSame("true_");
		testSame(" trueX");
		testSame(" true_");

		testSame("XtrueX ");
		testSame("_true_ ");
		testSame(" XtrueX ");
		testSame(" _true_ ");
		testSame("xtrueX");
		testSame("_true_");
		testSame(" XtrueX");
		testSame(" _true_");

		testSame("$(BOOLEAN)", "true");
		testSame(" $(BOOLEAN)", " true");
		testSame("$(BOOLEAN) ", "true ");

		testSame("$(BOOLEAN)", "false");
		testSame(" $(BOOLEAN)", " false");
		testSame("$(BOOLEAN) ", "false ");
	}

	@Test
	public void testUuid() {
		UUID uuid = UUID.randomUUID();
		testSame("test uuid:$(UUID)", "test uuid:" + uuid.toString());
		testSame("test uuid:$(UUID uppercase:true)", "test uuid:" + uuid.toString().toUpperCase());
	}

	@Test
	public void testSession() {
		testSame("$(SESSION)", "00000000000000000000000000000000");
		testSame("$(SESSION)", "0123456789abcdef0123456789abcdef");
		testSame("$(SESSION uppercase:true)", "0123456789ABCDEF0123456789ABCDEF");
		testSame("0123456789abcdef0123456789ABCDEF");
	}

	@Test
	public void testSessionHost() {
		testSame("$(SESSIONHOST)", "00000000000000000000000000000000.ip-1-2-3-4");
		testSame("$(SESSIONHOST)", "0123456789abcdef0123456789abcdef.ip-192-168-0-1");
		testSame("$(SESSIONHOST uppercase:true)", "0123456789ABCDEF0123456789ABCDEF.ip-192-168-0-1");
		testSame("0123456789abcdef0123456789ABCDEF.ip-$(LONG)$(LONG)$(LONG)$(LONG)",
			"0123456789abcdef0123456789ABCDEF.ip-192-168-0-1");
		testSame("0123456789abcdef0123456789ABCDEF.ip.$(LONG)$(LONG)$(LONG)$(LONG)",
			"0123456789abcdef0123456789ABCDEF.ip.-192-168-0-1");
	}
}
