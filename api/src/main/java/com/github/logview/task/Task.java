package com.github.logview.task;

public interface Task {
	boolean isStarted();

	boolean actionDo();

	void actionStart();

	void actionFinished();

	int getMaxThreads();

	String getName();
}
