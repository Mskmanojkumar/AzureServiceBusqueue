package com.servicebus.queue.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@ControllerAdvice
public class RestExceptionHandler {

	/**
	 * @ExceptionHandler - Explore the Method is Exception Handler Method
	 * @param exc Exception type to handle/catch
	 * @return type of the response body
	 */
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(MsgNotFoundException exc) {

		// create a ErrorResponse
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
		errorResponse.setMessage(exc.getMessage());
		errorResponse.setTimeStamp(System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	/**
	 * @param exc get the message
	 * @return error response and http status
	 */
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
