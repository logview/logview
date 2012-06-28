package com.github.logview.importer;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import com.github.logview.api.DetailLogEntry;
import com.github.logview.api.LogEntry;
import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Match;
import com.github.logview.task.AbstractTask;
import com.github.logview.util.Util;
import com.github.logview.value.api.Value;
import com.github.logview.value.api.ValueFactory;
import com.github.logview.value.api.ValueParams;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

public class TaggerTask extends AbstractTask {
	private final Reader<LogEntry> input;
	private final Writer<LogEntry> output;

	private final Properties manual;
	private final Properties auto;
	private final Properties global;
	private final ValueFactory factory = ValueFactory.createDefault();
	private final Tags tags = new Tags();

	private final LoadingCache<String, AnalyserMultiMatcher> clgCache = CacheBuilder.newBuilder().build(
		new CacheLoader<String, AnalyserMultiMatcher>() {
			@Override
			public AnalyserMultiMatcher load(String key) throws Exception {
				return (AnalyserMultiMatcher)new AnalyserMultiMatcher(factory, key, manual, auto)
						.load("global", global);
			}
		});

	public TaggerTask(Reader<LogEntry> input, Writer<LogEntry> output) {
		this.input = input;
		this.output = output;

		try {
			manual = Util.loadProps("manual.properties");
			auto = Util.loadProps("auto.properties");
			global = Util.loadProps("global.properties");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean actionDo() {
		while(true) {
			final Entry<Long, LogEntry> read = input.read();
			if(read == null) {
				return false;
			}
			final int id = (int)(long)read.getKey();
			final LogEntry entry = read.getValue();
			DetailLogEntry d = new DetailLogEntry(entry);
			tags.tag("class", d.getClassName(), id);
			tags.tag("level", d.getLevel(), id);
			for(String ndc : d.getNDC().split(" ")) {
				ndc = ndc.trim();
				if(ndc.length() != 0) {
					tags.tag("ndc", ndc, id);
				}
			}
			final Match line = entry.getLine();
			final String level = (String)line.getValue(1);
			final String cls = (String)line.getValue(3);
			final String text = (String)line.getValue(4);
			final String key = cls + level.substring(0, 1);

			AnalyserMultiMatcher clg = clgCache.getUnchecked(key);
			Match tm = clg.match(text);

			if(tm != null) {
				Table<String, String, Object> table = HashBasedTable.create();
				for(Entry<Value, Object> v : tm.getValueMap().entrySet()) {
					Value k = v.getKey();
					String to = k.getParamAsStringOrNull(ValueParams.TO);
					if(to != null) {
						table.put(to, k.getParamAsString(ValueParams.NAME), v.getValue());
					}
				}
				for(Entry<Value, Object> v : tm.getValueMap().entrySet()) {
					for(String tag : v.getKey().getParamAsList(ValueParams.TAGS)) {
						tags.tag(tag, (String)v.getValue(), id);
						// Map<String, Object> map = table.row(tag);
						// to, k.getParamAsString(ValueParams.NAME), v.getValue()
						// map.putAll(line.getNameMap());
						// map.put("entry", entry);
						// tags.add(tag, (String)v.getValue(), map);
					}
				}
			}

			output.write(read);
		}
	}

	@Override
	public void actionFinished() {
		super.actionFinished();
		StringBuilder sb = new StringBuilder();
		TreeMap<String, AnalyserMultiMatcher> sort = Maps.newTreeMap();
		sort.putAll(clgCache.asMap());
		for(AnalyserMultiMatcher m : sort.values()) {
			m.store(sb);
		}
		System.err.println("\n\n\n");
		System.err.println(sb.toString());
	}

	@Override
	public String getName() {
		return "tagger";
	}
}
