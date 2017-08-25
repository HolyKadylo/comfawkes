package com.kadylo.comfawkes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitSender {
	private int port = 0;
	ConnectionFactory factory;
	Connection connection;
	Channel channel;
	
	RabbitSender(String host, int port){
		this.port = port;
		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
	}

	// address -- queue name
	public void send(String address, String message) throws Exception {
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(address, false, false, false, null);
		channel.basicPublish("", address, null, message.getBytes("UTF-8"));
		System.out.println("-->RMQ sent: '" + message + "'");
		channel.close();
		connection.close();
	}
}
