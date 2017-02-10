package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import server.Message;
import server.SesionInterfaz;

@SuppressWarnings("deprecation")
public class NewsPanel extends JPanel {
	
	private static final long serialVersionUID = -9113896668942653052L;
	
	private String email_user;
	private JPanel messages_panel;
	private JScrollPane newsScroll;
	
	public NewsPanel(MainWindow mainFrame){
		this.email_user = mainFrame.getEmail();
		
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

		messages_panel = new JPanel();
		messages_panel.setBackground(Color.white);
		messages_panel.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(30, 144, 255)));
		messages_panel.setLayout(new BoxLayout(messages_panel, BoxLayout.PAGE_AXIS));

    	newsScroll = new JScrollPane(messages_panel,
    			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	newsScroll.setBackground(Color.white);
    	newsScroll.setMinimumSize(new Dimension(600, 520));
    	newsScroll.setMaximumSize(new Dimension(600, 520));
    	newsScroll.setPreferredSize(new Dimension(600, 520));
		panelCentral.add(newsScroll);
		
    	cargarMensajes();
	}
	
	private void cargarMensajes(){
		List<Message> listaMensajes;
		Iterator<Message> iterator;
		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email_user);
		    listaMensajes = stub.obtenerNovedades(email_user);
		    iterator = listaMensajes.iterator();
		    messages_panel.setPreferredSize(new Dimension(600, listaMensajes.size()*80));
		    newsScroll.revalidate();
		    JPanel[] arrayPaneles = new JPanel[listaMensajes.size()];
		    JTextArea[] arrayMessages = new JTextArea[listaMensajes.size()];
		    JTextArea[] arrayHours = new JTextArea[listaMensajes.size()];
		    JTextArea[] arrayNombres = new JTextArea[listaMensajes.size()];
		    messages_panel.removeAll();
		    int i = 0;
			while (iterator.hasNext()){
				arrayPaneles[i] = new JPanel();
				arrayPaneles[i].setBackground(Color.white);
				arrayPaneles[i].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(51, 153, 255)));
				arrayPaneles[i].setMinimumSize(new Dimension(600,80));
				arrayPaneles[i].setMaximumSize(new Dimension(600,80));
				arrayPaneles[i].setPreferredSize(new Dimension(600,80));
				messages_panel.add(arrayPaneles[i]);
				arrayPaneles[i].setLayout(null);
				
				Message msg = iterator.next();
				
				arrayMessages[i] = new JTextArea(msg.getMensaje());
				arrayMessages[i].setLineWrap(true);
				arrayMessages[i].setWrapStyleWord(true);
				arrayMessages[i].setFont(new Font("Calibri", Font.PLAIN, 16));
				arrayMessages[i].setBackground(Color.white);
				arrayMessages[i].setEditable(false);
				arrayMessages[i].setBounds(20, 4, 554, 60);
				arrayPaneles[i].add(arrayMessages[i]);
				
				arrayNombres[i] = new JTextArea(msg.getNombre());
				arrayNombres[i].setForeground(new Color(30, 144, 255));
				arrayNombres[i].setFont(new Font("Monospaced", Font.BOLD, 13));
				arrayNombres[i].setEditable(false);
				arrayNombres[i].setBorder(null);
				arrayNombres[i].setBackground(Color.WHITE);
				arrayNombres[i].setBounds(10, 60, 340, 22);
				arrayPaneles[i].add(arrayNombres[i]);
				
				if(msg.getFecha().getMinutes() < 10){
					arrayHours[i] = new JTextArea(msg.getFecha().getDate() + "-" + (msg.getFecha().getMonth()+1) + "-" + (msg.getFecha().getYear()+1900) + " " + msg.getFecha().getHours() + ":0" + msg.getFecha().getMinutes());
				}else{
					arrayHours[i] = new JTextArea(msg.getFecha().getDate() + "-" + (msg.getFecha().getMonth()+1) + "-" + (msg.getFecha().getYear()+1900) + " " + msg.getFecha().getHours() + ":" + msg.getFecha().getMinutes());
				}
				arrayHours[i].setFont(new Font("Monospaced", Font.BOLD, 13));
				arrayHours[i].setForeground(new Color(30, 144, 255));
				arrayHours[i].setBorder(null);
				arrayHours[i].setBackground(Color.white);
				arrayHours[i].setEditable(false);
				arrayHours[i].setBounds(412, 60, 152, 18);
				arrayPaneles[i].add(arrayHours[i]);
			   i++;
	        }
		}catch (Exception e1){
			e1.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	newsScroll.getVerticalScrollBar().setValue(0);
            }
        });
		
	}

}

