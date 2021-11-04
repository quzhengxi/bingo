package com.ps.bingo.winning;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.game.Ticket;

public class WinningChecker {
	static final Logger logger = LogManager.getLogger(WinningChecker.class.getName());
	
	private static volatile WinningChecker _instance;

	private List<Prize> winningPointPrizes = new ArrayList<Prize>();
	private List<Prize> nonWinningPointPrizes = new ArrayList<Prize>();

	private WinningChecker() {
		winningPointPrizes.add(new FullHousePrize());
		winningPointPrizes.sort((a, b) -> b.priority() - a.priority());
		nonWinningPointPrizes.add(new EarlyFivePrize());
		nonWinningPointPrizes.add(new TopLinePrize());
		nonWinningPointPrizes.sort((a, b) -> a.priority() - b.priority());
		if(logger.isDebugEnabled()) {
			final StringBuilder winningPoints = new StringBuilder("Winning Points: ");
			winningPointPrizes.forEach(a -> {
				winningPoints.append(a.priority()).append(":").append(a.getDisplayName()).append(" ");
			});
			logger.debug(winningPoints.toString());
			final StringBuilder nonWinningPoints = new StringBuilder("Normal Winnings: ");
			nonWinningPointPrizes.forEach(a -> {
				nonWinningPoints.append(a.priority()).append(":").append(a.getDisplayName()).append(" ");
			});
			logger.debug(nonWinningPoints.toString());
		}
	}

	public static WinningChecker getInstance() {
		if (_instance == null) {
			synchronized (WinningChecker.class) {
				if (_instance == null) {
					_instance = new WinningChecker();
				}
			}
		}
		return _instance;
	}

	public List<WinningCombination> check(Ticket ticket) {
		List<WinningCombination> winnings = new ArrayList<>();
		winnings.addAll(winningPointPrizes.stream().filter(a -> a.check(ticket)).map(a -> a.getName())
				.collect(Collectors.toList()));
		winnings.addAll(nonWinningPointPrizes.stream().filter(a -> a.check(ticket)).map(a -> a.getName())
				.collect(Collectors.toList()));
		return winnings;
	}
}
