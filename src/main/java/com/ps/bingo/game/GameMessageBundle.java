package com.ps.bingo.game;

import com.ps.bingo.localized.BundleMessage;
import com.ps.bingo.localized.MessageBundle;

public class GameMessageBundle extends MessageBundle {

	static {
		MessageBundle.initializeMessages(GameMessageBundle.class);
	}

	private GameMessageBundle() {
	}

	public static final String BUNDLE_NAME = "com.ps.bingo.game.Resources";

	static public BundleMessage WINNING;
	static public BundleMessage SEPARATOR_PLAYER_ID;
	static public BundleMessage WAIT_PLAYER_BARRIER_ERROR;
	static public BundleMessage WAIT_COMMAND_LATCH_ERROR;
}
