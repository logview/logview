package com.github.logview.task;

public interface TaskMonitor {
	void addListener(TaskListener listener);

	void removeListener(TaskListener listener);

	void fireNotifyAborted();
}
