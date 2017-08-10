package com.kadylo.comfawkes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitSender {

	private final static String QUEUE_NAME = "hello";

	public void send(String message) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbithost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
		System.out.println("-->RMQ sent: '" + message + "'");

		channel.close();
		connection.close();
	}
}
