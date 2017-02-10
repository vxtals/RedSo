package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import newclient.PushNewsInterface;

public class NotificarSolicitud extends Thread{
	
	private SessionFactory sessionFac;
	private String email;
	private String email_2;
	
	NotificarSolicitud(SessionFactory sessionFac, String email, String email_2){
		this.sessionFac = sessionFac;
		this.email = email;
		this.email_2 = email_2;
	}
	
	public void run() {
		System.out.println("Comienza notificación de solicitud");
		Session session7 = sessionFac.openSession();
		Transaction tx2 = session7.beginTransaction();
		String sql = "SELECT * FROM User WHERE email_ID=:email_2";
		SQLQuery query = session7.createSQLQuery(sql).addEntity(User.class);
		query.setParameter("email_2", email_2);;
		User userIP = (User)query.uniqueResult();
		tx2.commit();
		session7.close();
		try{	
			Registry registry = LocateRegistry.getRegistry(userIP.getLastIP(), 1099);
			PushNewsInterface stub = (PushNewsInterface) registry.lookup("Push");
			String email_destino = stub.consultaIdentidad();
			if(userIP.getEmail().compareTo(email_destino) == 0){
				stub.nuevaSolicitud(email);
			}
		}catch(Exception e){
			Session session8 = this.sessionFac.openSession();
			session8.beginTransaction();
			userIP.setSolicitud(true);
			session8.getTransaction().commit();
			session8.close();
		}

    }

}
