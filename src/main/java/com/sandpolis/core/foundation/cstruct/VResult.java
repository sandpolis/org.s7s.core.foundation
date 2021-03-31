//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.foundation.cstruct;

import java.util.Optional;

/**
 * The result of a validation or verification attempt.
 */
public final class VResult {

	/**
	 * The overall result.
	 */
	public final boolean result;

	/**
	 * An additional message if the result is false.
	 */
	public final Optional<String> message;

	public VResult(String message) {
		this.result = false;
		this.message = Optional.of(message);
	}

	public VResult(boolean result) {
		this.result = result;
		this.message = Optional.empty();
	}

}
