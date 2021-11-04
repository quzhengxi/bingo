package com.ps.bingo.validator;

public class ResultMessage {
	private boolean success = true;
	private String errorMessage = "";
	public ResultMessage() {}
	public ResultMessage(boolean success) {
		this.success = success;
	}

	public ResultMessage(boolean success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
 
	public void appendErrorMessage(String errorMessage) {
		if(errorMessage!= null && errorMessage.trim().length() > 0)
			this.errorMessage = this.errorMessage + (this.errorMessage.length() == 0 ? "" : " ") + errorMessage;
	}
	
}
