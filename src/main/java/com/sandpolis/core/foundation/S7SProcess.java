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
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;

public record S7SProcess(Process process) {

	private static final Logger log = LoggerFactory.getLogger(S7SProcess.class);

	public static S7SProcess exec(String... cmdLine) {
		log.trace("Executing system command: \"{}\"", String.join(" ", cmdLine));

		try {
			return new S7SProcess(Runtime.getRuntime().exec(cmdLine));
		} catch (IOException e) {
			// A failed process
			return new S7SProcess(null);
		}
	}

	public static S7SProcess shell(String command) {
		// TODO platform
		return exec("sh", "-c", command);
	}

	public Stream<String> lines() {
		return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
	}

	public Optional<String> string() {
		try {
			return Optional.of(CharStreams.toString(new InputStreamReader(process.getInputStream())));
		} catch (IOException e) {
			return Optional.empty();
		}
	}

}
