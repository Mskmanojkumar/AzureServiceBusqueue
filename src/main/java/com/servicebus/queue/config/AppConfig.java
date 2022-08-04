package com.servicebus.queue.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@Configuration
@Log4j2
public class AppConfig {

	private static String connectionString;

	private static String queueName;

	/**
	 * @param connectionString get the data from property file
	 */
	@Value("${connection-string}")
	public void setConnectionString(String connectionString) {
		AppConfig.connectionString = connectionString;
	}

	/**
	 * @param queueName get the data from property file
	 */
	@Value("${queue-name}")
	public void setQueueName(String queueName) {
		AppConfig.queueName = queueName;
	}

	/**
	 * @return connection string
	 */
	public static String getConnectionString() {

		log.info("Connection String " + connectionString);
		return connectionString;
	}

	/**
	 * @return Queue Name
	 */
	public static String getQueueName() {

		log.info("Queue Name" + queueName);
		return queueName;
	}

}
