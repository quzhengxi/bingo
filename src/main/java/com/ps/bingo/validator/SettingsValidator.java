package com.ps.bingo.validator;

import com.ps.bingo.game.GameSettings;

public class SettingsValidator {
	private final GameSettings settings;

	public SettingsValidator(final GameSettings settings) {
		this.settings = settings;
	}

	public ResultMessage validate() {
		SettingsRule rule = new SettingsRule(
				new NumberPerRowValidator(settings.getTicketRowSize(), settings.getTicketColumnSize(),
						settings.getNumberRange()),
				new NumberRangeValidator(), new PlayerNumberValidator(), new TicketSizeValidator(settings.getNumberRange()));
		return rule.validate(this.settings);
	}
}
