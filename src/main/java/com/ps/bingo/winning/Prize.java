package com.ps.bingo.winning;

import com.ps.bingo.game.Ticket;

public interface Prize {
	public default String getDisplayName() {
		return getName().toDisplayName();
	}
	
	public WinningCombination getName();
	public boolean check(Ticket ticket);
	public boolean isWinningPoint();
	public int priority();
}
