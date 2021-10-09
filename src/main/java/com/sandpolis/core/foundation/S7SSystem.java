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
import static com.sandpolis.core.foundation.Platform.OsType.FREEBSD;
import static com.sandpolis.core.foundation.Platform.OsType.LINUX;
import static com.sandpolis.core.foundation.Platform.OsType.MACOS;
import static com.sandpolis.core.foundation.Platform.OsType.NETBSD;
import static com.sandpolis.core.foundation.Platform.OsType.OPENBSD;
import static com.sandpolis.core.foundation.Platform.OsType.SOLARIS;
import static com.sandpolis.core.foundation.Platform.OsType.UNKNOWN_OS;
import static com.sandpolis.core.foundation.Platform.OsType.WINDOWS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sandpolis.core.foundation.Platform.ArchType;
import com.sandpolis.core.foundation.Platform.OsType;

public final class S7SSystem {

	private static final Logger log = LoggerFactory.getLogger(S7SSystem.class);

	public static final OsType OS_TYPE = queryOsType();

	public static final ArchType ARCH_TYPE = queryArchType();

	static {
		log.trace("Determined OS type: {}", OS_TYPE);
		log.trace("Determined architecture type: {}", ARCH_TYPE);
	}

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
		String uname = S7SProcess.exec("uname", "-m").string().orElse("").toLowerCase();

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
			String wmic = S7SProcess.exec("wmic", "computersystem", "get", "systemtype").string().orElse("")
					.toLowerCase();

			if (wmic.contains("x64"))
				return X86_64;

			if (wmic.contains("x86"))
				return X86;
		}

		return UNKNOWN_ARCH;
	}

	private S7SSystem() {
	}
}