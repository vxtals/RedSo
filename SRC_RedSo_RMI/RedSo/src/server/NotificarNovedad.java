package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import newclient.PushNewsInterface;

public class NotificarNovedad extends Thread{
	
	private SessionFactory sessionFac;
	private String email;
	
	NotificarNovedad(SessionFactory sessionFac, String email){
		this.sessionFac = sessionFac;
		this.email = email;
	}
	
	public void run() {
		
		
		System.out.println("Comienza notificar novedades");
		Session session7 = this.sessionFac.openSession();
		Transaction tx2 = session7.beginTransaction();
		System.out.println("Se ha abierto una sesión ...");
		String sql = "SELECT * FROM Friend INNER JOIN User ON Friend.email_1 = User.email_ID WHERE (((Friend.email_2)=:email))";
		SQLQuery query = session7.createSQLQuery(sql).addEntity(User.class);
		query.setParameter("email", email);;
	    @SuppressWarnings("unchecked")
		List<User> listaIP = query.list();
        tx2.commit();
		
		Transaction tx3 = session7.beginTransaction();
		
		String sql2 = "SELECT * FROM Friend INNER JOIN User ON Friend.email_2 = User.email_ID WHERE (((Friend.email_1)=:email))";
		SQLQuery query2 = session7.createSQLQuery(sql2).addEntity(User.class);
		query2.setParameter("email", email);;
	    @SuppressWarnings("unchecked")
		List<User> listaIP2 = query2.list();
        tx3.commit();
		session7.close();
		
		listaIP.addAll(listaIP2);
		
		//System.out.println("Empieza el proceso de notidicación: \n......");
		Iterator<User> iteradorIP = listaIP.iterator();
		while (iteradorIP.hasNext()){
			User ip = iteradorIP.next();
			try{
				System.out.println("Intento de conectar con " + ip.getLastIP());
				Registry registry = LocateRegistry.getRegistry(ip.getLastIP(), 1099);
				PushNewsInterface stub = (PushNewsInterface) registry.lookup("Push");
				String email_destino = stub.consultaIdentidad();
				if(ip.getEmail().compareTo(email_destino) == 0){
					stub.recibirNotificacion(email);
				}
			}catch(Exception e){
				System.out.println("Salta excepción");
				e.printStackTrace();
				Session session8 = this.sessionFac.openSession();
				session8.beginTransaction();
				//System.out.println("Va a establecer true novedad ");
				ip.setNovedad(true);
				//System.out.println("Ya lo ha hecho");
				session8.getTransaction().commit();
				session8.close();
			}
		}
		

    }

}