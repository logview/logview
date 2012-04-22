package com.github.logview.util;

import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.ImmutableMap;

public class DateUtil {
	public final static DateTimeZone UTC = DateTimeZone.UTC;
	public final static DateTimeZone CET = DateTimeZone.forID("Europe/Prague");
	public final static DateTimeZone CEST = DateTimeZone.forID("Europe/Prague");

	public final static Map<String, DateTimeZone> zones = new ImmutableMap.Builder<String, DateTimeZone>()
			.put("UTC", UTC) //
			.put("CET", CET) //
			.put("CEST", CEST) //
			.build();

	public static long parse(DateTimeFormatter format, String date) {
		for(Entry<String, DateTimeZone> e : zones.entrySet()) {
			String zone = e.getKey();
			if(date.contains(zone)) {
				return format.withZone(e.getValue()).parseMillis(date.replace(zone, ""));
			}
		}
		return format.withZoneUTC().parseMillis(date);
	}

	public static DateTimeFormatter withZone(DateTimeFormatter format, String zone) {
		if(zone == null) {
			return format;
		}

		DateTimeZone ret = zones.get(zone);
		if(ret == null) {
			throw new IllegalArgumentException("DateTimeZone '" + zone + "' not supported!");
		}
		return format.withZone(ret);
	}
}
