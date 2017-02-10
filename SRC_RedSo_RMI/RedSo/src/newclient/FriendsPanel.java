package newclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import server.SesionInterfaz;
import server.User;


public class FriendsPanel extends JPanel {
	
	private static final long serialVersionUID = -8186379047614919576L;
	
	private MainWindow mainFrame;
	private String email;
	
	private JPanel friends_panel;
	private JScrollPane friendScroll;
	
	public FriendsPanel(MainWindow mainFrame){

		this.mainFrame = mainFrame;
		email = mainFrame.getEmail();
		
		this.setMaximumSize(new Dimension(1000,700));
		this.setBackground(new Color(30, 144, 255));
		this.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(new Color(240,248,255));
		panelCentral.setMinimumSize(new Dimension(600, 540));
		panelCentral.setPreferredSize(new Dimension(1000, 540));
		this.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.PAGE_AXIS));
		
		JPanel panelLeft = new JPanel();
		panelLeft.setBackground(new Color(240,248,255));
		panelLeft.setPreferredSize(new Dimension(200, 10));
		this.add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(null);

		JPanel panelRight = new JPanel();
		panelRight.setBackground(new Color(240,248,255));
		panelRight.setPreferredSize(new Dimension(200, 10));
		this.add(panelRight, BorderLayout.EAST);
		
		friends_panel = new JPanel();
		friends_panel.setBackground(Color.white);
		friends_panel.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(30, 144, 255)));
		friends_panel.setLayout(new BoxLayout(friends_panel, BoxLayout.PAGE_AXIS));

    	friendScroll = new JScrollPane(friends_panel,
    			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	friendScroll.setBackground(Color.white);
    	friendScroll.setMinimumSize(new Dimension(600, 520));
    	friendScroll.setMaximumSize(new Dimension(600, 520));
    	friendScroll.setPreferredSize(new Dimension(600, 520));
		panelCentral.add(friendScroll);
    	
		cargarResultados();
		
	
	}
	
	private void cargarResultados(){
		List<User> listaAmigos;
		Iterator<User> iterator;
		User userTemp;
		
		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
		    listaAmigos = stub.getFriends(email);
		    iterator = listaAmigos.iterator();
		    
		    
		    JTextField[] arrayNombres = new JTextField[listaAmigos.size()];
			JButton[] arrayMuro = new JButton[listaAmigos.size()];
			JButton[] arrayRechazo = new JButton[listaAmigos.size()];
			JPanel[] arrayPaneles = new JPanel[listaAmigos.size()];
			
		    friends_panel.setPreferredSize(new Dimension(600, listaAmigos.size()*80));
		    friends_panel.setMaximumSize(new Dimension(600, listaAmigos.size()*80));
		    friendScroll.revalidate();
		    friends_panel.removeAll();
		    
		    int i = 0;
			while (iterator.hasNext()){
				arrayPaneles[i] = new JPanel();
				arrayPaneles[i].setBackground(Color.WHITE);
				arrayPaneles[i].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(51, 153, 255)));
				arrayPaneles[i].setMinimumSize(new Dimension(600,80));
				arrayPaneles[i].setMaximumSize(new Dimension(600,80));
				arrayPaneles[i].setPreferredSize(new Dimension(600,80));
				friends_panel.add(arrayPaneles[i]);
				arrayPaneles[i].setLayout(null);
				
				userTemp = iterator.next();
				
				arrayNombres[i] = new JTextField();
				arrayNombres[i].setHorizontalAlignment(JTextField.RIGHT);
				arrayNombres[i].setBorder(null);
				arrayNombres[i].setBackground(Color.white);
				arrayNombres[i].setHorizontalAlignment(JTextField.CENTER);
				arrayNombres[i].setFont(new Font("Calibri", Font.PLAIN, 18));
				arrayNombres[i].setEditable(false);
				arrayNombres[i].setBounds(10, 11, 300, 28);
				arrayPaneles[i].add(arrayNombres[i]);
				
				arrayMuro[i] = new JButton("Ver Muro");
				arrayMuro[i].setVisible(true);
				arrayMuro[i].setForeground(Color.black);
				arrayMuro[i].setBounds(340, 11, 100, 28);
				arrayPaneles[i].add(arrayMuro[i]);
				
				arrayRechazo[i] = new JButton("Rechazar");
				arrayRechazo[i].setEnabled(false);
				arrayRechazo[i].setVisible(false);
				arrayRechazo[i].setFont(new Font("Tahoma", Font.BOLD, 11));
				arrayRechazo[i].setForeground(Color.white);
				arrayRechazo[i].setBackground(new Color(30, 144, 255));
				arrayRechazo[i].setBounds(462, 11, 100, 28);
				arrayPaneles[i].add(arrayRechazo[i]);
				
				try{
					registry = LocateRegistry.getRegistry(MainClient.host,1099);
					stub = (SesionInterfaz) registry.lookup(email);
					boolean[] amistad = stub.getFriendship(userTemp.getEmail());
					if(amistad[1] == true){
						arrayMuro[i].setEnabled(true);
						arrayMuro[i].setName(userTemp.getEmail());
						//arrayRechazo[i].setVisible(true);
						arrayMuro[i].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								mainFrame.unselect();
								mainFrame.panelCenter.removeAll();
								JButton pressed = (JButton)arg0.getSource();
								JPanel panelWall = new VisitedPanel(mainFrame, pressed.getName());
								mainFrame.panelCenter.add(panelWall);
								mainFrame.panelCenter.revalidate();
							}
						});
					}else if(amistad[1]==false && amistad[2]==true){
						arrayMuro[i].setEnabled(false);
					}else if(amistad[1]==false && amistad[3]==true){
						arrayMuro[i].setText("Aceptar");
						arrayMuro[i].setEnabled(true);
						arrayMuro[i].setName(userTemp.getEmail());
						arrayMuro[i].setBackground(new Color(51, 204, 51));
						arrayMuro[i].setFont(new Font("Tahoma", Font.BOLD, 11));						
						arrayMuro[i].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								try{
									Registry registry = LocateRegistry.getRegistry(MainClient.host,1099);
									SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
									JButton pressed = (JButton)arg0.getSource();
									stub.confirmFriend(pressed.getName());
									mainFrame.showFriends();
								}catch (Exception e1){
									e1.printStackTrace();
								}
							}
						});
						arrayRechazo[i].setEnabled(true);
						arrayRechazo[i].setVisible(true);
						arrayRechazo[i].setName(userTemp.getEmail());
						arrayRechazo[i].addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								try{
									Registry registry = LocateRegistry.getRegistry(MainClient.host,1099);
									SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
									JButton pressed = (JButton)arg0.getSource();
									stub.refuseFriend(pressed.getName());
									mainFrame.showFriends();
								}catch (Exception e1){
									e1.printStackTrace();
								}
							}
						});
					}
				}catch (Exception e1){
					e1.printStackTrace();
				}
				
				arrayNombres[i].setText(userTemp.getNombre() + " " + userTemp.getApellidos());
				arrayPaneles[i].setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(30, 144, 255)));
			   i++;
	        }
		}catch (Exception e1){
			e1.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	friendScroll.getVerticalScrollBar().setValue(0);
            }
        });
		
	}
	
}
