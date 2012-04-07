package com.github.logview.regex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.logview.api.PartManager;
import com.github.logview.stringpart.api.Part;
import com.github.logview.stringpart.api.PartCreator;
import com.github.logview.stringpart.api.PartFactory;
import com.github.logview.stringpart.api.PartType;
import com.github.logview.util.Util;
import com.google.common.base.Strings;

public class Config {
	private final String[] names;
	private final PartType[] types;
	private final String[] stringFormats;
	private final SimpleDateFormat[] formats;
	private final String regex;
	private final String print;
	private final Pattern pattern;
	private final PartCreator[] partCreator;
	private final String[][] booleanStrings;
	private final String[] enumList;
	private final String[][] enums;
	private final String[] filler;
	private final static Pattern splitter = Pattern.compile("[\\(\\)]");
	private final PartCreator dateCreator;
	private final PartCreator booleanCreator;
	private final PartManager manager;

	public Config(String config, PartManager manager) {
		this.manager = manager;
		PartFactory factory = manager.getFactory();
		dateCreator = factory.getCreator(PartType.DATE);
		booleanCreator = factory.getCreator(PartType.BOOLEAN);

		String[] configs = config.split("\\|", 2);
		int count = Integer.parseInt(configs[0]);
		configs = configs[1].split("\\|", count + 2);
		if(configs.length != count + 2) {
			throw new IllegalArgumentException("wrong config syntax!");
		}
		names = new String[count];
		types = new PartType[count];
		formats = new SimpleDateFormat[count];
		stringFormats = new String[count];
		partCreator = new PartCreator[count];
		booleanStrings = new String[count][];
		enumList = new String[count];
		enums = new String[count][];
		for(int i = 0; i < count; i++) {
			String[] c = configs[i].split(" ");
			for(int o = 0; o < c.length; o++) {
				String[] cfg = c[o].split(":", 2);
				if(cfg.length != 2) {
					throw new IllegalArgumentException("wrong parameter syntax");
				} else if("name".equals(cfg[0])) {
					names[i] = cfg[1];
				} else if("type".equals(cfg[0])) {
					types[i] = PartType.valueOf(cfg[1].toUpperCase());
					partCreator[i] = factory.getCreator(types[i]);
				} else if("format".equals(cfg[0])) {
					stringFormats[i] = cfg[1];
					formats[i] = new SimpleDateFormat(Util.space(cfg[1]));
				} else if("values".equals(cfg[0])) {
					enumList[i] = cfg[1];
					enums[i] = cfg[1].split(";");
				} else if("false".equals(cfg[0])) {
					if(booleanStrings[i] == null) {
						booleanStrings[i] = new String[2];
					}
					booleanStrings[i][0] = cfg[1];
				} else if("true".equals(cfg[0])) {
					if(booleanStrings[i] == null) {
						booleanStrings[i] = new String[2];
					}
					booleanStrings[i][1] = cfg[1];
				} else {
					throw new IllegalArgumentException("wrong parameter type " + c[0]);
				}
			}
		}
		for(int i = 0; i < count; i++) {
			if(types[i] == null) {
				throw new IllegalArgumentException("missing type!");
			} else if(types[i] == PartType.DATE && formats[i] == null) {
				throw new IllegalArgumentException("missing date format!");
			} else if(types[i] != PartType.DATE && formats[i] != null) {
				throw new IllegalArgumentException("date format for none date!");
			}
		}
		if(Strings.isNullOrEmpty(configs[count])) {
			print = null;
		} else {
			print = configs[count];
		}
		regex = configs[count + 1];
		pattern = Pattern.compile(regex);
		String[] parts = splitter.split(regex);
		filler = new String[(parts.length + 1) / 2];
		for(int i = 0; i < parts.length; i++) {
			if(i % 2 == 0) {
				filler[i / 2] = parts[i];
			}
		}
	}

