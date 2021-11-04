package com.ps.bingo.winning;

import com.ps.bingo.game.Ticket;

public class FullHousePrize implements Prize {

	@Override
	public WinningCombination getName() {
		return WinningCombination.FULLHOUSE;
	}

	@Override
	public boolean check(Ticket ticket) {
		return ticket.isFullHouse();
	}

	@Override
	public boolean isWinningPoint() {
		return true;
	}

	@Override
	public int priority() {
		return 1;
	}

}
