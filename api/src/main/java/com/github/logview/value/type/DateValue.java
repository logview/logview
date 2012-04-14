package com.github.logview.value.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.github.logview.params.Params;
import com.github.logview.value.api.ValueParams;
import com.github.logview.value.api.ValueType;
import com.github.logview.value.base.AbstractRegexValue;

public class DateValue extends AbstractRegexValue {
	private final static ValueType type = ValueType.DATE;

	private static String replaceDigit2(String d, String ret) {
		return ret.replaceAll(d + d, "\\\\d{2}").replaceAll(d, "\\\\d{1,2}");
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
		ret = ret.replaceAll("y", "\\\\d");
		ret = ret.replaceAll("MMMM", "\\\\w+");
		ret = ret.replaceAll("MMM", "\\\\w{3}");
		ret = replaceDigit2("M", ret);
		ret = replaceDigit2("d", ret);
		ret = replaceDigit2("h", ret);
		ret = replaceDigit2("H", ret);
		ret = replaceDigit2("m", ret);
		ret = replaceDigit2("s", ret);
		ret = replaceDigit3("S", ret);
		return ret;
	}

	private final SimpleDateFormat format;

	public DateValue(Map<ValueParams, String> data) {
		this(type.create(data));
	}

	private DateValue(Params params) {
		super(createDateRegex(params.getParamAsString(ValueParams.FORMAT)), params);
		format = new SimpleDateFormat(params.getParamAsString(ValueParams.FORMAT));
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
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
		try {
			return format.parseObject(ret);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String format(Object data) {
		if(data instanceof Date) {
			return format.format((Date)data);
		}
		throw new IllegalArgumentException();
	}
}
