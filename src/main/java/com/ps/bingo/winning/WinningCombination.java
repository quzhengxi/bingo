package com.ps.bingo.winning;

public enum WinningCombination {
	EARLYFIVE, FULLHOUSE, TOPLINE, NOTHING;

	public String toDisplayName() {
		switch(this) {
			case EARLYFIVE: 
				return "Early Five";
			case FULLHOUSE:
				return "Full House";
			case TOPLINE:
				return "Top Line";
			case NOTHING:
				return "Nothing";
			default:
				return this.name();
		}
	}
}
