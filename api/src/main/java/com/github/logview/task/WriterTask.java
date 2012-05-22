package com.github.logview.task;

import com.github.logview.io.Writer;

public interface WriterTask<W> {
	Writer<W> getWriter();
}
