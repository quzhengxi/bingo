package com.ps.bingo.game;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.ps.bingo.Constants;
import com.ps.bingo.Simulator;
import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;

public class GameTest extends CommonTest{

	@Test
	public void testGame() {
		try {
			GameSettings settings = new GameSettings(NUMBER_RANGE, PLAYER_NUMBER, TICKET_ROW_SIZE, TICKET_COLUMN_SIZE, NUMBER_PER_ROW);
			Simulator sim = mock(Simulator.class);	
			LockManager lockManager = new LockManager(settings.getPlayerNumber());
			ErrorCallback errorCallback = new ErrorCallback(sim);
			Game game = new Game(settings).buildAndStart(lockManager, errorCallback, System.out);
			while (!game.isDone()) {
				String input = "N";
				if (Constants.NEXT_CMD.equals(input.toUpperCase())) {
					lockManager.resetNextCommandLatch(); // set latch to 1
					game.sendNextCommand();
					lockManager.nextCommandLatchAwait(); // wait for caller to count down				
				}
			}
			assertTrue(game.isDone());
		} catch (BingoException e) {
			assertTrue(false);
		}
	}
}
