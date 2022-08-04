package com.servicebus.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@SpringBootApplication
public class ServiceBusQueueApplication {

	/**
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServiceBusQueueApplication.class, args);
	}

}
