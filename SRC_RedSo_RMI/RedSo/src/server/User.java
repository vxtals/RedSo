package server;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Lob;
public class User implements Serializable{
	/**
	 * 
	 */
	
	@Lob
	@Column(name="foto", nullable=false, columnDefinition="mediumblob")
	
	private static final long serialVersionUID = -3644734156305154229L;

	private Long id;

	private String email;
	private String nombre;
	private String apellidos;
	private byte[] foto;
	private String web;
	private boolean publico;
	private String lastIP;
	private boolean novedad;
	private boolean solicitud;

	public User() {
		// this form used by Hibernate
	}

	public User(String email, String name, String apellidos, byte[] foto, String web, boolean publico, String lastIP, boolean novedad, boolean solicitud ) {
		// for application use, to create new events
		this.email = email;
		this.nombre = name;
		this.apellidos = apellidos;
		this.foto = foto;
		this.web = web;
		this.publico = publico;
		this.lastIP = lastIP;
		this.novedad = novedad;
		this.solicitud = solicitud;
		
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
	
	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	
	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}
	
	public boolean getPublico() {
		return publico;
	}

	public void setPublico(boolean publico) {
		this.publico = publico;
	}
	
	public String getLastIP() {
		return lastIP;
	}

	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}
	
	public boolean getNovedad() {
		return novedad;
	}

	public void setNovedad(boolean novedad) {
		this.novedad = novedad;
	}
	
	public boolean getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(boolean solicitud) {
	
		this.solicitud = solicitud;
	}
	 
}
