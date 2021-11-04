package com.ps.bingo.game;

import java.util.HashSet;
import java.util.Set;

import com.ps.bingo.exception.BingoException;
import com.ps.bingo.utils.RandomNumberGenerator;
import com.ps.bingo.validator.ResultMessage;
import com.ps.bingo.validator.SettingsValidator;

public class TicketGenerator {

	public static Ticket generateTicket(GameSettings settings) throws BingoException {
		ResultMessage validateResult = validateSettings(settings);
		if(!validateResult.isSuccess())
			throw new BingoException(validateResult.getErrorMessage());
		Set<Integer> pickedNumbers = new HashSet<>();
		Ticket t = new Ticket(settings.getTicketRowSize(), settings.getTicketColumnSize());
		int numerPerRow = settings.getNumberPerRow();
		for(int r=0; r<settings.getTicketRowSize(); r++) {
			Set<Integer> pickedCols = new HashSet<>();
			for(int j=0; j < numerPerRow; j++ ) {
				int c = RandomNumberGenerator.randInt(0, settings.getTicketColumnSize()-1, pickedCols);
				t.setNumber(r, c, RandomNumberGenerator.randInt(1, settings.getNumberRange(), pickedNumbers));
			}
		}
		return t;
	}
	
	private static ResultMessage validateSettings(GameSettings settings) {
		SettingsValidator v = new SettingsValidator(settings);
		return v.validate();		
	}
}
