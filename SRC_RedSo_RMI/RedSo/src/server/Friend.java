package server;


import java.io.Serializable;

import org.hibernate.annotations.*;

import javax.persistence.*;

public class Friend implements Serializable {
	
	private static final long serialVersionUID = 2519385440928838920L;

	@Id
	@GenericGenerator(name="gen",strategy="increment")
	@GeneratedValue(generator="gen")
	private Long id;
	
	private String email_1;
	private String email_2;
	private boolean acepta_1;
	private boolean acepta_2;
	
	public Friend() {
	}
	
	public Friend(String email_1, String email_2, boolean acepta_1, boolean acepta_2){
		this.email_1 = email_1;
		this.email_2 = email_2;
		this.acepta_1 = acepta_1;
		this.acepta_2 = acepta_2;
		
	}
	
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail_1(){
		return this.email_1;
	}
	
	public void setEmail_1(String email_1){
		this.email_1 = email_1;
	}
	
	public String getEmail_2(){
		return this.email_2;
	}
	
	public void setEmail_2(String email_2){
		this.email_2 = email_2;
	}
	
	
	public boolean getAcepta_1(){
		return this.acepta_1;
	}
	
	public void setAcepta_1(boolean acepta_1){
		this.acepta_1 = acepta_1;
	}
	
	public boolean getAcepta_2(){
		return this.acepta_2;
	}
	
	public void setAcepta_2(boolean acepta_2){
		this.acepta_2 = acepta_2;
	}
	
	
}
