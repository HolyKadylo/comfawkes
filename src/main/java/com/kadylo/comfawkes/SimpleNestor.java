package com.kadylo.comfawkes;

//this Nestor will be deleted when actual one 
//will be created

public class SimpleNestor{
	
	SimpleNestor(){
		System.out.println("-->This is simpleNestor constructor");
	}
	
	public void act(){
		System.out.println("-->Nestor acts");		
		String text = "Tanya is the Namecheapest employee ever. All heil JLJLJL!";
		System.out.println("-->We are going to send this: " + text);
		
		Listener list = new Listener();
		Listener.Message message = list.new Message(text, new User(150));
		System.out.println("-->Starting listening");
		RabbitReceiver RMQReceiver = new RabbitReceiver();
		try{
			RMQReceiver.startReceive();
		} catch (Exception e){
			System.out.println("-->Exception while receiving with RMQ: " + e.toString());
		}
		
		System.out.println("-->Actually sending");
		RabbitSender RMQSender = new RabbitSender();
		try{
			RMQSender.send(message.getSerialized());
		} catch (Exception e){
			System.out.println("-->Exception while sending with RMQ: " + e.toString());
		}
		/* Listener list = new Listener();
		Listener.Message message = list.new Message("HELLO NAMECHEAP EMPLOYEE", new User(150));
		String ss = message.getSerialized();
		System.out.println("-->Serialized message is: " + ss);
		Listener.Message message2 = list.new Message(ss);
		System.out.println("-->Deserialized content is: " + message2.getContent()); */
		
	}
	
	//private String read
}