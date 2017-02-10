package newclient;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import server.Message;
import server.SesionInterfaz;

@SuppressWarnings("deprecation")
public class WallPanel extends JPanel {
	
	private static final long serialVersionUID = -9113896668942653052L;
	
	private JTextArea txtNewState;
	private LimitedPlainDocument doc;
	private JLabel remaininglabel;
	private String email_user;
	
	private JPanel messages_panel;
	private JScrollPane wallScroll;
	
	public WallPanel(String email){
		
		this.email_user = email;
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
		
		JPanel panelNewState = new JPanel();
		panelNewState.setBackground(Color.WHITE);
		panelNewState.setMinimumSize(new Dimension(600, 100));
		panelNewState.setMaximumSize(new Dimension(600, 100));
		panelNewState.setPreferredSize(new Dimension(600, 100));
		panelCentral.add(panelNewState);

		txtNewState = new JTextArea(2,70);
		txtNewState.setLineWrap(true);
		txtNewState.setWrapStyleWord(true);
		doc = new LimitedPlainDocument(138);
        doc.addDocumentListener(new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void removeUpdate(DocumentEvent e) {updateCount(); }
        });
        txtNewState.setText("Nuevo estado");
		txtNewState.setDocument(doc);
		txtNewState.setRows(3);
		txtNewState.setFont(new Font("Calibri", Font.PLAIN, 16));
		txtNewState.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(51, 153, 255)));
		txtNewState.setBounds(41, 11, 391, 63);
		txtNewState.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (txtNewState.getText().compareTo("Nuevo estado") == 0)
					txtNewState.setText("");
			}
		});
		txtNewState.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtNewState.getText().length() == 0){
					txtNewState.setText("Nuevo estado");
				}
			}
		});
		
		panelNewState.setLayout(null);
		panelNewState.add(txtNewState);

		JButton btnSend = new JButton("Enviar");
		btnSend.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(102, 204, 0)));
		btnSend.setForeground(new Color(0, 0, 0));
		btnSend.setBackground(new Color(102, 204, 102));
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
					SesionInterfaz stub = (SesionInterfaz) registry.lookup(email_user);
					stub.publishMessage(txtNewState.getText());
					doc.resetLine();
					txtNewState.setText("Nuevo estado");
					txtNewState.setCaretPosition(0);
					cargarMensaje();
				} catch (Exception e1){
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(452, 11, 99, 63);
		panelNewState.add(btnSend);
		
		remaininglabel = new JLabel("138 caracteres restantes");
		remaininglabel.setFont(new Font("Calibri", Font.PLAIN, 11));
		remaininglabel.setForeground(new Color(30, 144, 255));
		remaininglabel.setBackground(Color.white);
		remaininglabel.setBounds(67, 73, 244, 14);
		panelNewState.add(remaininglabel);

		messages_panel = new JPanel();
		messages_panel.setBackground(Color.white);
		messages_panel.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(30, 144, 255)));
		messages_panel.setLayout(new BoxLayout(messages_panel, BoxLayout.PAGE_AXIS));

    	wallScroll = new JScrollPane(messages_panel,
    			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	wallScroll.setBackground(Color.white);
    	wallScroll.setMinimumSize(new Dimension(600, 420));
    	wallScroll.setMaximumSize(new Dimension(600, 420));
    	wallScroll.setPreferredSize(new Dimension(600, 420));
		panelCentral.add(wallScroll);
		
    	cargarMensaje();
	}
	
	
	private void cargarMensaje(){
		Iterator<Message> iterator;
		List<Message> listaMensajes;
		
		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email_user);
		    listaMensajes = stub.getMessages(email_user);
		    iterator = listaMensajes.iterator();
		    messages_panel.setPreferredSize(new Dimension(600, listaMensajes.size()*80));
		    wallScroll.revalidate();
		    JPanel[] arrayPaneles = new JPanel[listaMensajes.size()];
		    JTextArea[] arrayMessages = new JTextArea[listaMensajes.size()];
		    JTextArea[] arrayHours = new JTextArea[listaMensajes.size()];
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
				arrayMessages[i].setBounds(10, 4, 554, 60);
				arrayPaneles[i].add(arrayMessages[i]);
				
				
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
            	wallScroll.getVerticalScrollBar().setValue(0);
            }
        });
		
	}
	
	private void updateCount()
    {
		
        remaininglabel.setText((138 -doc.getLength()) + " caracteres restantes");
        if (doc.getLength() > 118){
        	remaininglabel.setForeground(Color.WHITE);
        }else {
        	remaininglabel.setForeground(new Color(30, 144, 255));
        }
    }

}
