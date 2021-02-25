package com.univocity.seedscramble;

import org.apache.commons.io.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Utils {

	public static String readTextFromResource(String resourcePath, Charset encoding) {
		return readTextFromInput(getInput(resourcePath), encoding);
	}

	public static List<String> readLinesFromResource(String resourcePath, Charset encoding) {
		return readLinesFromInput(getInput(resourcePath), encoding);
	}

	public static List<String> readLinesFromInput(InputStream in, Charset encoding) {
		try {
			return IOUtils.readLines(in, encoding);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new IllegalArgumentException("Unable to read contents from input", e);
		}
	}

	public static String readTextFromInput(InputStream in, Charset encoding) {
		try {
			StringBuilder out = new StringBuilder();

			for (String line : readLinesFromInput(in, encoding)) {
				out.append(line).append('\n');
			}
			if (out.length() > 0) {
				out.deleteCharAt(out.length() - 1);
			}
			return out.toString();
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new IllegalArgumentException("Unable to read contents from input", e);
		}
	}

	private static InputStream getInput(String path) {
		try {
			if (path != null) {
				path = FilenameUtils.separatorsToUnix(path);
			}

			File file = new File(path);
			if (!file.exists()) {
				InputStream input = Utils.class.getResourceAsStream(path);
				if (input == null) {
					input = Utils.class.getClassLoader().getResourceAsStream(path);
				}
				if (input == null) {
					input = ClassLoader.getSystemResourceAsStream(path);
				}
				if (input == null) {
					throw new IllegalStateException("Unable to determine resource from given path: " + path);
				} else {
					return input;
				}
			} else {
				return new FileInputStream(file);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read resource from given path " + path, e);
		}
	}

	/**
	 * Ensures a given argument is not null.
	 *
	 * @param o         the object to validate
	 * @param fieldName the description of the field
	 */
	public static final void notNull(Object o, String fieldName) {
		if (o == null) {
			throw new IllegalArgumentException(fieldName + " cannot be null");
		}
	}

	/**
	 * Ensures a given {@link CharSequence} argument is not null/empty/blank
	 *
	 * @param o         a character sequence
	 * @param fieldName the description of the field
	 */
	public static final void notBlank(CharSequence o, String fieldName) {
		notNull(o, fieldName);
		if (o.toString().trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " cannot be blank");
		}
	}


}
