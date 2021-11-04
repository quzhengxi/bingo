package com.ps.bingo.validator;

import java.util.ArrayList;
import java.util.Arrays;

import com.ps.bingo.game.GameSettings;

public class SettingsRule extends ArrayList<SettingValidator> {

    public SettingsRule(SettingValidator<?> ... chain) {
        addAll(Arrays.asList(chain));
    }

    public ResultMessage validate(GameSettings target) {
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