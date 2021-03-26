package com.ezest.employee.exceptionHandler;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;





@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException ex, WebRequest request){
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(),ex.getExceptionDetail(),request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?>  resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getExceptionDetail()+" - "+ex.getFieldValue(), request.getDescription(false));
        //System.out.println(errorDetails);
    	return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>  globleExcpetionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(MissingHeaderInfoException.class)
	public final ResponseEntity<ErrorDetails> handleInvalidTraceIdException
						(MissingHeaderInfoException ex, WebRequest request) {
	
		ErrorDetails error = new ErrorDetails(new Date(),ex.getMessage(), ex.getLocalizedMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException constraintViolationException) {
    	
    	//ErrorDetails errorDetails = new ErrorDetails(new Date(),constraintViolationException.getMessage()
       
    	return new ResponseEntity<>(constraintViolationException.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?>  handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getName() + " should be of type " + ex.getRequiredType().getName(), request.getDescription(false));
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
   
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
           ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Failed",
               ex.getBindingResult().toString());
           return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    } 
}