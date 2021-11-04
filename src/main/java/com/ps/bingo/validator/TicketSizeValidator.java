package com.ps.bingo.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ps.bingo.Constants;
import com.ps.bingo.game.GameSettings;

public class TicketSizeValidator implements SettingValidator<String> {
	static final Logger logger = LogManager.getLogger(TicketSizeValidator.class.getName());

	private final int numberRange;
	
	public TicketSizeValidator(final int numberRange) {
		this.numberRange = numberRange;
	}
	@Override
	public ResultMessage validate(final String t) {
		return validateInternal(t);
	}

	@Override
	public ResultMessage validate(final GameSettings t) {
		return validateInternal(t.getTicketRowSize() + "X" + t.getTicketColumnSize());
	}

	private boolean isValid(final String s) {
		if (s == null)
			return false;
		String[] rowsAndCols = s.split("X");
		if (rowsAndCols.length != 2)
			return false;
		try {
			int r = Integer.parseInt(rowsAndCols[0]);
			int c = Integer.parseInt(rowsAndCols[1]);
			if (r > 0 && r <= 100 && c > 0 && c <= 100 && r <= numberRange)
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private ResultMessage validateInternal(final String s) {
		if (isValid(s))
			return new ResultMessage(true);
		else {
			logger.error( ValidatorMessageBundle.INVALID_TICKET_SIZE.formatWithErrorCode(s, Constants.MAX_TICKET_ROW_COLUMN_SIZE, numberRange));
			return new ResultMessage(false, ValidatorMessageBundle.INVALID_TICKET_SIZE.format(s, Constants.MAX_TICKET_ROW_COLUMN_SIZE, numberRange));
		}
	}

}
