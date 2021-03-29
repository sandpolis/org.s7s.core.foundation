//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.foundation.config;

public final class CfgFoundation {

	/**
	 * Whether development features should be enabled.
	 */
	public static final ConfigProperty<Boolean> DEVELOPMENT_MODE = new SysEnvConfigProperty<>(Boolean.class,
			"s7s.development_mode");

	private CfgFoundation() {
	}
}
