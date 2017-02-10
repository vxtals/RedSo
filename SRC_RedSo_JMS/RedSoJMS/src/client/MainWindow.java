package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import server.SesionInterfaz;
import server.User;

public class MainWindow extends JFrame implements MessageListener {
	
private static final long serialVersionUID = -208549039642683290L;
	
	private MainWindow mainFrame;
	private JPanel panelGlobal;
	private JPanel panelTop;
	private JPanel panelTopContent;
	private JTextField fieldSearch;
	private JButton btnFriends;
	private JButton btnNews;
	private JButton btnWall;
	protected JPanel panelCenter;
	
	private String user_email;
	private boolean inNews = false;
	private boolean inFriends = false;
	
	static ConnectionFactory connectionFactory;
	
	public MainWindow(Point location,Dimension dim, String email) throws IOException, Exception{
		
		this.mainFrame = this;
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("iconoRS.png"));
		this.setIconImage(img);
		this.user_email = email;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(dim.width, dim.height);
		this.setLocation(location.x - (dim.width/2), location.y - (dim.height/2));
		this.setResizable(false);
		this.setVisible(true);
		
		//connectionFactory = new ActiveMQConnectionFactory("tcp://vxtals.es:61616");
		connectionFactory = new ActiveMQConnectionFactory("tcp://" + MainClient.host + ":61616");
		
		panelGlobal = (JPanel)this.getContentPane();
		panelGlobal.setBackground(new Color(240, 248, 255));
		panelGlobal.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelGlobal.setLayout(new BoxLayout(panelGlobal, BoxLayout.PAGE_AXIS));// BorderLayout(0, 0));
		String[] user_data = null;
		byte[] profile_photo = null;
		List<User> friends_list;
		
		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
			user_data = stub.getDatos(email);
			profile_photo = stub.getFoto(email);
			friends_list = stub.getFriends(user_email);
			Iterator<User> iterator_friends = friends_list.iterator();
			while(iterator_friends.hasNext()){
				User tempuser = iterator_friends.next();
				boolean[] amistad = stub.getFriendship(tempuser.getEmail());
				if(amistad[1] == true && amistad[2]== true){
					Connection connection = connectionFactory.createConnection();
					connection.start();
			        javax.jms.TopicSession session = (TopicSession) connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			        Topic notifyTopic = session.createTopic(tempuser.getEmail() + "_topic");
			        TopicSubscriber subscriber = session.createSubscriber(notifyTopic, null, true);
			        subscriber.setMessageListener(this);
			        connection.start();
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		/*
		 * AQU� OBTENCI�N DE DATOS
		 */
		
		panelTop = new JPanel();
		panelTop.setMinimumSize(new Dimension(1000, 140));
		panelTop.setPreferredSize(new Dimension(1000, 140));
		panelGlobal.add(panelTop);
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));

