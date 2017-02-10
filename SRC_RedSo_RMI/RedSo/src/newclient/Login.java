package newclient;
import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;
import javax.swing.*;

import server.ConexionInterfaz;

import java.net.ConnectException;
import java.rmi.registry.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.Image;
import java.io.UnsupportedEncodingException;


public class Login extends JFrame {

	private static final long serialVersionUID = -506809994509189767L;
	
	private JFrame loginFrame;
	private JPanel contentPane;
	private JTextField user_field;
	private JPasswordField pass_field;
	 
	public Login(Point location) {
		
		loginFrame = this;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("iconoRS.png"));
		this.setIconImage(img);
		this.setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 248, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.setContentPane(contentPane);
		this.setSize(600, 400);
		this.setLocation(location.x - 300, location.y - 200);
		this.setVisible(true);
/*
 * 	PANEL SUPERIOR: Contiene el nombre del programa.		
 */
		JPanel top_panel = new JPanel();
		top_panel.setBackground(new Color(30, 144, 255));
		top_panel.setPreferredSize(new Dimension(10, 60));
		contentPane.add(top_panel, BorderLayout.NORTH); 
		top_panel.setLayout(null);
		
		JLabel title_label = new JLabel("RedSo");
		title_label.setForeground(Color.BLACK);
		title_label.setFont(Fuentes.title_font);
		title_label.setBackground(new Color(30, 144, 255));
		title_label.setBounds(238, 0, 113, 56);
		top_panel.add(title_label);
/*
 *  PANEL IZQUIERDO: Vacío, necesario para que el central se centre.
 */
		JPanel panel_left = new JPanel();
		panel_left.setBackground(new Color(240, 248, 255));
		panel_left.setMinimumSize(new Dimension(0, 0));
		panel_left.setPreferredSize(new Dimension(150, 10));
		contentPane.add(panel_left, BorderLayout.WEST);
		panel_left.requestFocus();
/*
 *  PANEL INFERIOR: Contiene información de la versión.
 */  
		JPanel botton_panel = new JPanel();
		botton_panel.setBackground(new Color(240, 248, 255));
		botton_panel.setPreferredSize(new Dimension(600, 40));
		contentPane.add(botton_panel, BorderLayout.SOUTH);
		
		JLabel info_label = new JLabel("ver.1.0 @ Victor Portals Lorenzo - ETSIINF UPM");
		info_label.setVisible(true);
		info_label.setBounds(10, 275, 76, 14);
		botton_panel.add(info_label);
		this.setFocusable(true);
/*
 *  PANEL CENTRAL: Contiene el panel con los campos y botones.
 */
		JPanel central_panel = new JPanel();
		contentPane.add(central_panel, BorderLayout.CENTER); 
		central_panel.setLayout(new BoxLayout(central_panel, BoxLayout.Y_AXIS));
		central_panel.setBackground(new Color(240, 248, 255));
/*
 *  PANEL CAMPOS: Contiene los campos de usuario contraseño, y los botones de login y registro.
 */
		JPanel fields_panel = new JPanel();
		fields_panel.setVisible(true);
		fields_panel.setBackground(new Color(240, 248, 255));
		central_panel.add(fields_panel);
		fields_panel.setLayout(null);
/*
 *  CAMPO USUARIO: Dónde se introduce el nombre de usuario/email
 */
		
		user_field = new JTextField();
		user_field.setVisible(true);
		user_field.setFont(Fuentes.calibri_font_campo);
		user_field.setText("Usuario");
		user_field.setBounds(53, 31, 171, 30);
		user_field.setBorder(new LineBorder(new Color(30, 144, 255)));
		fields_panel.add(user_field);
		user_field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (user_field.getText().compareTo("Usuario") == 0)
				user_field.setText("");
			}
		});
		user_field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(user_field.getText().length() == 0){
					user_field.setText("Usuario");
				}
			}
		});
		user_field.setColumns(10);
		
/*
 *  CAMPO PASSWORD: Donde se introduce la contraseña de usuario
 */
		
		pass_field = new JPasswordField();
		pass_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					gotoWall();
				}

			}
		});
		pass_field.setVisible(true);
		pass_field.setFont(Fuentes.calibri_font_campo);
		pass_field.setText("Contrase\u00f1a");
		pass_field.setBounds(53, 81, 171, 30);
		pass_field.setBorder(new LineBorder(new Color(30, 144, 255)));
		pass_field.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (pass_field.getText().compareTo("Contrase\u00f1a") == 0){
				pass_field.setText("");
				pass_field.setEchoChar('\u25CF');
				}
			}
		});
		pass_field.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if(pass_field.getText().length() == 0){
					pass_field.setText("Contrase\u00f1a");
					pass_field.setEchoChar((char)0);
				}
			}
		});
		pass_field.setEchoChar((char)0);
		pass_field.setText("Contrase\u00f1a");
		fields_panel.add(pass_field);
		
/*
 *  BOTÓN ENTRAR: Acceso al sistema con usuario y contraseña.
 */
		
		JButton login_btn = new JButton("Entrar");
		login_btn.setVisible(true);
		login_btn.setBackground(new Color(102, 204, 51));
		login_btn.setForeground(Color.white);
		login_btn.setFont(Fuentes.button_font);
		login_btn.setBounds(53, 132, 171, 35);
		login_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gotoWall();
			}
		});
		fields_panel.add(login_btn);
		
/*
 *  BOTÓN REGISTRARSE: Acceso al registro de un nuevo usuario.
 */
		
		JButton register_btn = new JButton("Registrarse");
		register_btn.setVisible(true);
		register_btn.setForeground(Color.WHITE);
		register_btn.setFont(Fuentes.button_font);
		register_btn.setBackground(new Color(255, 69, 0));
		register_btn.setBounds(53, 174, 171, 35);
		register_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				contentPane.removeAll();
				contentPane.repaint();
				loginFrame.dispose();
				Point pos = new Point(loginFrame.getX() + (loginFrame.getSize().width/2) , loginFrame.getY() + (loginFrame.getSize().height/2));
				new Register(pos);
			}
		});
		fields_panel.add(register_btn);
	}
	
	private void gotoWall(){
		String user_email = user_field.getText();
		//MainClient.email_sesion = email;
		@SuppressWarnings("deprecation")
		String password=pass_field.getText();
		MessageDigest sha256;
		try {
			sha256 = MessageDigest.getInstance("SHA-256");
			sha256.update(password.getBytes("UTF-8"));
			byte[] digest = sha256.digest();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<digest.length;i++){
				sb.append(String.format("%02x", digest[i]));
			}
			String hash=sb.toString();
			try{
				Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
				ConexionInterfaz stub = (ConexionInterfaz) registry.lookup("Conexion");
				boolean ok_login = stub.iniciarSesion(user_email, hash);
				if (ok_login){
					contentPane.removeAll();
					contentPane.repaint();
					loginFrame.dispose();
					Point pos = new Point(loginFrame.getX() + (loginFrame.getSize().width/2) , loginFrame.getY() + (loginFrame.getSize().height/2));
					new MainWindow(pos, new Dimension(1000,700), user_email);
				}else{
					Point pos = new Point(this.getX() + (this.getSize().width/2) , this.getY() + (this.getSize().height/2));
					new ErrorMsg(pos, 0);
				}
			} catch (ConnectException e){
				Point pos = new Point(this.getX() + (this.getSize().width/2) , this.getY() + (this.getSize().height/2));
				new ErrorMsg(pos, 7);
			} catch (Exception e1){
				e1.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

	}
	
}
