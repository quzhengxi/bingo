package com.ps.bingo.game;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.exception.BingoException;

public class LockManager {
	static final Logger logger = LogManager.getLogger(LockManager.class.getName());

    //private CountDownLatch callerLatch = null;
    private CountDownLatch nextCommandLatch = null;   
    private CyclicBarrier playerBarrier;
    
    public LockManager(int playerNumber) {
    	//this.callerLatch = new CountDownLatch(playerNumber);
    	this.nextCommandLatch = new CountDownLatch(1);
    	this.playerBarrier = new CyclicBarrier(playerNumber + 1);  //players and caller
    }

    public void playerBarrierAwait() throws BingoException {
    	try {
			this.playerBarrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			logger.error(GameMessageBundle.WAIT_PLAYER_BARRIER_ERROR.formatWithErrorCode(), e);
			throw new BingoException(GameMessageBundle.WAIT_PLAYER_BARRIER_ERROR.format(), e);
		}
    }
	
	public void resetNextCommandLatch() {
		this.nextCommandLatch = new CountDownLatch(1);
	}

	public void nextCommandLatchAwait() throws BingoException {
		try {
			this.nextCommandLatch.await();
		} catch (InterruptedException e) {
			logger.error(GameMessageBundle.WAIT_COMMAND_LATCH_ERROR.formatWithErrorCode(), e);
			throw new BingoException(GameMessageBundle.WAIT_COMMAND_LATCH_ERROR.format(), e);
		}
	}
	
	public void nextCommandLatchCountDown() {
		this.nextCommandLatch.countDown();
	}

	/*public void resetCallerLatch() {
		this.callerLatch = new CountDownLatch(playerNumber);
	}

	public void callerLatchAwait() throws InterruptedException {
		this.callerLatch.await();	
	}
	
	public void callerLatchCountDown() {
		this.callerLatch.countDown();	
	}*/
}
