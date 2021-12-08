//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.foundation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ObjectArrays;
import com.google.common.io.CharStreams;

public record S7SProcess(Process process) {

	private static final Logger log = LoggerFactory.getLogger(S7SProcess.class);

	public static interface CompletionHandler {
		public void complete(int exit, String stdout, String stderr);
	}

	public static S7SProcess exec(Path executable, String... cmdLine) {
		return exec(ObjectArrays.concat(executable.toString(), cmdLine));
	}

	public static S7SProcess exec(String... cmdLine) {
		log.trace("Executing system command: \"{}\"", String.join(" ", cmdLine));

		try {
			return new S7SProcess(Runtime.getRuntime().exec(cmdLine));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static S7SProcess shell(String command) {
		// TODO platform
		return exec("sh", "-c", command);
	}

	public Stream<String> stdoutLines() {
		return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
	}

	public String stdout() {
		try {
			return CharStreams.toString(new InputStreamReader(process.getInputStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String stderr() {
		try {
			return CharStreams.toString(new InputStreamReader(process.getErrorStream()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int exitValue() {
		try {
			return process.waitFor();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void complete(CompletionHandler handler) {
		int exit = exitValue();
		handler.complete(exit, stdout(), stderr());
	}

	public void onComplete(CompletionHandler handler) {
		// Simple, but slow
		new Thread(() -> {
			complete(handler);
		}).start();
	}

}
