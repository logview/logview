package com.github.logview.task;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;

public class TaskManager extends AbstractTaskMonitor {
	private final ExecutorService es;
	private final LinkedList<TaskPartManager> tasks = Lists.newLinkedList();

	public TaskManager() {
		this.es = Executors.newCachedThreadPool();
	}

	public void add(Task task, int count) {
		tasks.add(new TaskPartManager(task, this, es, count));
	}

	public void close() {
		try {
			closeIE();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeIE() throws InterruptedException {
		System.err.println("closing TaskManager");
		for(TaskPartManager manager : tasks) {
			manager.close();
		}
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		System.err.println("closed TaskManager");
		/*
		AtomicBoolean closed = this.closed.get(i);
		AtomicInteger running = this.running.get(i);
		Task task = this.tasks.get(i);
		String name = task.getName();
		System.err.printf("closing task '%s'...\n", name);
		while(!started.isEmpty()) {
			for(TaskRunner tr : started.get(task)) {
			}
			closed.set(true);
			while(true) {
				synchronized(running) {
					int r = running.get();
					if(r == 0) {
						break;
					}
					System.err.printf("waiting for %d '%s' tasks to close...\n", r, name);
					running.wait();
				}
			}
			System.err.printf("closed task '%s'...\n", name);
		}
		*/
	}

	public String getStatus() {
		/*
		if(closed.size() == 0) {
			return "no tasks!";
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < closed.size(); i++) {
			sb.append(tasks.get(i).getName());
			sb.append(" running:");
			sb.append(running.get(i).get());
			sb.append(" closed:");
			sb.append(closed.get(i).get());
			sb.append('\n');
		}
		return sb.toString();
		*/
		return "TODO";
	}

	public void abort() {
		/*
		try {
			es.shutdown();
			for(int i = 0; i < closed.size(); i++) {
				this.closed.get(i).set(true);
			}
			fireNotifyAborted();
		} finally {
			System.err.println("closed TaskManager");
		}
		*/
	}

	public void flush() {
		for(TaskPartManager manager : tasks) {
			manager.flush();
		}
	}
}
