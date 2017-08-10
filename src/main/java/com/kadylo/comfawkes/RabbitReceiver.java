package com.kadylo.comfawkes;

import com.rabbitmq.client.*;
import java.io.IOException;

public class RabbitReceiver {

private final static String QUEUE_NAME = "hello";

	public void startReceive() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("rabbithost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("-->Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println("-->Received '" + message + "'");
				decode(message);
			}
		};
	channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
	private void decode (String s){
		Listener l = new Listener();
		Listener.Message msg = l.new Message(s);
		System.out.println("-->Decoded: " + msg.getContent());
	}
	
}
