package com.kadylo.comfawkes;

import com.rabbitmq.client.*;
import java.io.IOException;

public class RabbitReceiver {
	
	// particular class that it serves to
	private Object master = null;
	private String RMQ_COOKIE = "";
	private int port = 0;
	private String host = "";
	private String QUEUE_NAME = "";

	// constructor
	RabbitReceiver(Object master, String cookie, int port, String QUEUE_NAME, String host){
		this.master = master;
		RMQ_COOKIE = cookie;
		this.port = port;
		this.host = host;
		this.QUEUE_NAME = QUEUE_NAME;
	}

	public void startReceive() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println("-->Waiting for messages");

		Consumer consumer = new DefaultConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, 
				Envelope envelope, 
				AMQP.BasicProperties properties, 
				byte[] body) throws IOException {
					
				String message = new String(body, "UTF-8");
				System.out.println("-->Received '" + message + "'");
				decode(message);
				handleMessage(message);
			}
		};
	channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
	private void decode (String s){
		Listener l = new Listener();
		Listener.Message msg = l.new Message(s);
		System.out.println("-->Decoded: " + msg.getContent());
	}
	
	private void handleMessage(String message){
		
		// do something: instruction+role+email+password+publicAddress+publicId+port
		// created node listener at RMQ address: 10+id+address
		// created node poster at RMQ address: 11+id+address
		// node logged in and ready to operate: 12+address
		if (master instanceof SimpleNestor){
			System.out.println("-->Message for SimpleNestor");
		}
		
		// post:
		if (master instanceof Listener){
			System.out.println("-->Message for listener");
		}
		
		// post:
		if (master instanceof Poster){
			System.out.println("-->Message for poster");
		}
	}
}
