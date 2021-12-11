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

/**
 * Wrapper for {@link Process} that provides a simple interface without checked
 * exceptions.
 */
public record S7SProcess(Process process) {

	private static final Logger log = LoggerFactory.getLogger(S7SProcess.class);

	public static interface CompletionHandler {
		public void complete(int exit, String stdout, String stderr);
	}

	public static class ProcessException extends RuntimeException {

		private ProcessException(InterruptedException e) {
			super(e);
		}

		private ProcessException(IOException e) {
			super(e);
		}
	}

	/**
	 * Start a new process.
	 * 
	 * @param executable The process executable
	 * @param cmdLine    Arguments
	 * @return A new {@link S7SProcess}
	 */
	public static S7SProcess exec(Path executable, String... cmdLine) {
		return exec(ObjectArrays.concat(executable.toString(), cmdLine));
	}

	/**
	 * Start a new process.
	 * 
	 * @param cmdLine The process executable and arguments
	 * @return A new {@link S7SProcess}
	 */
	public static S7SProcess exec(String... cmdLine) {

		if (log.isTraceEnabled())
			log.trace("Executing system command: \"{}\"", String.join(" ", cmdLine));

		try {
			return new S7SProcess(Runtime.getRuntime().exec(cmdLine));
		} catch (IOException e) {
			throw new ProcessException(e);
		}
	}

	public Stream<String> stdoutLines() {
		return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
	}

	public Stream<String> stderrLines() {
		return new BufferedReader(new InputStreamReader(process.getErrorStream())).lines();
	}

	/**
	 * Write to the process's stdin.
	 * 
	 * @param input The desired input
	 * @return {@code this}
	 */
	public S7SProcess stdin(String input) {
		try {
			process.getOutputStream().write(input.getBytes());
			process.getOutputStream().flush();
		} catch (IOException e) {
			throw new ProcessException(e);
		}
		return this;
	}

	/**
	 * @return The process's full stdout
	 */
	public String stdout() {
		try {
			return CharStreams.toString(new InputStreamReader(process.getInputStream()));
		} catch (IOException e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * @return The process's full stderr
	 */
	public String stderr() {
		try {
			return CharStreams.toString(new InputStreamReader(process.getErrorStream()));
		} catch (IOException e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * Wait for the process to complete.
	 * 
	 * @return The process exit code
	 */
	public int complete() {
		try {
			return process.waitFor();
		} catch (InterruptedException e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * Wait for the process to complete.
	 * 
	 * @param handler Handler for the stdout, stderr, and exit code
	 */
	public void complete(CompletionHandler handler) {
		int exit = complete();
		handler.complete(exit, stdout(), stderr());
	}

	/**
	 * Run the given handler when the process completes.
	 * 
	 * @param handler Handler for the stdout, stderr, and exit code
	 */
	public void onComplete(CompletionHandler handler) {
		// Simple, but not scalable
		new Thread(() -> {
			complete(handler);
		}).start();
	}

}
