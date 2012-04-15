package com.github.logview.task;

public abstract class Task {

	abstract protected boolean actionDo();

	protected void actionStart() {
	}

	protected void actionFinished() {
	}

	public int getMaxThreads() {
		return 0;
	}

	abstract public String getName();
}
