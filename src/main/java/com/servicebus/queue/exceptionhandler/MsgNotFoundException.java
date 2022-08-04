package com.servicebus.queue.exceptionhandler;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
public class MsgNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MsgNotFoundException(String message, Throwable cause) {
		super(message, cause);

	}

	public MsgNotFoundException(String message) {
		super(message);

	}

	public MsgNotFoundException(Throwable cause) {
		super(cause);

	}

}
