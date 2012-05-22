package com.github.logview.task;

public interface PipelineTask<R, W> extends ReaderTask<R>, WriterTask<W> {
}
