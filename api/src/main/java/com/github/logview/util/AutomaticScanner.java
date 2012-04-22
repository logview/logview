package com.github.logview.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class AutomaticScanner {
	public static Scanner create(File file) throws IOException {
		if(file.getName().endsWith(".gz")) {
			return new Scanner(new GZIPInputStream(new FileInputStream(file)), "UTF-8");
		} else {
			return new Scanner(new FileReader(file));
		}
	}
}