		panelCenter = new JPanel();
		panelCenter.setBackground(new Color(240, 248, 255));
		panelCenter.setMinimumSize(new Dimension(1000, 140));
		panelCenter.setPreferredSize(new Dimension(1000, 560));
		panelCenter.setBorder(null);
		panelGlobal.add(panelCenter);
		panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));

		panelTopContent = new JPanel();
		panelTopContent.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(255, 255, 255)));
		panelTopContent.setBackground(new Color(30, 144, 255));
		panelTopContent.setMinimumSize(new Dimension(0, 75));
		panelTopContent.setPreferredSize(new Dimension(1000, 75));
		panelTop.add(panelTopContent);
		panelTopContent.setLayout(null);

		JLabel labelTitle = new JLabel("RedSo");
		labelTitle.setBounds(6, 6, 162, 69);
		labelTitle.setFont(Fuentes.title_font_wall);
		panelTopContent.add(labelTitle);

		fieldSearch = new JTextField("Busca usuarios");
		fieldSearch.setBorder(null);
		fieldSearch.setForeground(Color.GRAY);
		fieldSearch.setBounds(257, 29, 313, 20);
		fieldSearch.setPreferredSize(new Dimension(420, 20));
		panelTopContent.add(fieldSearch);
		fieldSearch.setColumns(10);
		fieldSearch.requestFocus();
		fieldSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (fieldSearch.getText().compareTo("Busca usuarios") == 0)
					fieldSearch.setForeground(Color.BLACK);
				fieldSearch.setText("");
			}
		});
		fieldSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(fieldSearch.getText().length() == 0){
					fieldSearch.setForeground(Color.GRAY);
					fieldSearch.setText("Busca usuarios");
				}
			}
		});

		JButton btnSearch = new JButton("Buscar");
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				showSearch(fieldSearch.getText());
				fieldSearch.setForeground(Color.GRAY);
				fieldSearch.setText("Busca usuarios");
			}
		});
		btnSearch.setBounds(577, 28, 89, 21);
		btnSearch.setFont(Fuentes.button_font_search);
		panelTopContent.add(btnSearch);


		BufferedImage img_foto;

		try {
			JButton edit_button;
			if(profile_photo != null){
				img_foto = ImageIO.read(new ByteArrayInputStream(profile_photo));
				edit_button = new JButton(new ImageIcon(img_foto));
			}else{
				edit_button = new JButton("Perfil");
			}
			edit_button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
					btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
					btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
					showProfile();
				}
			});
			edit_button.setBounds(925, 3, 65, 65);
			edit_button.setBorder(BorderFactory.createEmptyBorder());
			edit_button.setContentAreaFilled(false);
			panelTopContent.add(edit_button);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JButton btnLogout = new JButton("Salir");
		btnLogout.setFont(Fuentes.button_font_logout);
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBackground(new Color(255, 69, 0));
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panelGlobal.removeAll();
				panelGlobal.repaint();
				try{
					Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
					SesionInterfaz stub = (SesionInterfaz) registry.lookup(user_email);
					stub.logOut(user_email);
				} catch (Exception e){
					e.printStackTrace();
				}
				mainFrame.dispose();
				System.exit(0);
			}
		});
		btnLogout.setBounds(810, 6, 113, 23);
		panelTopContent.add(btnLogout);

		JLabel labelName = new JLabel(user_data[0]);
		labelName.setHorizontalAlignment(JLabel.RIGHT);
		labelName.setForeground(Color.white);
		labelName.setBounds(668, 38, 255, 14);
		labelName.setFont(Fuentes.calibri_font_name);
		panelTopContent.add(labelName);

		JPanel panelButtons = new JPanel();
		panelButtons.setMinimumSize(new Dimension(1000, 65));
		panelButtons.setPreferredSize(new Dimension(1000, 65));
		panelTop.add(panelButtons);
		panelButtons.setLayout(new BorderLayout(0, 0));

		btnWall = new JButton("Muro");
		btnWall.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
		btnWall.setFont(Fuentes.calibri_font_tab_sel);
		btnWall.setBackground(Color.white);
		btnWall.setPreferredSize(new Dimension(330, 23));
		btnWall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnWall.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
				showWall();
			}});
		panelButtons.add(btnWall, BorderLayout.WEST);

		btnNews = new JButton("Novedades");
		btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnNews.setFont(Fuentes.calibri_font_tab_usel);
		btnNews.setBackground(Color.white);
		btnNews.setPreferredSize(new Dimension(333, 23));
		btnNews.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
				btnNews.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
				showNews();
			}});
		panelButtons.add(btnNews, BorderLayout.CENTER);

		btnFriends = new JButton("Amigos");
		btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnFriends.setFont(Fuentes.calibri_font_tab_usel);
		btnFriends.setBackground(Color.white);
		btnFriends.setPreferredSize(new Dimension(330, 23));
		btnFriends.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showFriends();
			}
		});
		panelButtons.add(btnFriends, BorderLayout.EAST);

		showWall();

	}
	
	public void unselect(){
		btnNews.setForeground(Color.black);
		btnNews.setBackground(Color.white);
		btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
	}
	
	public void showWall(){
		inFriends = false;
		inNews = false;
		btnWall.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
		btnNews.setForeground(Color.black);
		btnNews.setBackground(Color.white);
		btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		panelCenter.removeAll();
		JPanel panelWall = new WallPanel(user_email);
		panelCenter.add(panelWall);
		panelCenter.revalidate();
	}
	
	public void showNews(){
		inFriends = false;
		inNews = true;
		btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnNews.setForeground(Color.black);
		btnNews.setBackground(Color.white);
		btnNews.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
		btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		panelCenter.removeAll();
		JPanel newsWall = new NewsPanel(this);
		panelCenter.add(newsWall);
		panelCenter.revalidate();
	}
	
	public void showFriends(){
		inFriends = true;
		inNews = false;
		btnWall.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnNews.setForeground(Color.black);
		btnNews.setBackground(Color.white);
		btnNews.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(30, 144, 255)));
		btnFriends.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(30, 144, 255)));
		panelCenter.removeAll();
		JPanel friendsWall = new FriendsPanel(this);
		panelCenter.add(friendsWall);
		panelCenter.revalidate();
	}
	
	public void showSearch(String busqueda){
		inFriends = false;
		inNews = false;
		unselect();
		panelCenter.removeAll();
		JPanel searchWall = new SearchPanel(this, busqueda);
		panelCenter.add(searchWall);
		panelCenter.revalidate();
	}
	
	public void showProfile(){
		inFriends = false;
		inNews = false;
		unselect();
		panelCenter.removeAll();
		JPanel editWall = new EditPanel(this, user_email);
		panelCenter.add(editWall);
		panelCenter.revalidate();
	}
	
	public String getEmail(){
		return this.user_email;
	}
	
	public void newsNotification (){
		System.out.println("Ejecuta la notificaci�n");
		if (inNews){
			showNews();
			btnNews.setForeground(Color.white);
			btnNews.setBackground(Color.RED);
		}else{
			btnNews.setForeground(Color.white);
			btnNews.setBackground(Color.RED);
		}
	}
	
	public void requestNotification (){
		System.out.println("Ejecuta la notificaci�n");
		if (inFriends){
			showFriends();
			btnFriends.setBorder(new MatteBorder(4, 4, 4, 4, Color.RED));
			
		}else{
			btnFriends.setBorder(new MatteBorder(2, 2, 2, 2, Color.RED));
		}
	}
	

	@Override
	public void onMessage(Message message) {
		//TextMessage textMessage = (TextMessage) message;
		//String usuario = textMessage.getStringProperty("usuario");
		newsNotification();
	}
	

}
