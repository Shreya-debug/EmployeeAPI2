package com.ezest.employee.exceptionHandler;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String exceptionDetail;
	
	
	public CustomException(String exceptionDetail) {
		super();
		this.exceptionDetail = exceptionDetail;
	}
	
	public String getExceptionDetail() {
		return exceptionDetail;
	}
	
	public void setExceptionDetail(String exceptionDetail) {
		this.exceptionDetail = exceptionDetail;
	}
	
	
}
