package com.ps.bingo.validator;

import com.ps.bingo.localized.BundleMessage;
import com.ps.bingo.localized.MessageBundle;

public class ValidatorMessageBundle extends MessageBundle {

	static {
		MessageBundle.initializeMessages(ValidatorMessageBundle.class);
	}

	private ValidatorMessageBundle() {
	}

	public static final String BUNDLE_NAME = "com.ps.bingo.validator.Resources";

	static public BundleMessage INVALID_NUMBER_RANGE;
	static public BundleMessage INVALID_PLAYER_NUMBER;
	static public BundleMessage INVALID_TICKET_SIZE;
	static public BundleMessage INVALID_NUMBER_PER_ROW;
	static public BundleMessage INVALID_NUMBER_PER_ROW_NO_ENOUGH_RANGE;
	
	static public BundleMessage INTERNAL_ERROR;

}
