package com.ps.bingo.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.Constants;
import com.ps.bingo.game.GameSettings;

public class NumberPerRowValidator implements SettingValidator<String> {
	static final Logger logger = LogManager.getLogger(NumberPerRowValidator.class.getName());

	private final int ticketRowSize, ticketColumnSize, numberRange;
	public NumberPerRowValidator(int ticketRowSize, int ticketColumnSize, int numberRange) {
		this.ticketColumnSize = ticketColumnSize;
		this.ticketRowSize = ticketRowSize;
		this.numberRange = numberRange;
	}

	@Override
	public ResultMessage validate(String t) {
		try {
			return validateInternal(Integer.parseInt(t));
		} catch (NumberFormatException e) {
			logger.error(ValidatorMessageBundle.INVALID_NUMBER_PER_ROW.formatWithErrorCode(t, Constants.MAX_NUMBER_PER_ROW, ticketColumnSize));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_NUMBER_PER_ROW.format(t, Constants.MAX_NUMBER_PER_ROW, ticketColumnSize) );
		}
	}

	@Override
	public ResultMessage validate(GameSettings t) {
		return validateInternal(t.getNumberPerRow());
	}
	
	private boolean isValid(Integer t) {
		if(t == null)
			return false;
		if(t > 0 && t <= 100 && t <= ticketColumnSize) {
			return true;
		}
		return false;
	}

	private boolean isRangeNumberEnough(Integer t) {
		if(t * ticketRowSize <= numberRange)
			return true;
		return false;
	}
	
	private ResultMessage validateInternal(Integer t) {
		if (isValid(t)) {
			if(isRangeNumberEnough(t))
				return new ResultMessage(true);
			else {
				logger.error(ValidatorMessageBundle.INVALID_NUMBER_PER_ROW_NO_ENOUGH_RANGE.formatWithErrorCode(numberRange/ticketRowSize));
				return new ResultMessage(false, ValidatorMessageBundle.INVALID_NUMBER_PER_ROW_NO_ENOUGH_RANGE.format(numberRange/ticketRowSize) );
			}
		}
		else {
			logger.error(ValidatorMessageBundle.INVALID_NUMBER_PER_ROW.formatWithErrorCode(t, Constants.MAX_NUMBER_PER_ROW, ticketColumnSize));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_NUMBER_PER_ROW.format(t, Constants.MAX_NUMBER_PER_ROW, ticketColumnSize) );
		}
	}
}
