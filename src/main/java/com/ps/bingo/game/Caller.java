package com.ps.bingo.game;

import java.io.PrintStream;
import java.util.concurrent.BrokenBarrierException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.SimulatorMessageBundle;
import com.ps.bingo.exception.ErrorCallback;
import com.ps.bingo.game.GameData.Command;
import com.ps.bingo.utils.RandomNumberGenerator;

public class Caller implements Runnable {
	static final Logger logger = LogManager.getLogger(Caller.class.getName());

	private final GameData gameData;
	private final LockManager lockManager;
	private final int numberRange;
	private final PrintStream out;
	private final ErrorCallback errorCallback;

	public Caller(final GameData gameData, final int numberRange, final LockManager lockManager,
			final ErrorCallback errorCallback, final PrintStream out) {
		this.gameData = gameData;
		this.numberRange = numberRange;
		this.lockManager = lockManager;
		this.errorCallback = errorCallback;
		this.out = out;
	}

	@Override
	public void run() {

		try {
			while (!gameData.isDone()) {
				Command takeNCommand = gameData.takeNCommand();
				if (takeNCommand == null || !takeNCommand.equals(Command.NEXT))
					continue;
				int nextNumber = RandomNumberGenerator.randInt(1, numberRange, gameData.getAnnouncedNumbers());
				out.println(SimulatorMessageBundle.NEXT_NUMBER.format(nextNumber));

				lockManager.playerBarrierAwait(); // new number has been issued, wait for all players to be ready, and then unblock all players to process current
													// number
				lockManager.playerBarrierAwait(); // wait for all players to process current number
				lockManager.nextCommandLatchCountDown(); // command has been processed, next command can be issued
			}
		} catch (Exception e) {
			errorCallback.onError(e);
		}

	}

}
