package com.ps.bingo.game;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.SimulatorMessageBundle;
import com.ps.bingo.exception.BingoException;
import com.ps.bingo.exception.ErrorCallback;
import com.ps.bingo.validator.ResultMessage;
import com.ps.bingo.validator.SettingsValidator;
import com.ps.bingo.winning.WinningCombination;

public class Game {
	static final Logger logger = LogManager.getLogger(Game.class.getName());

	private final GameSettings settings;
	private final GameData gameData = GameData.getInstance();
	private final Player[] players;
	
	public Game(GameSettings settings) {
		this.settings = settings;
		players = new Player[settings.getPlayerNumber()];
	}

	public Game buildAndStart(LockManager lockManager, ErrorCallback errorCallback, PrintStream out) throws BingoException {
		ResultMessage validateSettings = this.validateSettings();
		if (!validateSettings.isSuccess())
			throw new BingoException(validateSettings.getErrorMessage());
		Thread caller = new Thread(new Caller(gameData, this.settings.getNumberRange(), lockManager, errorCallback, out));
		caller.start();
		for (int i = 1; i <= settings.getPlayerNumber(); i++) {
			players[i-1] = new Player(i, gameData, settings, lockManager, errorCallback, out);
			new Thread(players[i-1]).start();
		}
		return this;
	}

	public boolean isDone() {
		return gameData.isDone();
	}

	public void sendNextCommand() throws BingoException {
		this.gameData.sendNCommand();
	}

	public void printResult(PrintStream out) {
		printAndLog(out, "\n\n\n");
		printAndLog(out, SimulatorMessageBundle.GAME_OVER.format());
		printAndLog(out, SimulatorMessageBundle.SEPARATOR.format());
		
		ConcurrentHashMap<Integer, List<WinningCombination>> winners = this.gameData.getWinners();
		for(int playerId=1; playerId <= this.settings.getPlayerNumber(); playerId++) {
			if(winners.get(playerId) != null && winners.get(playerId).size() > 0) {
				StringBuilder prizes = new StringBuilder();
				winners.get(playerId).stream().forEach(v -> {
					if(prizes.length() != 0) 
						prizes.append(", "); 
					prizes.append(v.toDisplayName());
					});
				printAndLog(out, SimulatorMessageBundle.PLAYER_WINNING.format(playerId, prizes.toString()));

			}else
				printAndLog(out, SimulatorMessageBundle.PLAYER_WINNING.format(playerId, WinningCombination.NOTHING.toDisplayName()));
		}
		if(logger.isDebugEnabled()) {
			for(Player player : players) {
				logger.debug(GameMessageBundle.SEPARATOR_PLAYER_ID.format(player.getPlayerId()));
				logger.debug("\n" + player.getTicket());
			}
			CopyOnWriteArrayList<Integer> announcedNumbers = gameData.getAnnouncedNumbers();
			logger.debug("AnnouncedNumbers: " + announcedNumbers.toString());
		}		
	}

	private ResultMessage validateSettings() {
		SettingsValidator v = new SettingsValidator(this.settings);
		return v.validate();
	}

	private void printAndLog(PrintStream out, String msg) {
		out.println(msg);
		logger.debug(msg);
	}
}
