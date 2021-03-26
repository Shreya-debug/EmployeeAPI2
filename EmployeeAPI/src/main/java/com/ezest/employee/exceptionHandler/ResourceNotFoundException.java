package com.ezest.employee.exceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionDetail;
    private Object fieldValue;
   
    public ResourceNotFoundException( String exceptionDetail) {
        super(exceptionDetail);
        this.exceptionDetail = exceptionDetail;
        //this.fieldValue = fieldValue;
    }
    public ResourceNotFoundException( String exceptionDetail, Long fieldValue) {
        super(exceptionDetail+" - "+fieldValue);
        this.exceptionDetail = exceptionDetail;
        this.fieldValue = fieldValue;
    }
    public String getExceptionDetail() {
        return exceptionDetail;
    }
    public Object getFieldValue() {
        return fieldValue;
    }
}