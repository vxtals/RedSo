package server;

import java.io.Serializable;
import java.util.Collections;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.hibernate.*;

@SuppressWarnings("unchecked")
public class Sesion extends UnicastRemoteObject implements SesionInterfaz, Serializable {

	private static final long serialVersionUID = 3219939288515936391L;
	private String email;
	private String nombre;
	private SessionFactory sessionFac;
	
	public Sesion(String nombre, String email, SessionFactory sessionFac) throws RemoteException{
		super();
		this.nombre = nombre;
		this.email = email;
		this.sessionFac = sessionFac;
		
	}

	@Override
	public List<Message> obtenerNovedades(String email) throws RemoteException {
		
		
		Session session10 = this.sessionFac.openSession();
		Transaction tx = session10.beginTransaction();
		
        String sql = "SELECT * FROM Friend INNER JOIN Message ON Friend.email_1 = Message.email_ID WHERE (((Friend.email_2)=:email) AND acepta_2 = '1')";
        SQLQuery query = session10.createSQLQuery(sql).addEntity(Message.class);
        query.setParameter("email", email);
		List<Message> datos_mensaje = query.list();
        tx.commit();
        
        Transaction tx2 = session10.beginTransaction();
		
        String sql2 = "SELECT * FROM Friend INNER JOIN Message ON Friend.email_2 = Message.email_ID WHERE (((Friend.email_1)=:email) AND acepta_2 = '1')";
        SQLQuery query2 = session10.createSQLQuery(sql2).addEntity(Message.class);
        query2.setParameter("email", email);
        List<Message> datos_mensaje2 = query2.list();
        tx2.commit();
		datos_mensaje.addAll(datos_mensaje2);
		
		Transaction tx3 = session10.beginTransaction();

		String sql3 = "SELECT * FROM Message WHERE email_ID = :email";
		SQLQuery query3 = session10.createSQLQuery(sql3).addEntity(Message.class);
		query3.setParameter("email", email);
		List<Message> datos_mensaje3 = query3.list();
		tx3.commit();
		session10.close();
		datos_mensaje.addAll(datos_mensaje3);
		
		Collections.sort(datos_mensaje, Collections.reverseOrder());
		
		return datos_mensaje;
		
	}

	@Override
	public String[] getDatos(String email) throws RemoteException {
		String[] datosMuro = null;
		String nombreApellidos = null;
		
		Session session1 = this.sessionFac.openSession();
		Transaction tx = session1.beginTransaction();
		
        String sql = "SELECT * FROM User WHERE email_ID = :email";
        SQLQuery query = session1.createSQLQuery(sql).addEntity(User.class);
        query.setParameter("email", email);
        User datos_usuario =(User)query.uniqueResult();

      
        nombreApellidos = datos_usuario.getNombre() + " " + datos_usuario.getApellidos();
       
        tx.commit();
		session1.close();
		
		datosMuro = new String[9];
		datosMuro[0] = nombreApellidos;
		datosMuro[1] = datos_usuario.getNombre();
		datosMuro[2] = datos_usuario.getApellidos();
		datosMuro[3] = datos_usuario.getEmail();
		datosMuro[4] = datos_usuario.getWeb();
		
		return datosMuro;
		
	}
	
	@Override
	public boolean getPublic(String email) throws RemoteException {
		boolean isPublic;
		Session session1 = this.sessionFac.openSession();
		Transaction tx = session1.beginTransaction();
		
        String sql = "SELECT * FROM User WHERE email_ID = :email";
        SQLQuery query = session1.createSQLQuery(sql).addEntity(User.class);
        query.setParameter("email", email);
        User datos_usuario =(User)query.uniqueResult();

        isPublic = datos_usuario.getPublico();
       
        tx.commit();
		session1.close();
		
		
		return isPublic;
		
	}
	
	@Override
	public byte[] getFoto(String email) throws RemoteException {
		Session session1 = this.sessionFac.openSession();
		Transaction tx = session1.beginTransaction();
		
		String sql = "SELECT * FROM User WHERE email_ID = :email";
        SQLQuery query = session1.createSQLQuery(sql).addEntity(User.class);
        query.setParameter("email", email);
        User datos_usuario =(User)query.uniqueResult();
        
        byte[] foto = datos_usuario.getFoto();
		
		tx.commit();
		session1.close();
		
		return foto;
	}

	@Override
	public void addFriend(String email_2) throws RemoteException {
		
		if(!getFriendship(email_2)[0]){
		Session session1 = this.sessionFac.openSession();
		session1.beginTransaction();
		session1.save(new Friend(email, email_2, true, false));
		session1.getTransaction().commit();
		session1.close();
		
		Runnable notificar = new NotificarSolicitud(this.sessionFac, email, email_2);
		new Thread(notificar).start();
		}
		
		
	}
	
	
	@Override
	public void confirmFriend(String email_1) throws RemoteException {
		
		Session session8 = this.sessionFac.openSession();
		session8.beginTransaction();
		String sql = "SELECT * FROM Friend WHERE (email_1 = :email_1) AND (email_2 = :email)";
        SQLQuery query = session8.createSQLQuery(sql).addEntity(Friend.class);
        query.setParameter("email", email);
        query.setParameter("email_1", email_1);
        Friend datos_amigo = (Friend)query.uniqueResult();
		session8.getTransaction().commit();
		
		
			Session session12 = this.sessionFac.openSession();
			session12.beginTransaction();
			Friend amigo = (Friend) session12.load( Friend.class, datos_amigo.getId());
			amigo.setAcepta_2(true);
			session12.flush();
			session12.getTransaction().commit();
			session12.close();
		
	}
	
