package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TopicSession;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import javax.jms.*;

public class Conexion extends UnicastRemoteObject implements ConexionInterfaz, Serializable {

	private static final long serialVersionUID = -2363846712294072247L;
	private SessionFactory sessionFac;

	@SuppressWarnings("deprecation")
	public Conexion() throws RemoteException{
		super();
		sessionFac = new Configuration()
		.configure()
		.buildSessionFactory();
	}

	@Override
	public boolean iniciarSesion(String email, String pass) throws RemoteException {
		try{
			Session session2 = sessionFac.openSession();
			session2.beginTransaction();
			String sql = "SELECT password FROM Identifier WHERE email_ID = :email";
			SQLQuery query = session2.createSQLQuery(sql);
			query.setParameter("email", email);
			String password = (String)query.uniqueResult();
			session2.getTransaction().commit();
			session2.beginTransaction();
			String sql2 = "SELECT * FROM User WHERE email_ID = :email";
			SQLQuery query2 = session2.createSQLQuery(sql2).addEntity(User.class);
			query2.setParameter("email", email);
			User usuario = (User)query2.uniqueResult();
			session2.getTransaction().commit();
			String nombreApellidos = usuario.getNombre() + " " + usuario.getApellidos(); 

			if (pass.compareTo(password) == 0){
				System.out.println("Tras comparar contraseñas, procede a crear la sesión");
				Sesion stub = new Sesion(nombreApellidos, email, sessionFac);
				//SesionInterfaz stub = (SesionInterfaz) UnicastRemoteObject.exportObject(obj_remoto,0);
				Registry registry = LocateRegistry.getRegistry();
				registry.rebind(email, stub);
				String clientHost = RemoteServer.getClientHost();
				session2.beginTransaction();
				usuario.setLastIP(clientHost);
				session2.getTransaction().commit();

				Connection connection2 = MainServer.connectionFactory.createConnection();


				javax.jms.TopicSession sessionjms = (TopicSession) connection2.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
				connection2.start();
				Topic messageTopic = sessionjms.createTopic(email + "_topic");
				TopicSubscriber subscriber = sessionjms.createSubscriber(messageTopic, null, true);
				subscriber.setMessageListener(stub);
				connection2.start();

			}else{
				return false;
			}

		} catch (Exception e){
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			return false;
		}

		return true;

	}

	@Override
	public boolean crearUsuario(String email, String name, String apellidos, String pass, byte[] foto, String web, boolean publico) throws RemoteException{


		Session session1 = sessionFac.openSession();
		session1.beginTransaction();
		User user1 = new User(email, name, apellidos,foto, web, publico, null, false, false);
		session1.save(user1);
		session1.save(new Identifier(email, pass));
		session1.getTransaction().commit();
		session1.close();


		Connection connection;
		javax.jms.Session session;
		try {
			connection = MainServer.connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(email + "_queue");
			Destination destination2 = session.createTopic(email + "_topic");

			MessageProducer producer = session.createProducer(destination);
			MessageProducer producer2 = session.createProducer(destination2);
			producer.getDestination();
			producer2.getDestination();

			connection.close();



		} catch (JMSException e) {
			e.printStackTrace();
		}


		boolean ini_sesion = iniciarSesion(email, pass);
		return ini_sesion;
	}



}
