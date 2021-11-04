package com.ps.bingo.game;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;
import com.ps.bingo.winning.WinningChecker;
import com.ps.bingo.winning.WinningCombination;

public class Player implements Runnable {
	static final Logger logger = LogManager.getLogger(Player.class.getName());

	private final int playerId;
	private final Ticket ticket;
	private final GameData gameData;
	private final LockManager lockManager;
	private final ErrorCallback errorCallback;
	private final PrintStream out;

	private final WinningChecker winningChecker;
	private final Set<WinningCombination> claimedPrizes = new HashSet<>();

	public Player(final int playerId, final GameData gameData, final GameSettings settings,
			final LockManager lockManager, final ErrorCallback errorCallback, final PrintStream out)
			throws BingoException {
		this.playerId = playerId;
		this.gameData = gameData;
		this.out = out;
		this.lockManager = lockManager;
		this.errorCallback = errorCallback;
		this.winningChecker = WinningChecker.getInstance();
		this.ticket = TicketGenerator.generateTicket(settings);
	}

	@Override
	public void run() {

		try {
			while (!gameData.isDone()) {
				lockManager.playerBarrierAwait();

				Integer latestAnnouncedNumber = gameData.getLatestAnnouncedNumber();
				if (ticket.removeNumber(latestAnnouncedNumber)) {
					List<WinningCombination> winnings = winningChecker.check(ticket);
					if(winnings != null && !winnings.isEmpty()) {
						for(WinningCombination w : winnings) {
							if(gameData.addWinner(playerId, w)) {
								printAndLogWinner(w);
								claimedPrizes.add(w);
								break;
							}
						}
					}
				}
				
				lockManager.playerBarrierAwait();
			}
		} catch (Exception e) {
			errorCallback.onError(e);
		}

	}

	public void printTicket() {
		this.ticket.print();
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getTicket() {
		return this.ticket.toString();
	}

	public Set<WinningCombination> getClaimedPrizes() {
		return claimedPrizes;
	}

	private void printAndLogWinner(WinningCombination winning) {
		out.println(GameMessageBundle.WINNING.format(playerId, winning.toDisplayName()));
		if (logger.isDebugEnabled()) {
			logger.debug(GameMessageBundle.WINNING.format(playerId, winning.toDisplayName()));
			logger.debug("\n" + getTicket());
		}
	}

}
