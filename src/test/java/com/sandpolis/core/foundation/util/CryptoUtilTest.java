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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CryptoUtilTest {

	@Test
	@DisplayName("Check PBKDF2 verification")
	void testCheckPBKDF2() {
		assertTrue(CryptoUtil.PBKDF2.check("pa55w0rd",
				"2142:7D06806F24653DD5364C6BCFFEC86029:ts3IyK5ws7GI69Nti24WBd5zvOmunZ7eWj/GDV25j09SkmUPl+9HmSw0OXlH5mFq"));
		assertFalse(CryptoUtil.PBKDF2.check("pa55w0rd",
				"2142:7D06806F24653DD5364C6BCFFEC86029:UQWvabjl1dSWq21Edl+ME7lUb/L9KSKT90K2U6iPCtUGUbNiDnj5TdnGc6irJJgE"));

		assertTrue(CryptoUtil.PBKDF2.check("goodpass", CryptoUtil.PBKDF2.hash("goodpass")));
	}
}
