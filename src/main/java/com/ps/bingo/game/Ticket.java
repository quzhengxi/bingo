package com.ps.bingo.game;

import java.util.Arrays;

public class Ticket {

	private final int rowSize;
	private final int colSize;
	private final int[][] matrix;

	public Ticket(final int rowSize, final int colSize) {
		this.rowSize = rowSize;
		this.colSize = colSize;
		this.matrix = new int[rowSize][colSize];
	}

	public void setNumber(final int r, final int c, final int val) {
		if (r < 0 || r >= rowSize || c < 0 || c >= colSize || val <= 0)
			throw new IllegalArgumentException("");
		matrix[r][c] = val;
	}

	public boolean removeNumber(final int num) {
		for (int r = 0; r < rowSize; r++)
			for (int c = 0; c < colSize; c++) {
				if (matrix[r][c] == num) {
					matrix[r][c] = -1;
					return true;
				}
			}
		return false;
	}

	public boolean isEarlyFive() {
		int striked = 0;
		for (int r = 0; r < rowSize; r++)
			for (int c = 0; c < colSize; c++) {
				if (matrix[r][c] == -1) {
					striked += 1;
					if (striked >= 5)
						return true;
				}
			}
		return false;
	}

	public boolean isTopLine() {
		for (int c = 0; c < colSize; c++) {
			if (matrix[0][c] != 0 && matrix[0][c] != -1) {
				return false;
			}
		}
		return true;
	}

	public boolean isFullHouse() {
		for (int r = 0; r < rowSize; r++)
			for (int c = 0; c < colSize; c++) {
				if (matrix[r][c] != 0 && matrix[r][c] != -1) {
					return false;
				}
			}
		return true;
	}
	
	public void print() {
		for (int r = 0; r < rowSize; r++)
			System.out.println(Arrays.toString(matrix[r]));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < rowSize; r++)
			sb.append(Arrays.toString(matrix[r])).append("\n");
		
		return sb.toString();
	}

}
