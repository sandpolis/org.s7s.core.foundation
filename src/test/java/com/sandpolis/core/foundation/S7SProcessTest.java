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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import com.sandpolis.core.foundation.Platform.OsType;

class S7SProcessTest {

	@Test
	void testCompleteLinux() {
		assumeTrue(S7SSystem.OS_TYPE == OsType.LINUX);

		S7SProcess.exec("true").complete((exit, stdout, stderr) -> {
			assertEquals(0, exit);
			assertEquals("", stdout);
			assertEquals("", stderr);
		});

		S7SProcess.exec("false").complete((exit, stdout, stderr) -> {
			assertEquals(1, exit);
			assertEquals("", stdout);
			assertEquals("", stderr);
		});

		S7SProcess.exec("echo", "true").complete((exit, stdout, stderr) -> {
			assertEquals(0, exit);
			assertEquals("true\n", stdout);
			assertEquals("", stderr);
		});
	}

}
