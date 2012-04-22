package com.github.logview.value.api;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

import com.github.logview.util.DateUtil;

public class DateUtilTest {

	private void test(final String date, DateTimeZone zone, final long stamp) {
		final DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss z yyyy")
				.withLocale(java.util.Locale.ENGLISH).withZone(zone);
		Assert.assertEquals(date, format.print(stamp));
		Assert.assertEquals(stamp, DateUtil.parse(format, date));
	}

	@Test
	public void testUTC() {
		test("Fri Feb 13 23:31:30 UTC 2009", DateUtil.UTC, 1234567890000L);
	}

	@Test
	public void testCST() {
		test("Sat Feb 14 00:31:30 CET 2009", DateUtil.CET, 1234567890000L);
	}

	@Test
	public void testCEST() {
		test("Sun Jun 21 11:51:30 CEST 2009", DateUtil.CEST, 1245577890000L);
	}
}
