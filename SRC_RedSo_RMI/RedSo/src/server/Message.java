package server;

import java.util.Date;
import java.io.Serializable;
public class Message implements Serializable, Comparable<Message> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2078698557371601062L;

	private Long id;
	
	private Date fecha;
	private String email;
	private String mensaje;
	private String nombre;
	
	public Message() {
		// this form used by Hibernate
	}
	
	public Message(String email, String mensaje, String nombre){
		this.nombre = nombre;
		this.email = email;
		this.mensaje = mensaje;
		this.fecha = new Date();
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
	
	public String getMensaje(){
		return this.mensaje;
	}
	
	public void setMensaje(String mensaje){
		this.mensaje = mensaje;
	}
	
	public Date getFecha(){
		return this.fecha;
	}
	
	public void setFecha(Date fecha){
		this.fecha = fecha;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public int compareTo(Message mensaje) {
		// TODO Auto-generated method stub
		return fecha.compareTo(mensaje.fecha);
	}

}
