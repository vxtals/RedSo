package server;

import java.io.Serializable;
public class Identifier implements Serializable {
	
	private static final long serialVersionUID = 8841503506406043033L;

	private Long id;
	
	private String email;
	private String password;
	
	public Identifier() {
		// this form used by Hibernate
	}
	
	public Identifier(String email, String password){
		this.email = email;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
