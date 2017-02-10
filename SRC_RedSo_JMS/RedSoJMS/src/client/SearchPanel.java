package client;

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

public class SearchPanel extends JPanel {
	
	private static final long serialVersionUID = -3675458748151318946L;

	private JPanel results_panel;
	private JScrollPane searchScroll;
	
	private String busqueda;
	private String email;
	private MainWindow mainFrame;
	
	SearchPanel(MainWindow mainFrame, String busqueda){
		
		this.mainFrame = mainFrame;
		email = mainFrame.getEmail();
		
		this.busqueda = busqueda;
		
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
		
		results_panel = new JPanel();
		results_panel.setBackground(Color.white);
		results_panel.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(30, 144, 255)));
		results_panel.setLayout(new BoxLayout(results_panel, BoxLayout.PAGE_AXIS));

    	searchScroll = new JScrollPane(results_panel,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    	searchScroll.setBackground(Color.white);
    	searchScroll.setMinimumSize(new Dimension(600, 520));
    	searchScroll.setMaximumSize(new Dimension(600, 520));
    	searchScroll.setPreferredSize(new Dimension(600, 520));
		panelCentral.add(searchScroll);
		
    	cargarResultados();
    	
		
	}
	
	private void cargarResultados(){
		List<User> listaResultados;
		User userTemp;
		Iterator<User> iterator;
		
		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
		    listaResultados = stub.getResults(busqueda);
		    iterator = listaResultados.iterator();
		    JTextField[] arrayNombres = new JTextField[listaResultados.size()];
			JButton[] arrayMuro = new JButton[listaResultados.size()];
			JButton[] arrayAmigo = new JButton[listaResultados.size()];
			JPanel[] arrayPaneles = new JPanel[listaResultados.size()];
		    results_panel.setPreferredSize(new Dimension(600, listaResultados.size()*80));
		    results_panel.setMaximumSize(new Dimension(600, listaResultados.size()*80));
		    searchScroll.revalidate();
		    results_panel.removeAll();
		    int i = 0;
			while (iterator.hasNext()){
				arrayPaneles[i] = new JPanel();
				arrayPaneles[i].setBackground(Color.WHITE);
				arrayPaneles[i].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(51, 153, 255)));
				arrayPaneles[i].setMinimumSize(new Dimension(600,80));
				arrayPaneles[i].setMaximumSize(new Dimension(600,80));
				arrayPaneles[i].setPreferredSize(new Dimension(600,80));
				results_panel.add(arrayPaneles[i]);
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
				arrayMuro[i].setVisible(false);
				arrayMuro[i].setEnabled(false);
				arrayMuro[i].setForeground(Color.black);
				arrayMuro[i].setBounds(340, 11, 100, 28);
				arrayPaneles[i].add(arrayMuro[i]);
				
				arrayAmigo[i] = new JButton("+ Amigo");
				arrayAmigo[i].setEnabled(false);
				arrayAmigo[i].setVisible(false);
				arrayAmigo[i].setFont(new Font("Tahoma", Font.BOLD, 11));
				arrayAmigo[i].setForeground(Color.white);
				arrayAmigo[i].setBackground(new Color(30, 144, 255));
				arrayAmigo[i].setBounds(462, 11, 100, 28);
				arrayPaneles[i].add(arrayAmigo[i]);
				
				
				arrayNombres[i].setText(userTemp.getNombre() + " " + userTemp.getApellidos());
				arrayMuro[i].setVisible(true);
				arrayAmigo[i].setVisible(true);
				arrayPaneles[i].setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(30, 144, 255)));
				try{
				    boolean[] amistad = stub.getFriendship(userTemp.getEmail());
				    if(userTemp.getPublico() || amistad[1] == true || userTemp.getEmail().compareTo(email) == 0){
				    	arrayMuro[i].setEnabled(true);
					}
				    if(amistad[0] == false && !(userTemp.getEmail().compareTo(email) == 0)){
				    	arrayAmigo[i].setEnabled(true);
					}else{
						arrayAmigo[i].setEnabled(false);
					}
				}catch (Exception e1){
					e1.printStackTrace();
				}
				arrayMuro[i].setName(userTemp.getEmail());
				arrayMuro[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						mainFrame.panelCenter.removeAll();
						JButton pressed = (JButton)arg0.getSource();
						JPanel panelWall = new VisitedPanel(mainFrame, pressed.getName());
						mainFrame.panelCenter.add(panelWall);
						mainFrame.panelCenter.revalidate();
					}
				});
				arrayAmigo[i].setName(userTemp.getEmail());
				arrayAmigo[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try{
							Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
							SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
							JButton pressed = (JButton)arg0.getSource();
						    stub.addFriend(pressed.getName());
						    mainFrame.showSearch(busqueda);
						}catch (Exception e1){
							e1.printStackTrace();
						}
					}
				});
			   i++;
	        }
		}catch (Exception e1){
			e1.printStackTrace();
		}
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	searchScroll.getVerticalScrollBar().setValue(0);
            }
        });
		
	}
	
	

}
