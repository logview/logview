package com.github.logview.importer;

import com.github.logview.api.Loader;
import com.github.logview.task.TaskListener;
import com.github.logview.task.TaskManager;
import com.github.logview.task.TaskMonitor;
import com.github.logview.task.TaskMonitorWrapper;

public abstract class AbstractLoader extends TaskMonitorWrapper implements Loader, TaskListener, TaskMonitor {
	protected final TaskManager manager = (TaskManager)wrapped;

	public AbstractLoader() {
		this(new TaskManager());
	}

	public AbstractLoader(TaskManager manager) {
		super(manager);
		manager.addListener(this);
	}

	@Override
	public void load() {
	}

	@Override
	public void flush() {
		manager.flush();
	}

	@Override
	public void close() {
		manager.close();
		manager.removeListener(this);
	}

	@Override
	public void notifyAborted() {
	}
}
