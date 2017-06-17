package com.kadylo.comfawkes;

// This represents our identity
public class Account{
	private String email;
	private String password;
	private String phoneNo;
	private Node node;
	public enum Role{
		POSTER, LISTENER//, AMBIVALENT
	}
	private Role role;
	
	public Account (String email, 
		String password, 
		String phoneNo, 
		Node node, 
		Role role){
		
		this(email, password, phoneNo, role);
		this.setNode(node);
	}
	
	public Account (String email,
		String password,
		String phoneNo,
		Role role){
	
		System.out.println("-->Constructing account");
		this.email = email;
		this.password = password;
		this.phoneNo = phoneNo;
		this.role = role;
		System.out.println("-->Account " + phoneNo + " is constructed");
	}
	
	//Setters & Getters
	public void setNode(Node node){
		this.node = node;
	}
	/* public void setEmail(String email){
		this.email = email;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public void setPhoneNo(String phoneNo){
		this.phoneNo = phoneNo;
	} */
	public Node getNode(){
		return node;
	}
	public String getEmail(){
		return email;
	}
	public String getPassword(){
		return password;
	}
	public String getPhoneNo(){
		return phoneNo;
	}
}