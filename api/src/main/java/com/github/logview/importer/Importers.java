package com.github.logview.importer;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

import com.github.logview.api.Loader;
import com.github.logview.task.TaskListener;
import com.github.logview.task.TaskMonitor;
import com.github.logview.util.AutomaticScanner;
import com.github.logview.util.Util;

public class Importers {
	public static void loadPath(Loader loader, TaskMonitor monitor) throws Exception {
		loadPath(loader, monitor, Preferences.userRoot().get("logpath", "."));
	}

	public static void loadPath(Loader loader, TaskMonitor monitor, String path) throws Exception {
		File dir = new File(path);
		for(File file : dir.listFiles()) {
			if(!file.isFile()) {
				continue;
			}
			loader.load();
			loadLog(loader, monitor, file);
			loader.close();
		}
	}

	private static int TO = 16;
	private static int lines = 1024;

	public static void loadLog(Loader loader, TaskMonitor monitor, File file) throws Exception {
		Scanner scanner = AutomaticScanner.create(file);
		final AtomicBoolean aborted = new AtomicBoolean();
		TaskListener abortedListener = new TaskListener() {
			@Override
			public void notifyAborted() {
				aborted.set(true);
			}
		};
		if(monitor != null) {
			monitor.addListener(abortedListener);
		}

		final long start = System.currentTimeMillis();
		try {
			long id = 0;
			long bytes = 0;
			/*
			long mb = 0, lastmb = 0;
			*/
			while(scanner.hasNextLine() && !aborted.get()) {
				final String line = scanner.nextLine();
				bytes += line.length();
				if(TO != 0 && bytes >= 1024 * 1024 * TO) {
					break;
				}
				loader.write(id, Util.trimRight(line));
				id++;
				if(id > lines) {
					break;
				}
				/*
				mb = bytes / 1024 / 1024 / 100;
				if(mb != lastmb) {
					lastmb = mb;
					System.err.printf("read %d MB, %6.1fm lines: %s\n", bytes / 1024 / 1024, id / 1000.0 / 1000.0,
						mti.getStatus());
				}
				*/
			}
		} finally {
			if(monitor != null) {
				monitor.removeListener(abortedListener);
			}
			System.err.printf("reading: %d ms\n", System.currentTimeMillis() - start);
			scanner.close();
		}
	}
}
