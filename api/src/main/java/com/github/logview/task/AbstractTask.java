package com.github.logview.task;

public abstract class AbstractTask implements Task {
	private boolean started = false;

	protected void setStarted() {
		started = true;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void actionStart() {
	}

	@Override
	public void actionFinished() {
	}

	@Override
	public int getMaxThreads() {
		return 0;
	}
}
