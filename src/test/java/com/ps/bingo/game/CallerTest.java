package com.ps.bingo.game;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.ps.bingo.Simulator;
import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;

public class CallerTest extends CommonTest {

	@Test
	public void testCaller() {
		try {
			GameData gameData = GameData.getInstance();
			GameSettings settings = new GameSettings(NUMBER_RANGE, PLAYER_NUMBER_1, TICKET_ROW_SIZE, TICKET_COLUMN_SIZE,
					NUMBER_PER_ROW);
			LockManager lockManager = new LockManager(settings.getPlayerNumber());
			Simulator sim = mock(Simulator.class);
			ErrorCallback errorCallback = new ErrorCallback(sim);

			Thread caller = new Thread(
					new Caller(gameData, settings.getNumberRange(), lockManager, errorCallback, System.out));
			caller.start();
			Player player = new Player(1, gameData, settings, lockManager, errorCallback, System.out);
			new Thread(player).start();
			while (!gameData.isDone()) {
				lockManager.resetNextCommandLatch();
				gameData.sendNCommand();
				lockManager.nextCommandLatchAwait();
			}
		} catch (BingoException e) {
			assertTrue(false);
		}
	}

}
