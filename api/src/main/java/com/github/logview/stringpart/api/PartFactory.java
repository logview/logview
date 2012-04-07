package com.github.logview.stringpart.api;

import java.util.Map;

import com.github.logview.api.LogFile;
import com.github.logview.api.PartManager;
import com.github.logview.stringpart.type.BooleanPart;
import com.github.logview.stringpart.type.BytePart;
import com.github.logview.stringpart.type.DatePart;
import com.github.logview.stringpart.type.DoublePart;
import com.github.logview.stringpart.type.EmptyPart;
import com.github.logview.stringpart.type.EnumPart;
import com.github.logview.stringpart.type.IntegerPart;
import com.github.logview.stringpart.type.ListPart;
import com.github.logview.stringpart.type.LongPart;
import com.github.logview.stringpart.type.RegexPart;
import com.github.logview.stringpart.type.ShortPart;
import com.github.logview.stringpart.type.StringPart;
import com.github.logview.stringpart.type.TagListPart;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;

public class PartFactory {
	private final Map<PartType, PartCreator> instances = Maps.newHashMap();
	private final PartManager manager;

	public PartFactory(LogFile logfile, PartManager manager) {
		this(manager);
	}

	public PartFactory(PartManager m) {
		this.manager = m;
		register(PartType.EMPTY, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new EmptyPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new EmptyPart(data, manager);
			}
		});
		register(PartType.ENUM, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new EnumPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new EnumPart(data, manager);
			}
		});
		register(PartType.LIST, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new ListPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new ListPart(data, manager);
			}
		});
		register(PartType.STRING, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new StringPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new StringPart(data, manager);
			}
		});
		register(PartType.REGEX, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new RegexPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new RegexPart(data, manager);
			}
		});
		register(PartType.BOOLEAN, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new BooleanPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new BooleanPart(data, manager);
			}
		});
		register(PartType.BYTE, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new BytePart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new BytePart(data, manager);
			}
		});
		register(PartType.SHORT, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new ShortPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new ShortPart(data, manager);
			}
		});
		register(PartType.INT, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new IntegerPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new IntegerPart(data, manager);
			}
		});
		register(PartType.LONG, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new LongPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new LongPart(data, manager);
			}
		});
		register(PartType.DOUBLE, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new DoublePart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new DoublePart(data, manager);
			}
		});
		register(PartType.DATE, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new DatePart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new DatePart(data, manager);
			}
		});
		register(PartType.TAGLIST, new PartCreator() {
			@Override
			public Part createInstance(ByteArrayDataInput data) {
				return new TagListPart(data, manager);
			}

			@Override
			public Part createInstance(String data) {
				return new TagListPart(data, manager);
			}
		});
	}

	public Part createFrom(ByteArrayDataInput data) {
		return instances.get(PartType.values()[data.readByte()]).createInstance(data);
	}

	public void register(PartType type, PartCreator creator) {
		instances.put(type, creator);
	}

	public PartCreator getCreator(PartType type) {
		return instances.get(type);
	}
}
