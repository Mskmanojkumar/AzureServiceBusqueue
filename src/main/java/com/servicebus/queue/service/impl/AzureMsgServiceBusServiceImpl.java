package com.servicebus.queue.service.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusFailureReason;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.servicebus.queue.config.AppConfig;
import com.servicebus.queue.service.AzureMsgServiceBusService;

import lombok.extern.log4j.Log4j2;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@Service
@Log4j2
public class AzureMsgServiceBusServiceImpl implements AzureMsgServiceBusService {

	@Autowired
	private static String msg;

	/**
	 * Send messages to a queue
	 */
	@Override
	public String sendMessage(String msg) {

		if (StringUtils.isNotBlank(msg)) {

			// create a Service Bus Sender client for the queue
			ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
					.connectionString(AppConfig.getConnectionString()).sender().queueName(AppConfig.getQueueName())
					.buildClient();

			// send one message to the queue
			senderClient.sendMessage(new ServiceBusMessage(msg));
			System.out.println("Sent a single message to the queue: " + AppConfig.getQueueName());

		}
		return "updated msg to Queue Sucessfully" + AppConfig.getQueueName();

	}

	/**
	 * Receive messages from a queue
	 */
	@Override
	public String receiveMessages() {
		CountDownLatch countdownLatch = new CountDownLatch(1);

		// Create an instance of the processor through the ServiceBusClientBuilder
		ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
				.connectionString(AppConfig.getConnectionString()).processor().queueName(AppConfig.getQueueName())
				.processMessage(AzureMsgServiceBusServiceImpl::processMessage)
				.processError(context -> processError(context, countdownLatch)).buildProcessorClient();

		log.info("Starting the processor");
		processorClient.start();

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("Stopping and closing the processor");
		processorClient.close();
		return msg;

	}

	/**
	 * @param context processMessage method to process a message received from the
	 *                Service Bus subscription.
	 */
	private static void processMessage(ServiceBusReceivedMessageContext context) {
		ServiceBusReceivedMessage message = context.getMessage();
		log.info("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
				message.getSequenceNumber(), message.getBody());

		msg = message.getBody().toString();

	}

	/**
	 * @param context        hold the service bus error context
	 * @param countdownLatch CountDownLatch is initialized with a given count.The
	 *                       await methods block until the current count reacheszero
	 *                       due to invocations of the countDown method, after
	 *                       whichall waiting threads are released and any
	 *                       subsequent invocations of await return immediately.
	 *                       This is a one-shot phenomenon-- the count cannot be
	 *                       reset. If you need a version that resets thecount,
	 *                       consider using a CyclicBarrier.
	 */
	private static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
		log.info("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
				context.getFullyQualifiedNamespace(), context.getEntityPath());

		if (!(context.getException() instanceof ServiceBusException)) {
			log.info("Non-ServiceBusException occurred: %s%n", context.getException());
			return;
		}

		ServiceBusException exception = (ServiceBusException) context.getException();
		ServiceBusFailureReason reason = exception.getReason();

		if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
				|| reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
				|| reason == ServiceBusFailureReason.UNAUTHORIZED) {
			log.info("An unrecoverable error occurred. Stopping processing with reason %s: %s%n", reason,
					exception.getMessage());

			countdownLatch.countDown();
		} else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
			log.info("Message lock lost for message: %s%n", context.getException());
		} else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
			try {
				// Choosing an arbitrary amount of time to wait until trying again.
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				System.err.println("Unable to sleep for period of time");
			}
		} else {
			log.info("Error source %s, reason %s, message: %s%n", context.getErrorSource(), reason,
					context.getException());
		}
	}
}
