package com.ps.bingo.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.Constants;
import com.ps.bingo.game.GameSettings;

public class NumberRangeValidator implements SettingValidator<String> {
	static final Logger logger = LogManager.getLogger(NumberRangeValidator.class.getName());

	@Override
	public ResultMessage validate(String t) {
		try {
			return validateInternal(Integer.parseInt(t));
		} catch (NumberFormatException e) {
			logger.error( ValidatorMessageBundle.INVALID_NUMBER_RANGE.formatWithErrorCode(t, Constants.MAX_NUMBER_RANGE));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_NUMBER_RANGE.format(t, Constants.MAX_NUMBER_RANGE) );
		}
	}

	@Override
	public ResultMessage validate(GameSettings t) {
		return validateInternal(t.getNumberRange());
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
			logger.error( ValidatorMessageBundle.INVALID_NUMBER_RANGE.formatWithErrorCode(t, Constants.MAX_NUMBER_RANGE));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_NUMBER_RANGE.format(t, Constants.MAX_NUMBER_RANGE));
		}

	}

}
