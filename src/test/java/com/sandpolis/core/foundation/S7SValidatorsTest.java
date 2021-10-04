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

import static com.sandpolis.core.foundation.S7SValidators.GROUP_MAX;
import static com.sandpolis.core.foundation.S7SValidators.GROUP_MIN;
import static com.sandpolis.core.foundation.S7SValidators.USER_MAX;
import static com.sandpolis.core.foundation.S7SValidators.USER_MIN;
import static com.sandpolis.core.foundation.S7SValidators.group;
import static com.sandpolis.core.foundation.S7SValidators.path;
import static com.sandpolis.core.foundation.S7SValidators.port;
import static com.sandpolis.core.foundation.S7SValidators.privateIP;
import static com.sandpolis.core.foundation.S7SValidators.username;
import static com.sandpolis.core.foundation.S7SValidators.version;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class S7SValidatorsTest {

	@Test
	void testUsername() {
		assertFalse(username(null));
		assertFalse(username("test user"));
		assertFalse(username("_test*"));
		assertFalse(username(""));

		assertTrue(username("cilki"));
	}

	@Test
	void testGroup() {
		assertFalse(group(null));
		assertFalse(group("test group**"));

		assertTrue(group("test group"));
	}

	@Test
	void testPrivateIP() {
		assertFalse(privateIP(null));
		assertFalse(privateIP("74.192.155.168"));
		assertFalse(privateIP("245.3.36.18"));
		assertFalse(privateIP("162.113.53.86"));
		assertFalse(privateIP("72.155.10.184"));
		assertFalse(privateIP("202.69.223.43"));
		assertFalse(privateIP("151.250.62.220"));
		assertFalse(privateIP("80.101.92.188"));
		assertFalse(privateIP("22.194.149.43"));
		assertFalse(privateIP("13.118.39.20"));
		assertFalse(privateIP("150.140.194.234"));
		assertFalse(privateIP("44.82.127.42"));

		assertTrue(privateIP("192.168.1.1"));
		assertTrue(privateIP("192.168.41.184"));
		assertTrue(privateIP("192.168.210.208"));
		assertTrue(privateIP("192.168.44.75"));
		assertTrue(privateIP("192.168.129.77"));
		assertTrue(privateIP("192.168.29.221"));
		assertTrue(privateIP("10.0.0.1"));
		assertTrue(privateIP("10.252.166.215"));
		assertTrue(privateIP("10.207.85.163"));
		assertTrue(privateIP("10.146.201.129"));
		assertTrue(privateIP("10.198.177.8"));
		assertTrue(privateIP("10.70.198.55"));

	}

	@Test
	void testPortString() {
		assertFalse(port(null));
		assertFalse(port(""));
		assertFalse(port("123456789"));
		assertFalse(port("4000g"));
		assertFalse(port("test"));
		assertFalse(port("-5000"));

		assertTrue(port("80"));
		assertTrue(port("8080"));
		assertTrue(port("10101"));

	}

	@Test
	void testPath() {
		assertFalse(path(null));

		assertTrue(path("test/.test.txt"));
	}

	@Test
	void testVersionSimple() {
		assertFalse(version(null));
		assertFalse(version("5..0"));
		assertFalse(version("5..0.0"));
		assertFalse(version("5.0.0.0"));

		assertTrue(version("5.0.0"));
		assertTrue(version("05.00.00"));
	}

	@Test
	void testVersionWithBuild() {
		assertFalse(version("5.0.0--0"));
		assertFalse(version("5.0.0-"));

		assertTrue(version("5.0.0-9"));
		assertTrue(version("5.0.0-92833434"));
	}

}
