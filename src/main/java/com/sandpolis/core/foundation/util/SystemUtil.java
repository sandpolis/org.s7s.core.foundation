//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.foundation.util;

import static com.sandpolis.core.foundation.Platform.OsType.FREEBSD;
import static com.sandpolis.core.foundation.Platform.ArchType.AARCH64;
import static com.sandpolis.core.foundation.Platform.ArchType.ARM;
import static com.sandpolis.core.foundation.Platform.ArchType.MIPS;
import static com.sandpolis.core.foundation.Platform.ArchType.MIPS64;
import static com.sandpolis.core.foundation.Platform.ArchType.POWERPC;
import static com.sandpolis.core.foundation.Platform.ArchType.POWERPC64;
import static com.sandpolis.core.foundation.Platform.ArchType.S390X;
import static com.sandpolis.core.foundation.Platform.ArchType.SPARC64;
import static com.sandpolis.core.foundation.Platform.ArchType.UNKNOWN_ARCH;
import static com.sandpolis.core.foundation.Platform.ArchType.X86;
import static com.sandpolis.core.foundation.Platform.ArchType.X86_64;
import static com.sandpolis.core.foundation.Platform.OsType.DRAGONFLYBSD;
import static com.sandpolis.core.foundation.Platform.OsType.NETBSD;
import static com.sandpolis.core.foundation.Platform.OsType.OPENBSD;
import static com.sandpolis.core.foundation.Platform.OsType.SOLARIS;
import static com.sandpolis.core.foundation.Platform.OsType.LINUX;
import static com.sandpolis.core.foundation.Platform.OsType.MACOS;
import static com.sandpolis.core.foundation.Platform.OsType.UNKNOWN_OS;
import static com.sandpolis.core.foundation.Platform.OsType.WINDOWS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;
import com.sandpolis.core.foundation.Platform.ArchType;
import com.sandpolis.core.foundation.Platform.OsType;

public final class SystemUtil {

	private static final Logger log = LoggerFactory.getLogger(SystemUtil.class);

	public static final OsType OS_TYPE = queryOsType();

	public static final ArchType ARCH_TYPE = queryArchType();

	/**
	 * @return The system's {@link OsType}
	 */
	private static OsType queryOsType() {
		String name = System.getProperty("os.name").toLowerCase();

		if (name.contains("windows"))
			return WINDOWS;

		if (name.contains("linux"))
			return LINUX;

		if (name.contains("mac") || name.contains("darwin"))
			return MACOS;

		if (name.contains("solaris") || name.contains("sunos"))
			return SOLARIS;

		if (name.contains("freebsd"))
			return FREEBSD;

		if (name.contains("openbsd"))
			return OPENBSD;

		if (name.contains("netbsd"))
			return NETBSD;

		if (name.contains("dragonfly"))
			return DRAGONFLYBSD;

		return UNKNOWN_OS;
	}

	/**
	 * @return The system's {@link ArchType}
	 */
	private static ArchType queryArchType() {

		// Try uname first because many systems have it
		String uname = exec("uname", "-m").string().toLowerCase();

		if (!uname.isBlank()) {
			if (uname.contains("x86_64") || uname.contains("ia64"))
				return X86_64;

			if (uname.contains("i686") || uname.contains("i386"))
				return X86;

			if (uname.contains("armv7") || uname.contains("armv6"))
				return ARM;

			if (uname.contains("aarch64") || uname.contains("armv8"))
				return AARCH64;

			if (uname.contains("ppc64"))
				return POWERPC64;

			if (uname.contains("ppc"))
				return POWERPC;

			if (uname.contains("mips64"))
				return MIPS64;

			if (uname.contains("mips"))
				return MIPS;

			if (uname.contains("s390"))
				return S390X;

			if (uname.contains("sparc"))
				return SPARC64;
		}

		// Also try WMI on windows
		if (OS_TYPE == WINDOWS) {
			String wmic = exec("wmic", "computersystem", "get", "systemtype").string().toLowerCase();

			if (wmic.contains("x64"))
				return X86_64;

			if (wmic.contains("x86"))
				return X86;
		}

		return UNKNOWN_ARCH;
	}

	public static final class ProcessWrapper {

		private Process process;
		private Exception error;

		private ProcessWrapper(Process process) {
			this.process = Objects.requireNonNull(process);
		}

		private ProcessWrapper(Exception e) {
			this.error = Objects.requireNonNull(e);
		}

		public ProcessWrapper complete(long timeout) throws Exception {
			if (error != null)
				throw error;

			process.waitFor(timeout, TimeUnit.SECONDS);
			return this;
		}

		public int exitValue() throws InterruptedException {
			return process.waitFor();
		}

		public Stream<String> lines() {
			return new BufferedReader(new InputStreamReader(process.getInputStream())).lines();
		}

		public String string() {
			try {
				return CharStreams.toString(new InputStreamReader(process.getInputStream()));
			} catch (IOException e) {
				error = e;
				return "";
			}
		}

	}

	public static ProcessWrapper exec(String... cmd) {
		log.trace("Executing system command: \"{}\"", String.join(" ", cmd));

		try {
			return new ProcessWrapper(Runtime.getRuntime().exec(cmd));
		} catch (IOException e) {
			// A failed ProcessWrapper
			return new ProcessWrapper(e);
		}
	}

	public static ProcessWrapper execp(String... cmd) {
		// TODO use OS type
		return exec(cmd);
	}

	private SystemUtil() {
	}
}