	public Matcher match(String line) {
		return pattern.matcher(line);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(names.length);
		for(int i = 0; i < names.length; i++) {
			sb.append('|');
			if(names[i] != null) {
				sb.append("name:");
				sb.append(names[i]);
				sb.append(' ');
			}
			sb.append("type:");
			sb.append(types[i].toString());
			if(types[i] == PartType.DATE) {
				sb.append(" format:");
				sb.append(stringFormats[i]);
			} else if(types[i] == PartType.BOOLEAN) {
				sb.append(" true:");
				sb.append(booleanStrings[i][1]);
				sb.append(" false:");
				sb.append(booleanStrings[i][0]);
			} else if(types[i] == PartType.ENUM) {
				sb.append(" values:");
				sb.append(enumList[i]);
			}
		}
		sb.append('|');
		sb.append(regex);
		return sb.toString();
	}

	public static Config load(String prefix, Properties props, PartManager manager) {
		String regex = props.getProperty(prefix);

		int max = 0;
		while(true) {
			if(props.getProperty(prefix + "." + max + ".type") == null) {
				break;
			}
			max++;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(max);
		sb.append('|');
		for(int i = 0; i < max; i++) {
			String p = prefix + "." + i;
			String t = props.getProperty(p + ".type");
			if(t == null) {
				throw new IllegalArgumentException("missing type " + p + ".type");
			}
			PartType type = PartType.valueOf(t.toUpperCase());
			sb.append("type:");
			sb.append(type.toString());
			String n = props.getProperty(p);
			if(n != null) {
				sb.append(" name:");
				sb.append(n);
			}
			if(type == PartType.DATE) {
				String format = props.getProperty(p + ".format");
				if(format == null) {
					throw new RuntimeException("missing format " + p + ".format");
				}
				sb.append(" format:");
				sb.append(format);
			} else if(type == PartType.BOOLEAN) {
				sb.append(" true:");
				sb.append(props.getProperty(p + ".true"));
				sb.append(" false:");
				sb.append(props.getProperty(p + ".false"));
			} else if(type == PartType.ENUM) {
				sb.append(" values:");
				sb.append(props.getProperty(p + ".values"));
			}
			sb.append('|');
		}
		String print = props.getProperty(prefix + ".print");
		if(print != null) {
			sb.append(print);
		}
		sb.append('|');
		sb.append(regex);

		return create(sb.toString(), manager);
	}

	public static Config create(String config, PartManager manager) {
		return new Config(config, manager);
	}

	public int size() {
		return names.length;
	}

	public String getFiller(int id) {
		if(id < 0 || id >= filler.length) {
			return "";
		}
		return filler[id];
	}

	public Part createPart(int id, String data) {
		if(id < 0 || id >= names.length) {
			throw new IllegalArgumentException("id is out of range");
		}
		if(partCreator[id] == null) {
			throw new IllegalArgumentException("no part factory for id " + id + " found!");
		}
		if(formats[id] != null) {
			try {
				Date date = formats[id].parse(data);
				return dateCreator.createInstance(Long.toString(date.getTime()));
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		if(types[id] == PartType.BOOLEAN) {
			if(booleanStrings[id][0].equals(data)) {
				return booleanCreator.createInstance("false");
			} else if(booleanStrings[id][1].equals(data)) {
				return booleanCreator.createInstance("true");
			} else {
				throw new RuntimeException(String.format("Illegal value %s: allowed is %s|%s!", data,
					booleanStrings[id][0], booleanStrings[id][1]));
			}
		}
		if(types[id] == PartType.ENUM) {
			for(int i = 0; i < enums[id].length; i++) {
				if(enums[id][i].equals(data)) {
					return partCreator[id].createInstance(Integer.toString(i));
				}
			}
			throw new RuntimeException(String.format("Illegal value %s: allowed is %s!", data, enumList[id]));
		}
		return partCreator[id].createInstance(data);
	}

	public String getFormat() {
		return print;
	}

	public int getIdByName(String name) {
		for(int i = 0; i < names.length; i++) {
			if(name.equals(names[i])) {
				return i;
			}
		}
		throw new IllegalArgumentException("Name '" + name + "' not found!");
	}

	public Object format(int id, Part data) {
		// TODO other types!
		switch(types[id]) {
			case DATE: {
				return formats[id].format((Date)data.value());
			}
			case ENUM: {
				return enums[id][(Byte)data.value()];
			}
			default: {
				return data.value();
			}
		}
	}

	public PartManager getManager() {
		return manager;
	}
}
