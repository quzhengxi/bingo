package com.ps.bingo.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TicketUtils {

	public static void verifyTicket(Ticket ticket, GameSettings settings) {
		assertNotNull(ticket);
		String matrix = ticket.toString();
		verifyTicket(matrix, settings);
	}
	public static void verifyTicket(String matrix, GameSettings settings) {
		assertNotNull(matrix);
		String[] rows = matrix.split("\n");
		assertEquals(settings.getTicketRowSize(), rows.length);
		for (String row : rows) {
			String[] numbers = row.replace("[", "").replace("]", "").split(",");
			assertEquals(settings.getTicketColumnSize(), numbers.length);

			int numberCount = 0;
			for (String s : numbers) {
				if (!s.trim().equals("0") && !s.trim().equals("-1"))
					numberCount += 1;
			}
			assertEquals(settings.getNumberPerRow(), numberCount);
		}
	}
	
	public static int[][] getTicketMatrix(String ticket, GameSettings settings) {
		assertNotNull(ticket);
		int[][] matrix = new int[settings.getTicketRowSize()][settings.getTicketColumnSize()];
		String[] rows = ticket.split("\n");
		assertEquals(settings.getTicketRowSize(), rows.length);
		int r = 0;
		for (String row : rows) {
			String[] numbers = row.replace("[", "").replace("]", "").split(",");
			assertEquals(settings.getTicketColumnSize(), numbers.length);

			int c = 0;
			for (String s : numbers) {
				matrix[r][c] = Integer.parseInt(s.trim());
				c += 1;
			}
			r += 1;
		}
		return matrix;
	}
}
