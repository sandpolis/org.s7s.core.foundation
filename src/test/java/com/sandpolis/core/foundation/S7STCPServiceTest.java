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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class S7STCPServiceTest {

	@Test
	@DisplayName("Check some open ports")
	void checkOpenPorts() throws IOException {

		try (ServerSocket socket = new ServerSocket(8923)) {
			assertTrue(S7STCPService.of(8923).checkPort("localhost"));
		}

		assertFalse(S7STCPService.of(8923).checkPort("localhost"));
	}

	@Test
	@DisplayName("Check some closed ports")
	void checkClosedPorts() throws IOException {
		assertFalse(S7STCPService.of(8923).checkPort("localhost"));
	}

	@Test
	@DisplayName("Check well-known service names")
	void getServiceName() {
		assumeTrue(System.getProperty("os.name").toLowerCase().contains("linux"));

		assertEquals("ssh", S7STCPService.of(22).serviceName().get());
		assertEquals("sandpolis", S7STCPService.of(8768).serviceName().get());
	}

}
