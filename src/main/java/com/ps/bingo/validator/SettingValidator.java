package com.ps.bingo.validator;

import com.ps.bingo.game.GameSettings;

public interface SettingValidator<T> {

	/**
	 * Validate given value
	 * @param setting data
	 * @return a result message with validation information
	 */
	public ResultMessage validate(T t);
	
	/**
	 * Validate given game settings
	 * @param game settings
	 * @return a result message with validation information
	 */
	public ResultMessage validate(GameSettings t);
}
