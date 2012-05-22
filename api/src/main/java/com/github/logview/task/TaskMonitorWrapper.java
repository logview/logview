package com.github.logview.task;

public class TaskMonitorWrapper implements TaskMonitor {
	protected final TaskMonitor wrapped;

	public TaskMonitorWrapper(TaskMonitor wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void addListener(TaskListener listener) {
		wrapped.addListener(listener);
	}

	@Override
	public void removeListener(TaskListener listener) {
		wrapped.removeListener(listener);
	}

	@Override
	public void fireNotifyAborted() {
		wrapped.fireNotifyAborted();
	}
}
