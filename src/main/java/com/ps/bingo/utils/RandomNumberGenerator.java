package com.ps.bingo.utils;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomNumberGenerator {
	private static Random rand = new Random();

	public static int randInt(int min, int max, Set<Integer> seen) {
		int randomNum ;
		do{
			randomNum = rand.nextInt((max - min) + 1) + min;
		}while(seen.contains(randomNum));
		seen.add(randomNum);
		return randomNum;
	}
	public static int randInt(int min, int max, List<Integer> seen) {
		int randomNum ;
		do{
			randomNum = rand.nextInt((max - min) + 1) + min;
		}while(seen.contains(randomNum));
		seen.add(randomNum);
		return randomNum;
	}
}
