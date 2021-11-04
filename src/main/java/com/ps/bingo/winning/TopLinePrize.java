package com.ps.bingo.winning;

import com.ps.bingo.game.Ticket;

public class TopLinePrize implements Prize{
	@Override
	public WinningCombination getName() {
		return WinningCombination.TOPLINE;
	}

	@Override
	public boolean check(Ticket ticket) {
		return ticket.isTopLine();
	}

	@Override
	public boolean isWinningPoint() {
		return false;
	}

	@Override
	public int priority() {
		return 2;
	}
}