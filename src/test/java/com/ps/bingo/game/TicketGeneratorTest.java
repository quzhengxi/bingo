package com.ps.bingo.game;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ps.bingo.exception.BingoException;

public class TicketGeneratorTest {

	@Test
	public void testGenerateTicket80_5X10_5() {
		try {
			executeTest();
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testGenerateTicket80_5X10_10() {
		try {
			executeTest();
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testGenerateTicket80_8X10_10() {
		try {
			executeTest();
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	/*
	 * failure case: Number Range: 80, Ticket Size: 9X10, Number Per Row: 10
	 * There is no enough number to fill ticket.
	 */
	@Test
	public void testGenerateTicket80_9X10_10() {
		assertThrows(BingoException.class, () -> executeTest()) ;
	}

	private void executeTest() throws Exception {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		String settings = null;
		for(StackTraceElement trace : stackTraces) {
			if(trace.getMethodName().startsWith("testGenerateTicket")) {
				 settings = trace.getMethodName().replace("testGenerateTicket", "");
				 break;
			}
		}
		if(settings == null)
			throw new Exception("Failed to read settings from method name, whose format is: testGenerateTicket80_9X10_10.");
		String[] split = settings.split("(_)|(X)");
		this.testTicketWithSettings(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),
				Integer.parseInt(split[3]));

	}

	private void testTicketWithSettings(int numberRange, int ticketRowSize, int ticketColSize, int numberPerRow) throws BingoException {
		GameSettings settings = new GameSettings();
		settings.setNumberRange(numberRange);
		settings.setPlayerNumber(3);
		settings.setTicketRowSize(ticketRowSize);
		settings.setTicketColumnSize(ticketColSize);
		settings.setNumberPerRow(numberPerRow);
		Ticket ticket = TicketGenerator.generateTicket(settings);
		TicketUtils.verifyTicket(ticket, settings);
	}

	
}
