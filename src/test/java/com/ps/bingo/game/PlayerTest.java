package com.ps.bingo.game;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.ps.bingo.Simulator;
import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;
import com.ps.bingo.winning.WinningCombination;

public class PlayerTest extends CommonTest {

	@Test
	public void testPlayer() {
		GameData gameData = GameData.getInstance();
		GameSettings settings = new GameSettings(NUMBER_RANGE, PLAYER_NUMBER_1, TICKET_ROW_SIZE, TICKET_COLUMN_SIZE,
				NUMBER_PER_ROW);
		LockManager lockManager = new LockManager(settings.getPlayerNumber());
		Simulator sim = mock(Simulator.class);
		ErrorCallback errorCallback = new ErrorCallback(sim);
		try {
			Player player = new Player(1, gameData, settings, lockManager, errorCallback, System.out);
			new Thread(player).start();
			TicketUtils.verifyTicket(player.getTicket(), settings);

			int[][] ticketMatrix = TicketUtils.getTicketMatrix(player.getTicket(), settings);
			assertNotNull(ticketMatrix);
			for (int r = 0; r < TICKET_ROW_SIZE; r++) {
				for (int c = 0; c < TICKET_COLUMN_SIZE; c++) {
					if (ticketMatrix[r][c] != 0 && ticketMatrix[r][c] != -1) {
						gameData.addAnnouncedNumbers(ticketMatrix[r][c]);
						lockManager.playerBarrierAwait();
						lockManager.playerBarrierAwait();
						int[][] updatedTicketMatrix = TicketUtils.getTicketMatrix(player.getTicket(), settings);
						assertEquals(-1, updatedTicketMatrix[r][c]);
					}
				}
				switch (r) {
				case 0:
					assertTrue(gameData.isWinningAnnounced(PLAYER_NUMBER, WinningCombination.TOPLINE));
					break;
				case 1:
					assertTrue(gameData.isWinningAnnounced(PLAYER_NUMBER, WinningCombination.EARLYFIVE));
					break;
				case 2:
					assertTrue(gameData.isWinningAnnounced(PLAYER_NUMBER, WinningCombination.FULLHOUSE));
					break;
				}
			}
			assertTrue(gameData.isDone());
		} catch (BingoException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testPlayer_100() {
		GameData gameData = GameData.getInstance();
		GameSettings settings = new GameSettings(NUMBER_RANGE, PLAYER_NUMBER_100, TICKET_ROW_SIZE, TICKET_COLUMN_SIZE,
				NUMBER_PER_ROW);
		LockManager lockManager = new LockManager(settings.getPlayerNumber());
		Simulator sim = mock(Simulator.class);
		ErrorCallback errorCallback = new ErrorCallback(sim);
		try {
			for (int i = 1; i <= PLAYER_NUMBER_100; i++) {
				Player player = new Player(i, gameData, settings, lockManager, errorCallback, System.out);
				new Thread(player).start();
				TicketUtils.verifyTicket(player.getTicket(), settings);
			}
			for (int i = 1; i <= NUMBER_RANGE; i++) {
				gameData.addAnnouncedNumbers(i);
				lockManager.playerBarrierAwait();
				lockManager.playerBarrierAwait();
				if (gameData.isDone())
					break;
			}

			assertTrue(gameData.isDone());
		} catch (BingoException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testPlayer_interrupted() {
		GameData gameData = GameData.getInstance();
		GameSettings settings = new GameSettings(NUMBER_RANGE, PLAYER_NUMBER_1, TICKET_ROW_SIZE, TICKET_COLUMN_SIZE,
				NUMBER_PER_ROW);
		LockManager lockManager = new LockManager(settings.getPlayerNumber());
		ErrorCallback errorCallback = mock(ErrorCallback.class);
		Mockito.doAnswer(invocation -> {
			Throwable t = (Throwable) invocation.getArguments()[0];
			if (t != null)
				assertEquals(GameMessageBundle.WAIT_PLAYER_BARRIER_ERROR.format(), t.getMessage());
			return null;
		}).when(errorCallback).onError(Mockito.any());
		try {
			Player player = new Player(1, gameData, settings, lockManager, errorCallback, System.out);
			Thread thread = new Thread(player);
			thread.start();
			TicketUtils.verifyTicket(player.getTicket(), settings);
			thread.interrupt();
			Thread.sleep(50);
			assertFalse(gameData.isDone());
		} catch (BingoException | InterruptedException e) {
			assertTrue(false);
		}
	}
}
