package com.ps.bingo.winning;

import com.ps.bingo.game.Ticket;

public interface Prize {
	/**
	 * Get display name of prize
	 * @return a string of display name
	 */
	public default String getDisplayName() {
		return getName().toDisplayName();
	}
	
	/**
	 * Get prize name
	 * @return a enum
	 */
	public WinningCombination getName();
	
	/**
	 * 
	 * @param ticket
	 * @return
	 */
	public boolean check(Ticket ticket);
	
	/**
	 * Get if prize is a winning point
	 * @return a boolean
	 */
	public boolean isWinningPoint();
	
	/**
	 * Get priority of prize, 1 is the highest priority.
	 * @return a integer of prize's priority
	 */
	public int priority();
}
