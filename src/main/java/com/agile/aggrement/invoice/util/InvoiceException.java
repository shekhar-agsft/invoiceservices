package com.agile.aggrement.invoice.util;

public class InvoiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6231568802011859857L;

	int code;

	String message;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "InvoiceException [code=" + code + ", message=" + message + "]";
	}

	public InvoiceException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public InvoiceException() {

		// TODO Auto-generated constructor stub
	}

}
