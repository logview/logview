package com.github.logview.task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRunner implements Runnable {
	private final String name;
	private final AtomicBoolean close;
	private final AtomicBoolean flush;
	private final AtomicInteger count;
	private final Task task;
	private final TaskManager manager;

	public TaskRunner(TaskManager manager, int id, AtomicBoolean close, AtomicInteger count, Task task) {
		this.manager = manager;
		this.close = close;
		this.count = count;
		this.task = task;
		this.name = task.getName() + " " + id;
		this.flush = new AtomicBoolean();
		count.incrementAndGet();
	}

	@Override
	public void run() {
		Thread thread = Thread.currentThread();
		final String tname = thread.getName();
		thread.setName(name);
		try {
			final long start = System.currentTimeMillis();
			System.err.println("starting task " + name);
			task.actionStart();
			while(true) {
				if(task.actionDo()) {
					continue;
				}
				if(flush.getAndSet(false)) {
					System.err.printf("flushed task %s!\n", name);
				}
				if(close.get()) {
					task.actionFinished();
					System.err.printf("finished task %s: %d ms\n", name, System.currentTimeMillis() - start);
					return;
				}
				Thread.sleep(10);
			}
		} catch (Throwable t) {
			System.err.println("aborted task " + name + " " + t.getMessage());
			manager.abort();
			throw new RuntimeException(t);
		} finally {
			count.decrementAndGet();
			synchronized(count) {
				count.notifyAll();
			}
			thread.setName(tname);
		}
	}

	public void flush() {
		System.err.printf("flushing task %s!\n", name);
		flush.set(true);
		while(flush.get()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		System.err.printf("flushing task %s done!\n", name);
	}
}
