package com.github.logview.value.type;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.github.logview.params.Params;
import com.github.logview.util.DateUtil;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class DateValue extends AbstractRegexValue {
	private final static ValueType type = ValueType.DATE;

	private static String replaceDigit2(String d, String ret) {
		return ret.replaceAll(d + d, "\\\\d{2}").replaceAll("[^\\\\]" + d, "\\\\d{1,2}");
	}

	private static String replaceDigit3(String d, String ret) {
		return ret.replaceAll(d + d + d, "\\\\d{3}").replaceAll(d + d, "\\\\d{2,3}").replaceAll(d, "\\\\d{1,3}");
	}

	public static String createDateRegex(String format) {
		String ret = format;
		ret = ret.replaceAll("\\.", "\\\\.");
		ret = ret.replaceAll("\\:", "\\\\:");
		ret = ret.replaceAll("\\(", "\\\\(");
		ret = ret.replaceAll("\\)", "\\\\)");
		ret = ret.replaceAll("z", "\\\\w+");
		ret = ret.replaceAll("y", "\\\\d");
		ret = ret.replaceAll("MMMM", "\\\\w+");
		ret = ret.replaceAll("MMM", "\\\\w{3}");
		ret = ret.replaceAll("EEEE", "\\\\w+");
		ret = ret.replaceAll("EEE", "\\\\w{3}");
		ret = replaceDigit2("M", ret);
		ret = replaceDigit2("d", ret);
		ret = replaceDigit2("h", ret);
		ret = replaceDigit2("H", ret);
		ret = replaceDigit2("m", ret);
		ret = replaceDigit2("s", ret);
		ret = replaceDigit3("S", ret);
		return ret;
	}

	private final DateTimeFormatter format;

	public DateValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private DateValue(Params params) {
		super(createDateRegex(params.getParamAsString(ValueParams.FORMAT)), params);
		format = DateUtil.withZone(DateTimeFormat.forPattern(params.getParamAsString(ValueParams.FORMAT)),
			params.getParamAsStringOrNull(ValueParams.ZONE)).withLocale(
			getLocale(params.getParamAsString(ValueParams.LOCALE)));
	}

	private Locale getLocale(String language) {
		return new Locale(language);
	}

	@Override
	public ValueType getType() {
		return type;
	}

	@Override
	public Object valueOf(String string) {
		String ret = (String)super.valueOf(string);
		if(ret == null) {
			return ret;
		}
		return new Date(DateUtil.parse(format, ret));
	}

	@Override
	public String format(Object data) {
		if(data instanceof Date) {
			return format.print(((Date)data).getTime());
		}
		throw new IllegalArgumentException();
	}
}
