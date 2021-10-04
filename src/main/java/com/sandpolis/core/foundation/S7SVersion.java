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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.regex.Pattern;

public record S7SVersion(String version) implements Comparable<S7SVersion> {

	@Override
	public int compareTo(S7SVersion o) {
		return Arrays.compare(Arrays.stream(version.split("\\.|\\+")).mapToInt(Integer::parseInt).toArray(),
				Arrays.stream(o.version.split("\\.")).mapToInt(Integer::parseInt).toArray());
	}

	public static S7SVersion of(String version) {
		return new S7SVersion(version);
	}

	public boolean isModuleVersion() {
		return true;
	}

	/**
	 * Extract the version number out of the JVM --version text.
	 *
	 * @param versionText The version info
	 * @return The version number
	 */
	public static S7SVersion fromJavaVersionText(String versionText) {
		checkNotNull(versionText);

		var matcher = Pattern.compile("\\b([0-9]+\\.[0-9]\\.[0-9])\\b").matcher(versionText);
		if (matcher.find()) {
			return new S7SVersion(matcher.group(1));
		}
		return null;
	}

}
