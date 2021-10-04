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

import static com.google.common.base.Preconditions.checkNotNull;

public record S7SString(String text) {

	public enum AnsiColor {
		BLUE(4), //
		CYAN(6), //
		GREEN(2), //
		MAGENTA(5), //
		RED(1), //
		YELLOW(3);

		public static String reset() {
			return String.format("\u001b[%dm", 0);
		}

		private final int value;

		AnsiColor(int index) {
			this.value = index;
		}

		public String bg() {
			return String.format("\u001b[%dm", value + 40);
		}

		public String bgBright() {
			return String.format("\u001b[%dm", value + 100);
		}

		public String fg() {
			return String.format("\u001b[%dm", value + 30);
		}

		public String fgBright() {
			return String.format("\u001b[%dm", value + 90);
		}
	}

	public static S7SString of(String text) {
		return new S7SString(text);
	}

	/**
	 * @return Whether the terminal is attached to a console and supports ANSI color
	 *         escape codes.
	 */
	public static boolean checkAnsiColors() {
		return true; // TODO
	}

	/**
	 * Colorize a string with ANSI escape codes.
	 *
	 * @param color The text color
	 * @return The colorized text
	 */
	public String colorize(AnsiColor color) {
		checkNotNull(color);

		if (!checkAnsiColors()) {
			return text;
		}

		StringBuilder buffer = new StringBuilder();
		buffer.append(color.fg());
		buffer.append(text);
		buffer.append(AnsiColor.reset());

		return buffer.toString();
	}

	/**
	 * Randomly colorize a string with ANSI escape codes. No two consecutive
	 * characters will be the same color.
	 *
	 * @return The colorized text
	 */
	public String rainbowize() {

		if (!checkAnsiColors()) {
			return text;
		}

		AnsiColor[] colors = AnsiColor.values();

		StringBuilder buffer = new StringBuilder();

		AnsiColor last = null;
		for (int i = 0; i < text.length(); i++) {
			AnsiColor rand = S7SRandom.nextItem(colors);
			if (rand == last) {
				i--;
				continue;
			}

			last = rand;
			buffer.append(rand.fg());
			buffer.append(text.charAt(i));
		}
		buffer.append(AnsiColor.reset());

		return buffer.toString();
	}

	/**
	 * Get ascii art that says "sandpolis".
	 *
	 * @return A String of 6 lines that each measure 43 characters wide
	 */
	public static String getSandpolisArt() {
		return "                     _             _ _     \n ___  __ _ _ __   __| |_ __   ___ | (_)___ \n/ __|/ _` | '_ \\ / _` | '_ \\ / _ \\| | / __|\n\\__ \\ (_| | | | | (_| | |_) | (_) | | \\__ \\\n|___/\\__,_|_| |_|\\__,_| .__/ \\___/|_|_|___/\n                      |_|                  ";
	}
}
