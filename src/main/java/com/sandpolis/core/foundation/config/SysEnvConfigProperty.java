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

import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This {@link ConfigProperty} reads values from system properties and
 * environment variables in that order.
 * 
 * Note: environment variables are expected to be uppercase and separated with
 * underscores rather than dots. Therefore the equivalent environment variable
 * for "install.path" is "INSTALL_PATH".
 *
 * @since 5.0.0
 */
public class SysEnvConfigProperty<T> implements ConfigProperty<T> {

	private static final Logger log = LoggerFactory.getLogger(SysEnvConfigProperty.class);

	/**
	 * Whether the property has been "evaluated".
	 */
	private boolean evaluated;

	/**
	 * The property's name.
	 */
	private final String property;

	/**
	 * The property's data type.
	 */
	private final Class<T> type;

	/**
	 * The property's data value.
	 */
	private T value;

	public SysEnvConfigProperty(Class<T> type, String property) {
		this(type, property, null);
	}

	public SysEnvConfigProperty(Class<T> type, String property, T defaultValue) {
		this.type = Objects.requireNonNull(type);
		this.property = Objects.requireNonNull(property);
		this.value = defaultValue;
	}

	@Override
	public boolean defined() {
		evaluate();
		return value != null;
	}

	/**
	 * Attempt to determine a value for this property.
	 *
	 * @return Whether a value was obtained previously or by the current invocation
	 */
	protected boolean evaluate() {
		if (evaluated)
			return true;
		evaluated = true;

		String value;

		// First priority: system properties
		value = System.getProperty(property);
		if (value != null) {
			log.trace("Found system property: {}", property);
			setValue(value);
			return true;
		}

		// Second priority: environment variables
		value = System.getenv().get(property.toUpperCase().replace('.', '_'));
		if (value != null) {
			log.trace("Found environment variable: {}", property.toUpperCase().replace('.', '_'));
			setValue(value);
			return true;
		}

		return false;
	}

	@Override
	public String property() {
		return property;
	}

	@Override
	public void register() {
		evaluate();
	}

	@Override
	public void register(T _default) {
		evaluate();
		if (value == null)
			value = _default;
	}

	@Override
	public void require() {
		evaluate();
		if (!defined())
			throw new UnsatisfiedConfigPropertyException(property);
	}

	protected void setValue(String value) {
		Objects.requireNonNull(value);

		try {
			if (type == String.class) {
				this.value = (T) (String) value;
			}

			else if (type == String[].class) {
				this.value = (T) (String[]) value.split(",");
			}

			else if (type == Integer.class) {
				this.value = (T) (Integer) Integer.parseInt(value);
			}

			else if (type == Boolean.class) {
				this.value = (T) (Boolean) Boolean.parseBoolean(value);
			}
		} catch (Exception e) {
			log.error("Failed to parse property: {}", property);
		}
	}

	@Override
	public Optional<T> value() {
		evaluate();
		return Optional.ofNullable(value);
	}
}
