package com.ps.bingo.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.exception.BingoException;
import com.ps.bingo.winning.WinningCombination;

public class GameData {
	static final Logger logger = LogManager.getLogger(GameData.class.getName());

	enum Command{
		NEXT;
	}
	private final BlockingQueue<Command> command = new ArrayBlockingQueue<>(1);
	private final ConcurrentHashMap<Integer, List<WinningCombination>> winners = new ConcurrentHashMap<>();
	private final CopyOnWriteArrayList<Integer> announcedNumbers = new CopyOnWriteArrayList<>();
	private volatile boolean done = false;
	
	private final ReadWriteLock winningLock = new ReentrantReadWriteLock();
    private final Lock rlock = winningLock.readLock();
    private final Lock wlock = winningLock.writeLock();
    
	private GameData() {}
	public static GameData getInstance() {
		return new GameData();
		/*if (_instance == null) {
			synchronized (GameData.class) {
				if (_instance == null) {
					_instance = new GameData();
				}
			}
		}
		return _instance;*/
	}
	public boolean addWinner(final int playerId, final WinningCombination prize) {
		if(isWinningAnnounced(playerId, prize))
    		return false;
		wlock.lock();
        try {       
        	if(isWinningAnnounced(playerId, prize))
        		return false;
        	
        	if(prize.equals(WinningCombination.FULLHOUSE))
        		done = true;
        	List<WinningCombination> list = winners.get(playerId);
        	if(list == null) {
        		list = new ArrayList<WinningCombination>();
        		winners.put(playerId, list);
        	}
        	list.add(prize);
        } finally {
            wlock.unlock();
        }
        return true;
	}
	public boolean isWinningAnnounced(final int playerId, final WinningCombination prize) {
		rlock.lock();
        try {
        	for(List<WinningCombination> winnings : winners.values()) {
        		if(winnings.contains(prize)) {
        			return true;
        		}
        	}
        } finally {
            rlock.unlock();
        }
        return false;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public ConcurrentHashMap<Integer, List<WinningCombination>> getWinners() {
		return winners;
	}
	public void sendNCommand() throws BingoException{
		try {
			this.command.put(Command.NEXT);
		} catch (InterruptedException e) {
			logger.error(e);
			throw new BingoException("Failed to send next command.", e);
		}
	}
	
	public Command takeNCommand() throws BingoException {
		try {
			return this.command.take();
		} catch (InterruptedException e) {
			logger.error(e);
			throw new BingoException("Failed to take next command.", e);
		}
	}
	
	public CopyOnWriteArrayList<Integer> getAnnouncedNumbers() {
		return announcedNumbers;
	}
	
	public Integer getLatestAnnouncedNumber() {
		return announcedNumbers.get(announcedNumbers.size()-1);
	}
	public void addAnnouncedNumbers(int number) {
		announcedNumbers.add(number);
	}

}
