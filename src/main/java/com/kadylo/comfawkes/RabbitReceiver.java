package com.kadylo.comfawkes;

import com.rabbitmq.client.*;
import java.io.IOException;

public class RabbitReceiver {
	
	// particular class that it serves to
	private Object master = null;
	private String RMQ_COOKIE = "";
	private int port = 0;
	private String QUEUE_NAME = "";

	// constructor
	RabbitReceiver(Object master, String cookie, int port, String QUEUE_NAME){
		this.master = master;
		RMQ_COOKIE = cookie;
		this.port = port;
		this.QUEUE_NAME = QUEUE_NAME;
		
		System.out.println("-->DELETECreated with queue name:" + this.QUEUE_NAME);
	}

	public void startReceive() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
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
		
		System.out.println("-->TODO DELETE: received with queueName " + QUEUE_NAME);
		
		// do something: instruction+role+email+password+publicAddress+publicId+port
		if (master instanceof App){
			System.out.println("-->Message for app");
		}
		
		// post:
		if (master instanceof Listener){
			System.out.println("-->Message for listener");
		}
		
		// post:
		if (master instanceof Poster){
			System.out.println("-->Message for poster");
		}
		
		//RMQCookie+instruction+role+email+password+publicAddress+publicId+port
		//0 instruction
		//1 email
		
		//0 instruction start/stop/reboot
		//1 role listener/poster
		//2 email foo@bar.com / telephoneNo which we treat the same way
		//3 password abc123
		//4 publicAddress https://vv.com/xxxx
		//5 publicId 1234567
		//6 port 5001
		if (message.contains(RMQ_COOKIE)){
			
			// means it is starting message
			String[] parts = message.split("+");
			if (parts[0].equals("reboot")){
	//			app.reboot(parts[1]);
			}
			if (parts[0].equals("start")){
	//			app.start(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
			}
			if (parts[0].equals("stop")){
	//			app.stop(parts[5]);
			}
			return;
		} else {
			
			// handle message to poster or listener further
		}
	}
	
}
