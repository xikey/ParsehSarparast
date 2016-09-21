package com.example.zikey.sarparast.Helpers;

public class MTGConnectToServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8571850561132886198L;

	public MTGConnectToServerException() {
		super("ارتباط با سرور مقدور نیست");
	}

	public MTGConnectToServerException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public MTGConnectToServerException(String detailMessage) {
		super(detailMessage);
	}

	public MTGConnectToServerException(Throwable throwable) {
		super(throwable);
	}

}
