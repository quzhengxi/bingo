package com.ps.bingo.exception;

import com.ps.bingo.Simulator;

public class ErrorCallback {

	private final Simulator simulator;
	
	public ErrorCallback(Simulator simulator){
		this.simulator = simulator;
	}
	
	public void onError(Throwable t) {
		simulator.exitWithError(t);
	}
}
