package com.github.logview.task;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

public class TaskManager {
	private final ExecutorService es;
	private final ArrayList<AtomicBoolean> closed = Lists.newArrayList();
	private final ArrayList<AtomicInteger> running = Lists.newArrayList();
	private final ArrayList<Task> tasks = Lists.newArrayList();

	public TaskManager() {
		this.es = Executors.newCachedThreadPool();
	}

	public void add(Task task, int count) {
		AtomicBoolean closed = new AtomicBoolean();
		AtomicInteger running = new AtomicInteger();
		for(int i = 0; i < count; i++) {
			es.execute(new TaskRunner(i + 1, closed, running, task));
		}
		this.closed.add(closed);
		this.running.add(running);
		this.tasks.add(task);
	}

	public void close() throws InterruptedException {
		System.err.println("closing TaskManager");
		for(int i = 0; i < closed.size(); i++) {
			AtomicBoolean closed = this.closed.get(i);
			AtomicInteger running = this.running.get(i);
			String name = this.tasks.get(i).getName();
			System.err.printf("closing task '%s'...\n", name);
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

		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		System.err.println("closed TaskManager");
	}

	public String getStatus() {
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
	}
}
