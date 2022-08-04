package com.servicebus.queue.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicebus.queue.exceptionhandler.MsgNotFoundException;
import com.servicebus.queue.service.AzureMsgServiceBusService;

/**
 * Send messages to and receive messages from Azure Service Bus queues
 * 
 * @author manoj.kumar
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/queue")
public class AzureMsgServiceBusController {

	@Autowired
	private AzureMsgServiceBusService azureMsgServiceBusService;

	/**
	 * @param msg getting from the User
	 * @return result of the sending msg
	 * @throws Exception
	 */
	@PostMapping("/send")
	public ResponseEntity<String> sendMsg(String msg) throws Exception {
		if (StringUtils.isBlank(msg)) {
			throw new Exception("Please Enter the Message");
		}

		return ResponseEntity.ok(azureMsgServiceBusService.sendMessage(msg));
	}

	/**
	 * @return message from azure service bus Queue
	 * @throws Exception if producer msg not found any msg through the
	 */
	@GetMapping("/receive")
	public ResponseEntity<String> receiveMessages() throws Exception {
		try {
			String msg = azureMsgServiceBusService.receiveMessages();
			if (StringUtils.isBlank(msg)) {
				throw new MsgNotFoundException("No Msg Found From the Azure ServiceBus");
			}
			return ResponseEntity.ok(msg);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}
}