	@Override
	public void refuseFriend(String email_1) throws RemoteException {
		
		Session session9 = this.sessionFac.openSession();
		session9.beginTransaction();
		String sql = "DELETE FROM Friend WHERE email_1 = :email_1 AND email_2 = :email";
        SQLQuery query = session9.createSQLQuery(sql);
        query.setParameter("email", email);
        query.setParameter("email_1", email_1);
        query.executeUpdate();
        session9.getTransaction().commit();
		session9.close();
		
	}

	@Override
	public boolean[] getFriendship(String email_objetivo) throws RemoteException {
		
		boolean[] friendship = {false,false,false,false};
		Session session6 = this.sessionFac.openSession();
		Transaction tx = session6.beginTransaction();
        String sql = "SELECT * FROM Friend WHERE (email_1 = :email AND email_2 = :email_objetivo) OR (email_1 = :email_objetivo AND email_2 = :email)";
        SQLQuery query = session6.createSQLQuery(sql).addEntity(Friend.class);
        query.setParameter("email", email);
        query.setParameter("email_objetivo", email_objetivo);
        Friend datos_amistad =(Friend)query.uniqueResult();
        tx.commit();
		session6.close();
        
        if (datos_amistad != null){
        	friendship[0] = true;
        	if (datos_amistad.getAcepta_1() && datos_amistad.getAcepta_2()) friendship[1] = true;
        	if (((datos_amistad.getEmail_1().compareTo(email) == 0) && datos_amistad.getAcepta_1()) || ((datos_amistad.getEmail_2().compareTo(email) == 0) && datos_amistad.getAcepta_2())){
        		friendship[2] = true;
        	}
        	if (((datos_amistad.getEmail_1().compareTo(email_objetivo) == 0) && datos_amistad.getAcepta_1()) || ((datos_amistad.getEmail_2().compareTo(email_objetivo) == 0) && datos_amistad.getAcepta_2())){
        		friendship[3] = true;
        	}
        }
		return friendship;
	}

	@Override
	public void publishMessage(String mensaje) throws RemoteException {
		
		Session session3 = this.sessionFac.openSession();
		session3.beginTransaction();
		session3.save(new Message(email, mensaje, nombre));
		session3.getTransaction().commit();
		session3.close();
		
		Runnable notificar = new NotificarNovedad(this.sessionFac, email);
		new Thread(notificar).start();
	}
		
	
	@Override
	public List<Message> getMessages(String email) throws RemoteException {
		Session session2 = this.sessionFac.openSession();
		Transaction tx = session2.beginTransaction();
        String sql = "SELECT * FROM Message WHERE email_ID = :email ORDER BY time_ID DESC";
        SQLQuery query = session2.createSQLQuery(sql).addEntity(Message.class);
        query.setParameter("email", email);
        List<Message> datos_mensaje = query.list();

        tx.commit();
		session2.close();
		
		return datos_mensaje;
	}

	@Override
	public List<User> getResults(String busqueda) throws RemoteException {

				String delimitadores= " ";
				String[] palabrasSeparadas = busqueda.split(delimitadores);
				
				Session session5 = this.sessionFac.openSession();
				Transaction tx2 = session5.beginTransaction();
				List<User> listaResultados = null;
				
				for (int i = 0; i<palabrasSeparadas.length; i++){
					String sql = "SELECT * FROM User WHERE (apellidos LIKE '%" + palabrasSeparadas[i] + "%') OR email_ID LIKE '*" + palabrasSeparadas[i] + "%'";
					SQLQuery query = session5.createSQLQuery(sql).addEntity(User.class);
					if (i==0){
						listaResultados = query.list();
					}else{
						listaResultados.addAll(query.list());
					}
				}
				
		        tx2.commit();
				session5.close();
				
				return listaResultados;
	}

	@Override
	public List<User> getFriends(String email) throws RemoteException {
		
		Session session7 = this.sessionFac.openSession();
		Transaction tx2 = session7.beginTransaction();
		
		String sql = "SELECT * FROM Friend INNER JOIN User ON Friend.email_1 = User.email_ID WHERE (((Friend.email_2)=:email))";
		SQLQuery query = session7.createSQLQuery(sql).addEntity(User.class);
		query.setParameter("email", email);
	    List<User> listaAmigos = query.list();
        tx2.commit();
		
		Transaction tx3 = session7.beginTransaction();
		
		String sql2 = "SELECT * FROM Friend INNER JOIN User ON Friend.email_2 = User.email_ID WHERE (((Friend.email_1)=:email))";
		SQLQuery query2 = session7.createSQLQuery(sql2).addEntity(User.class);
		query2.setParameter("email", email);
	    List<User> listaAmigos2 = query2.list();
        tx3.commit();
		session7.close();
		
		listaAmigos.addAll(listaAmigos2);
		
		return listaAmigos;
		
	}
	

	@Override
	public void logOut(String email) throws RemoteException {
		Registry registry = LocateRegistry.getRegistry();
			try {
				registry.unbind(email);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
	        UnicastRemoteObject.unexportObject(this, true);
		
	}
	

	@Override
	public boolean modifyProfile(String nombre, String apellidos, String web, Boolean isPublic, byte[] foto) throws RemoteException {
		
		Session session1 = this.sessionFac.openSession();
		Transaction tx = session1.beginTransaction();
		
		User usuario = (User)session1.get(User.class, email);
        
        if(nombre != null){
			usuario.setNombre(nombre);
		}
		if(apellidos != null){
			usuario.setApellidos(apellidos);
		}
		if(web != null){
			usuario.setWeb(web);
		}
		if(isPublic != null){
			usuario.setPublico(isPublic);
		}
		if(foto != null){
			usuario.setFoto(foto);
		}
        
        tx.commit();
		session1.close();
		return true;
		
	}
	
	



}
