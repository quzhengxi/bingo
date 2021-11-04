package com.ps.bingo.game;

public class GameSettings {

	private int numberRange;
	private int playerNumber;
	private int ticketRowSize;
	private int ticketColumnSize;
	private int numberPerRow;

	public GameSettings() {
	}

	public GameSettings(final int numberRange, final int playerNumber, final int ticketRowSize,
			final int ticketColumnSize, final int numberPerRow) {
		this.numberRange = numberRange;
		this.playerNumber = playerNumber;
		this.ticketRowSize = ticketRowSize;
		this.ticketColumnSize = ticketColumnSize;
		this.numberPerRow = numberPerRow;
	}

	public int getNumberRange() {
		return numberRange;
	}

	public void setNumberRange(final  int numberRange) {
		this.numberRange = numberRange;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(final int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getTicketRowSize() {
		return ticketRowSize;
	}

	public void setTicketRowSize(final  int ticketRowSize) {
		this.ticketRowSize = ticketRowSize;
	}

	public int getTicketColumnSize() {
		return ticketColumnSize;
	}

	public void setTicketColumnSize(final int ticketColumnSize) {
		this.ticketColumnSize = ticketColumnSize;
	}

	public int getNumberPerRow() {
		return numberPerRow;
	}

	public void setNumberPerRow(final int numberPerRow) {
		this.numberPerRow = numberPerRow;
	}

	public String toString() {
		return new StringBuilder().append("numberRange: ").append(numberRange).append(", playerNumber: ")
				.append(playerNumber).append(", ticketRowSize: ").append(ticketRowSize).append(", ticketColumnSize: ")
				.append(ticketColumnSize).append(", numberPerRow:").append(numberPerRow).toString();
	}
}
