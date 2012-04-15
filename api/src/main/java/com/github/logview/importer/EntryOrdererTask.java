package com.github.logview.importer;

import com.github.logview.io.Reader;
import com.github.logview.io.Writer;
import com.github.logview.matcher.Match;
import com.github.logview.task.Task;

public class EntryOrdererTask extends Task {
	private final Reader<Match> matchesIn;
	private final Reader<String> extraIn;
	private final Writer<Match> matchesOut;
	private final Writer<String> extraOut;
	private long min;

	public EntryOrdererTask(Reader<Match> matchesIn, Reader<String> extraIn, Writer<Match> matchesOut,
			Writer<String> extraOut) {
		this.matchesIn = matchesIn;
		this.extraIn = extraIn;
		this.matchesOut = matchesOut;
		this.extraOut = extraOut;
	}

	@Override
	protected boolean actionDo() {
		while(true) {
			Long a = matchesIn.firstKey();
			if(a != null) {
				if(a == min) {
					matchesOut.write(matchesIn.read());
					min++;
					continue;
				}
			}
			Long b = extraIn.firstKey();
			if(b != null) {
				if(b == min) {
					extraOut.write(extraIn.read());
					min++;
					continue;
				}
			}
			return false;
		}
	}

	@Override
	public int getMaxThreads() {
		return 1;
	}

	@Override
	public String getName() {
		return "entry orderer";
	}
}
