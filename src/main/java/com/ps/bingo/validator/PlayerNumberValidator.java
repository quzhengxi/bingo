package com.ps.bingo.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.Constants;
import com.ps.bingo.game.GameSettings;

public class PlayerNumberValidator implements SettingValidator<String> {
	static final Logger logger = LogManager.getLogger(PlayerNumberValidator.class.getName());

	@Override
	public ResultMessage validate(String t) {
		try {
			return validateInternal(Integer.parseInt(t));
		} catch (NumberFormatException e) {
			logger.error( ValidatorMessageBundle.INVALID_PLAYER_NUMBER.formatWithErrorCode(t, Constants.MAX_PLAYER_NUMBER));

			return new ResultMessage(false, ValidatorMessageBundle.INVALID_PLAYER_NUMBER.format(t) );
		}

	}

	@Override
	public ResultMessage validate(GameSettings t) {
		return validateInternal(t.getPlayerNumber());
	}
	
	private boolean isValid(Integer t) {
		if (t == null)
			return false;
		if (t > 0 && t <= 100)
			return true;
		return false;
	}

	private ResultMessage validateInternal(Integer t) {
		if (isValid(t))
			return new ResultMessage(true);
		else {
			logger.error( ValidatorMessageBundle.INVALID_PLAYER_NUMBER.formatWithErrorCode(t, Constants.MAX_PLAYER_NUMBER));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_PLAYER_NUMBER.format(t, Constants.MAX_PLAYER_NUMBER));
		}

	}



}
