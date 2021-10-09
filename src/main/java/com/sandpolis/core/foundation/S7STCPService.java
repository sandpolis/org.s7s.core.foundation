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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record S7STCPService(int port) {

	public static S7STCPService of(int port) {
		if (port < 0 || port >= 65536)
			throw new IllegalArgumentException("Invalid port: " + port);

		return new S7STCPService(port);
	}

	/**
	 * The number of milliseconds to wait before considering a port to be
	 * unreachable.
	 */
	private static final int PORTCHECK_TIMEOUT = 850;

	/**
	 * Tests the visibility of a port on a remote host by attempting a socket
	 * connection.
	 *
	 * @param host The target DNS name or IP address
	 * @return True if the port is open, false if the port is closed or there was a
	 *         connection error
	 * @throws IOException
	 */
	public boolean checkPort(String host) throws IOException {

		try (Socket sock = new Socket()) {
			sock.connect(new InetSocketAddress(host, port), PORTCHECK_TIMEOUT);
			return sock.isConnected();
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		} catch (ConnectException e) {
			return false;
		}
	}

	/**
	 * A cache for service names obtained from the system.
	 */
	private static Map<Integer, String> serviceCache = new HashMap<>();

	/**
	 * Whether service name resolution is enabled. This flag will be set if it
	 * becomes unlikely that future resolution attempts will succeed.
	 */
	private static boolean serviceResolutionEnabled = true;

	/**
	 * Resolve an IANA service name.
	 *
	 * @param port A TCP port number
	 * @return The service name registered to the given port number
	 */
	public Optional<String> serviceName() {

		if (!serviceResolutionEnabled)
			// Service name resolution disabled
			return Optional.empty();

		if (port >= 49152)
			// Ephemeral port range
			return Optional.empty();

		if (serviceCache.containsKey(port))
			// The service name is cached
			return Optional.of(serviceCache.get(port));

		try {
			var serviceName = serviceName0(port);
			if (serviceName != null) {
				serviceCache.put(port, serviceName);
				return Optional.of(serviceName);
			} else {
				return Optional.empty();
			}
		} catch (Exception e) {
			serviceResolutionEnabled = false;
			return Optional.empty();
		}
	}

	private static String serviceName0(int port) throws IOException {

		Path services = Paths.get("/etc/services");
		if (Files.exists(services)) {
			try (var in = Files.newBufferedReader(services)) {

				// Skip header
				in.readLine();
				in.readLine();

				String line;
				while ((line = in.readLine()) != null) {
					int p = Integer.parseInt(line.substring(16, 21).trim());

					if (p > port) {
						break;
					} else if (p == port) {
						return line.substring(0, 16).trim();
					}
				}
				return null;
			}
		}

		// TODO check other locations
		throw new FileNotFoundException();
	}

}
