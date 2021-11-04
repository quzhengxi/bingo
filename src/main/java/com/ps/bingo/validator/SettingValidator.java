package com.ps.bingo.validator;

import com.ps.bingo.game.GameSettings;

public interface SettingValidator<T> {

	public ResultMessage validate(T t);
	public ResultMessage validate(GameSettings t);
}
