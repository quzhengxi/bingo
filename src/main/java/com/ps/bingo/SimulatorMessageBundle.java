package com.ps.bingo;

import com.ps.bingo.localized.BundleMessage;
import com.ps.bingo.localized.MessageBundle;

public class SimulatorMessageBundle extends MessageBundle {

	static {
		MessageBundle.initializeMessages(SimulatorMessageBundle.class);
	}

	private SimulatorMessageBundle() {
	}

	public static final String BUNDLE_NAME = "com.ps.bingo.Resources";

	static public BundleMessage WELCOME;
	static public BundleMessage WELCOME_NOTE;

	static public BundleMessage NUMER_RANGE_FIELD;
	static public BundleMessage PLAYER_NUMBER_FIELD;
	static public BundleMessage TICKET_SIZE_FIELD;
	static public BundleMessage NUMBER_PER_ROW_FIELD;

	static public BundleMessage NUMER_RANGE_PROMPT;
	static public BundleMessage PLAYER_NUMBER_PROMPT;
	static public BundleMessage TICKET_SIZE_PROMPT;
	static public BundleMessage NUMBER_PER_ROW_PROMPT;

	static public BundleMessage NEXT_PROMPT;

	static public BundleMessage EXIT;
	static public BundleMessage INVALID_INPUT;

	static public BundleMessage INTERNAL_ERROR;
	static public BundleMessage NEXT_NUMBER;
	static public BundleMessage GAME_ID;
	static public BundleMessage GAME_OVER;
	static public BundleMessage SEPARATOR;
	static public BundleMessage PLAYER_WINNING;

	static public BundleMessage SET_LOG_LEVEL_ERROR;
	static public BundleMessage RUN_SIMULATOR_ERROR;
}
