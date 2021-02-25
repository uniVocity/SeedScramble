package com.univocity.seedscramble.seed;

import java.util.*;

public class SeedScrambler {
	private static long bytesToLong(byte[] b) {
		long result = 0;
		for (int i = 0; i < b.length; i++) {
			result <<= 8;
			result |= (b[i] & 0xFF);
		}
		return result;
	}

	private static void unscramble(int cycles, Random random, byte[] entropy) {
		int[] positions = new int[entropy.length * cycles];
		for (int i = 0; i < positions.length; i++) {
			positions[i] = Math.abs(random.nextInt() % entropy.length);
		}

		for (int i = positions.length - 1; i >= 0; i--) {
			swap(entropy, i, positions[i]);
		}
	}

	private static void scramble(int cycles, Random random, byte[] entropy) {
		for (int i = 0; i < entropy.length * cycles; i++) {
			swap(entropy, i, Math.abs(random.nextInt() % entropy.length));
		}
	}

	private static void swap(byte[] entropy, int i, int x) {
		int y = i % entropy.length;

		byte tmp = entropy[y];
		entropy[y] = entropy[x];
		entropy[x] = tmp;
	}

	private static byte[] getPassword(String password) {
		char[] pwd = password.toCharArray();
		final byte[] out = new byte[pwd.length];
		for (int i = 0; i < pwd.length; i++) {
			out[i] = (byte) pwd[i];
		}
		return out;
	}

	public static String scrambleSeed(String seed, int cycles, String password){
		Random random = new Random(bytesToLong(getPassword(password)));

		byte[] bytes = Seed.checkEnglishSeedPhrase(seed);
		scramble(cycles, random, bytes);

		return Seed.generateEnglishSeedPhrase(bytes);
	}

	public static String unscrambleSeed(String scrambledSeed, int cycles, String password){
		Random random = new Random(bytesToLong(getPassword(password)));

		byte[] bytes = Seed.checkEnglishSeedPhrase(scrambledSeed);
		unscramble(cycles, random, bytes);

		return Seed.generateEnglishSeedPhrase(bytes);
	}
}
