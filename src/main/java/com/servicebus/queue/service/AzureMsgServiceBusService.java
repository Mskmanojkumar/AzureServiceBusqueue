package com.servicebus.queue.service;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
public interface AzureMsgServiceBusService {

	String sendMessage(String msg);

	String receiveMessages();
}
