package com.github.logview.importer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.tools.bzip2.CBZip2OutputStream;

import com.github.logview.api.LogEntry;
import com.github.logview.task.AbstractReaderTask;

public class LogEntryTask extends AbstractReaderTask<List<LogEntry>> {
	private final AtomicLong raw = new AtomicLong();
	private final AtomicLong bz2 = new AtomicLong();
	private final AtomicLong lines = new AtomicLong();

	@Override
	public boolean actionDo() {
		while(true) {
			final Entry<Long, List<LogEntry>> m = getReader().read();
			if(m == null) {
				return false;
			}
			List<LogEntry> entrys = m.getValue();

			try {
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				CBZip2OutputStream os = new CBZip2OutputStream(bao);
				PrintWriter out = new PrintWriter(os);
				long s = 0;
				int l = 0;
				for(LogEntry entry : entrys) {
					String source = entry.getLine().getSource();
					out.println(source);
					s += source.getBytes().length;
					l++;
					for(String line : entry.getLines()) {
						l++;
						out.println(line);
						s += line.getBytes().length;
					}
				}
				out.close();
				os.close();
				int b = bao.toByteArray().length;
				raw.addAndGet(s);
				bz2.addAndGet(b);
				lines.addAndGet(l);
				// System.err.printf("%d/%d bytes %d lines\n", s, b, lines);
				System.err.printf("Total: %d/%d bytes %d lines\n", raw.get(), bz2.get(), lines.get());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void actionFinished() {
		System.err.printf("Total: %d/%d bytes %d lines\n", raw.get(), bz2.get(), lines.get());
	}

	@Override
	public String getName() {
		return "log entry";
	}
}
