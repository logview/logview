package com.github.logview.task;

import java.util.Set;

import com.google.common.collect.Sets;

public class AbstractTaskMonitor implements TaskMonitor {
	private final Set<TaskListener> listeners = Sets.newLinkedHashSet();

	@Override
	public void addListener(TaskListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(TaskListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void fireNotifyAborted() {
		for(TaskListener listener : listeners) {
			listener.notifyAborted();
		}
	}
}
