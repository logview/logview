package com.github.logview.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

public class TaskPartManager {
	private final AtomicBoolean closed = new AtomicBoolean();
	private final AtomicInteger running = new AtomicInteger();
	private final Task task;
	private final List<TaskRunner> runner = Lists.newLinkedList();

	public TaskPartManager(Task task, TaskManager manager, ExecutorService es, int count) {
		this.task = task;

		for(int i = 0; i < count; i++) {
			TaskRunner tr = new TaskRunner(manager, i + 1, closed, running, task);
			es.execute(tr);
			runner.add(tr);
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

	public void flush() {
		for(TaskRunner tr : runner) {
			tr.flush();
		}
	}
}
