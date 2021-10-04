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

import java.io.File;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.RegexValidator;

/**
 * Utilities that validate user input.
 *
 * @author cilki
 * @since 4.0.0
 */
public final class S7SValidators {
	private S7SValidators() {
	}

	/**
	 * The longest possible username.
	 */
	public static final int USER_MAX = 30;

	/**
	 * The shortest possible username.
	 */
	public static final int USER_MIN = 5;

	/**
	 * The longest possible group name.
	 */
	public static final int GROUP_MAX = 48;

	/**
	 * The shortest possible group name.
	 */
	public static final int GROUP_MIN = 4;

	/**
	 * The longest possible password.
	 */
	public static final int PASSWORD_MAX = 64;

	/**
	 * The shortest possible password.
	 */
	public static final int PASSWORD_MIN = 5;

	/**
	 * Username validator.
	 */
	private static final RegexValidator USERNAME_REGEX = new RegexValidator(
			String.format("^[a-zA-Z0-9]{%d,%d}$", USER_MIN, USER_MAX));

	/**
	 * Group name validator.
	 */
	private static final RegexValidator GROUPNAME_REGEX = new RegexValidator(
			String.format("^[a-zA-Z0-9 ]{%d,%d}$", GROUP_MIN, GROUP_MAX));

	/**
	 * Password validator.
	 */
	private static final RegexValidator PASSWORD_REGEX = new RegexValidator(
			String.format("^.{%d,%d}$", PASSWORD_MIN, PASSWORD_MAX));

	/**
	 * Private IPv4 validator.
	 */
	private static final RegexValidator PRIVATEIP_REGEX = new RegexValidator(
			"(^127\\..*$)|(^10\\..*$)|(^172\\.1[6-9]\\..*$)|(^172\\.2[0-9]\\..*$)|(^172\\.3[0-1]\\..*$)|(^192\\.168\\..*$)");

	/**
	 * Version validator.
	 */
	private static final RegexValidator VERSION_REGEX = new RegexValidator("^(\\d)+\\.(\\d)+\\.(\\d)+(-(\\d)+)?$");

	/**
	 * Validate a user name.
	 *
	 * @param user The candidate username
	 * @return The username validity
	 */
	public static boolean username(String user) {
		return USERNAME_REGEX.isValid(user);
	}

	/**
	 * Validate a group name.
	 *
	 * @param group The candidate group name
	 * @return The group name validity
	 */
	public static boolean group(String group) {
		return GROUPNAME_REGEX.isValid(group);
	}

	/**
	 * Validate a password.
	 *
	 * @param password The candidate password
	 * @return The password validity
	 */
	public static boolean password(String password) {
		return PASSWORD_REGEX.isValid(password);
	}

	/**
	 * Validate a private IPv4 address.
	 *
	 * @param ip The candidate IP address
	 * @return The IP validity
	 */
	public static boolean privateIP(String ip) {
		return ipv4(ip) && PRIVATEIP_REGEX.isValid(ip);
	}

	/**
	 * Validate a DNS name or IP address.
	 *
	 * @param address The candidate address
	 * @return The address validity
	 */
	public static boolean address(String address) {
		if (address == null)
			return false;

		return InetAddressValidator.getInstance().isValid(address) || DomainValidator.getInstance().isValid(address);
	}

	/**
	 * Validate a port number.
	 *
	 * @param port The candidate port
	 * @return The port validity
	 */
	public static boolean port(String port) {
		try {
			return port(Integer.parseInt(port));
		} catch (Throwable t) {
			return false;
		}
	}

	/**
	 * Validate a port number.
	 *
	 * @param port The candidate port
	 * @return The port validity
	 */
	public static boolean port(int port) {
		return (port > 0 && port < 65536);
	}

	/**
	 * Validate a filesystem path.
	 *
	 * @param path The candidate path
	 * @return The path validity
	 */
	public static boolean path(String path) {
		try {
			new File(path).getCanonicalPath();
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * Validate a Sandpolis version number.
	 *
	 * @param version The candidate version
	 * @return The version validity
	 */
	public static boolean version(String version) {
		return VERSION_REGEX.isValid(version);
	}

	/**
	 * Validate an email address.
	 *
	 * @param email The candidate email
	 * @return The email validity
	 */
	public static boolean email(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	/**
	 * Validate an IPv4 address.
	 *
	 * @param ipv4 The candidate address
	 * @return The address validity
	 */
	public static boolean ipv4(String ipv4) {
		return InetAddressValidator.getInstance().isValidInet4Address(ipv4);
	}

}
