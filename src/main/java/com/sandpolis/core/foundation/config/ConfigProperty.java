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

import java.util.Optional;

public interface ConfigProperty<T> {

	/**
	 * Get whether the property was defined by the runtime environment.
	 *
	 * @return Whether the property has a non-null value
	 */
	public boolean defined();

	/**
	 * Get the name of this property.
	 *
	 * @return The property name
	 */
	public String property();

	/**
	 * Suggest that the runtime environment provide a value for this property. If no
	 * value is found, the property will remain be "undefined".
	 */
	public void register();

	/**
	 * Suggest that the runtime environment provide a value for this property. If no
	 * value is found, the given default will be used.
	 *
	 * @param _default A default property value
	 */
	public void register(T _default);

	/**
	 * Insist that the runtime environment provide a value for this property. If no
	 * value is found, an exception will be thrown.
	 */
	public void require();

	/**
	 * Get the value of this property.
	 *
	 * @return The property value
	 */
	public Optional<T> value();
}
