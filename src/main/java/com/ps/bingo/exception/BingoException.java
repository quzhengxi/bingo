package com.ps.bingo.exception;

public class BingoException extends Exception{
	public BingoException(String message) {
		super(message);
	}

	public BingoException(Exception e) {
		super(e);
	}
	public BingoException(String message, Exception e) {
		super(message, e);
	}

	private static final long serialVersionUID = -5721247730335762935L;
 
}
