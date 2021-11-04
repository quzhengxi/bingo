package com.ps.bingo.winning;

import com.ps.bingo.game.Ticket;

public class EarlyFivePrize implements Prize{
	@Override
	public WinningCombination getName() {
		return WinningCombination.EARLYFIVE;
	}

	@Override
	public boolean check(Ticket ticket) {
		return ticket.isEarlyFive();
	}

	@Override
	public boolean isWinningPoint() {
		return false;
	}

	@Override
	public int priority() {
		return 1;
	}
}
