package com.github.logview.params;

import com.github.logview.util.Util;
import com.github.logview.value.api.ValueParams;

public abstract class AbstractParams implements Params {
	@Override
	public boolean getParamAsBoolean(ValueParams key) {
		String ret = getParamAsString(key);
		if("true".equals(ret)) {
			return true;
		} else if("false".equals(ret)) {
			return false;
		}
		throw new RuntimeException("key '" + key + "'='" + ret + "' is not of type Boolean");
	}

	@Override
	public Integer getParamAsInt(ValueParams key) {
		String ret = getParamAsStringOrNull(key);
		if(ret == null) {
			return null;
		}
		return Integer.parseInt(ret);
	}

	@Override
	public String getParamAsString(ValueParams key) {
		String ret = getParamAsStringOrNull(key);
		if(ret == null) {
			throw new RuntimeException("key '" + key + "' not found!");
		}
		return ret;
	}

	@Override
	public String getParamsAsString() {
		StringBuilder sb = new StringBuilder();
		for(ValueParams key : getParams()) {
			sb.append(' ');
			sb.append(key);
			sb.append(':');
			sb.append(Util.escapeSpace(getParamAsString(key)));
		}
		return sb.toString();
	}
}
