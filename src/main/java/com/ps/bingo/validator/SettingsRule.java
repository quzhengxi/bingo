package com.ps.bingo.validator;

import java.util.ArrayList;
import java.util.Arrays;

import com.ps.bingo.game.GameSettings;

public class SettingsRule extends ArrayList<SettingValidator> {

	/**
	 * Initalize rule of settings, validators need to given with proper order.
	 * @param chain
	 */
    public SettingsRule(final SettingValidator<?> ... chain) {
        addAll(Arrays.asList(chain));
    }

    public ResultMessage validate(final GameSettings target) {
    	ResultMessage result = new ResultMessage();
        for (SettingValidator rule : this) {
            ResultMessage r = rule.validate(target);
            if (!r.isSuccess()) {
            	result.setSuccess(false);
            	result.appendErrorMessage(r.getErrorMessage());
            }
        }
        return result;
    }

}