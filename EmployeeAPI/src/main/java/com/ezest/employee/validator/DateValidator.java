package com.ezest.employee.validator;

public interface DateValidator {

	boolean isValid(String dateStr);
	//LocalDate validateAndParseDateJava8(String dateStr, DateTimeFormatter dateFormatter);
}
