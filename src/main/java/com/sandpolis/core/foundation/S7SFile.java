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

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.module.ModuleFinder;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public record S7SFile(Path path) {

	public static S7SFile of(Path path) {
		return new S7SFile(path);
	}

	public static S7SFile of(File file) {
		return new S7SFile(file.toPath());
	}

	/**
	 * Locate a module's jar file in the given directory.
	 *
	 * @param directory The directory to search
	 * @param module    The module name
	 * @return The file containing the desired module
	 */
	public Optional<Path> findModule(String module) {
		if (!Files.isDirectory(path))
			throw new IllegalArgumentException();

		return ModuleFinder.of(path).find(module).flatMap(ref -> {
			return ref.location().map(Paths::get);
		});
	}

	/**
	 * Download a file from the Internet to a local file.
	 *
	 * @param url  The resource location
	 * @param file The output file
	 * @throws IOException
	 */
	public void download(URL url) throws IOException {
		if (url == null)
			throw new IllegalArgumentException();

		URLConnection con = url.openConnection();

		try (DataInputStream in = new DataInputStream(con.getInputStream())) {
			try (OutputStream out = Files.newOutputStream(path)) {
				in.transferTo(out);
			}
		}
	}

	/**
	 * Logically overwrite a file with 0's. There's no way to know whether the new
	 * bytes will be written to the file's original physical location, so this
	 * method should not be used for secure applications.
	 *
	 * @throws IOException
	 */
	public void overwrite() throws IOException {

		if (!Files.exists(path))
			throw new FileNotFoundException();

		byte[] zeros = new byte[4096];

		try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "w")) {
			for (long i = 0; i < raf.length(); i += zeros.length)
				raf.write(zeros);
			for (long i = 0; i < raf.length() % zeros.length; i++)
				raf.writeByte(0);
		}
	}

	/**
	 * Replace the first occurrence of the placeholder in the binary file with the
	 * given replacement. This method uses a standard needle/haystack linear search
	 * algorithm with backtracking.
	 *
	 * @param placeholder The unique placeholder
	 * @param replacement The payload buffer
	 * @throws IOException
	 */
	public void replace(short[] placeholder, byte[] replacement) throws IOException {

		if (!Files.exists(path))
			throw new FileNotFoundException();

		// Check the replacement buffer size
		if (replacement.length > placeholder.length)
			throw new IllegalArgumentException();

		try (var ch = FileChannel.open(path, READ, WRITE)) {
			var buffer = ch.map(MapMode.READ_WRITE, ch.position(), ch.size()).order(ByteOrder.nativeOrder());

			buffer.mark();
			find: while (buffer.remaining() >= placeholder.length) {
				for (int i = 0; i < placeholder.length; i++) {
					if (buffer.get() != (byte) placeholder[i]) {
						buffer.reset();
						buffer.position(buffer.position() + 1).mark();
						continue find;
					}
				}

				// Return to the start of the placeholder
				buffer.position(buffer.position() - placeholder.length);

				// Overwrite
				buffer.put(replacement);
				return;
			}

			// Placeholder not found
			throw new IOException("Failed to find placeholder");
		}
	}
}
