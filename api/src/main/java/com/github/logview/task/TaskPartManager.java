package com.github.logview.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPartManager {
	private final AtomicBoolean closed = new AtomicBoolean();
	private final AtomicInteger running = new AtomicInteger();
	private final Task task;

	public TaskPartManager(Task task, TaskManager manager, ExecutorService es, int count) {
		this.task = task;

		for(int i = 0; i < count; i++) {
			es.execute(new TaskRunner(manager, i + 1, closed, running, task));
		}
	}

	public void close() throws InterruptedException {
		final String name = task.getName();
		System.err.printf("closing task '%s'...\n", name);
		closed.set(true);

		synchronized(running) {
			while(true) {
				int count = running.get();
				if(count < 1) {
					break;
				}
				System.err.printf("waiting for %d '%s' tasks to close...\n", count, name);
				running.wait();
			}
		}
		System.err.printf("closed task '%s'...\n", name);
	}
}
