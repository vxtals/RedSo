package client;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import javax.swing.JTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;


public class ErrorMsg extends JDialog {

	private static final long serialVersionUID = 3554503326555142066L;

	static final int WRONG_PASS = 0;
	static final int DUP_RMI = 1;
	static final int PASS_MATCH = 2;
	static final int DUPLICATE_NAME = 3;
	static final int CHANGES = 4;
	static final int INCOMPLETE = 5;
	static final int CONF_FILE = 6;
	static final int REFUSED = 7;

	public ErrorMsg(Point location, int errorCode) {


		String errorMsg = null;

		switch(errorCode){
		case WRONG_PASS:
			errorMsg = "Las contrase\u00F1as no coinciden";
			break;
		case DUP_RMI:
			errorMsg = "Hay otro servidor RMI en la misma m\u00E1quina, no funcionar\u00E1n las notificaciones.";
			break;
		case PASS_MATCH:
			errorMsg = "Las contrase\u00F1as no coinciden";
			break;
		case DUPLICATE_NAME:
			errorMsg = "El nombre de usuario est\u00E1 ocupado";
			break;
		case CHANGES:
			errorMsg = "Reinicie la sesi\u00F3n para ver los cambios";
			break;
		case INCOMPLETE:
			errorMsg = "Alg\u00FAn campo requerido no ha sido completado";
			break;
		case CONF_FILE:
			errorMsg = "Error al leer el archivo de configuraci\u00F3n 'redso.conf'";
			break;
		case REFUSED:
			errorMsg = "El servidor rechaz\u00F3 la conexi\u00F3n, compruebe el archivo de configuraci\u00F3n 'redso.conf'";
			break;
		}
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		setResizable(false);
		this.setAlwaysOnTop(true);
		setSize(600, 258);
		setLocation(location.x - 217, location.y - 129);
		this.setVisible(true);

		JPanel contentPanel = (JPanel)getContentPane();
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setLayout(new BorderLayout());

		JPanel panelTop = new JPanel();
		panelTop.setBackground(new Color(240, 248, 255));
		panelTop.setPreferredSize(new Dimension(600, 70));
		contentPanel.add(panelTop, BorderLayout.NORTH);
		
		JPanel panelLeft = new JPanel();
		panelLeft.setBackground(new Color(240, 248, 255));
		panelLeft.setPreferredSize(new Dimension(70, 200));
		contentPanel.add(panelLeft, BorderLayout.WEST);

		JPanel panelRight = new JPanel();
		panelRight.setBackground(new Color(240, 248, 255));
		panelRight.setPreferredSize(new Dimension(70, 200));
		contentPanel.add(panelRight, BorderLayout.EAST);

		JTextArea lblNewLabel = new JTextArea(errorMsg);
		lblNewLabel.setBackground(new Color(240, 248, 255));
		lblNewLabel.setEditable(false);  
		lblNewLabel.setCursor(null);  
		lblNewLabel.setOpaque(false);  
		lblNewLabel.setFocusable(false);
		lblNewLabel.setLineWrap(true);
		lblNewLabel.setWrapStyleWord(true);
		lblNewLabel.setPreferredSize(new Dimension(500, 200));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPanel.add(lblNewLabel, BorderLayout.CENTER);

		JButton okButton = new JButton("Aceptar");
		contentPanel.add(okButton, BorderLayout.SOUTH);
		okButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
		switch(errorCode){
		default:
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}

		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);

	}



}
