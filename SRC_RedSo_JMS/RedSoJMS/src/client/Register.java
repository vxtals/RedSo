package client;

import java.rmi.registry.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

import org.hibernate.exception.ConstraintViolationException;

import server.ConexionInterfaz;



public class Register extends JFrame {

	private static final long serialVersionUID = 205495640346239987L;
	
	private JFrame regFrame;
	private JPanel contentPane;
	
	private JTextField txtName;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtWeb;
	private JPasswordField pwdPass;
	private JPasswordField pwdRepPass;
	private JLabel pictureLabel;
	private boolean publico = false;
	private byte[] photo = null;

	public Register(Point location) {

		regFrame = this;
		new Fuentes();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 700);
		this.setLocation(location.x - 300, location.y - 350);
		this.setTitle("RedSo - Registro");
		this.setResizable(false);
		this.setVisible(true);
		
		contentPane = (JPanel)this.getContentPane();
		contentPane.setBackground(new Color(240, 248, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(30, 144, 255));
		panel.setPreferredSize(new Dimension(10, 80));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(null);

		JLabel label = new JLabel("RedSo");
		label.setBounds(223, 11, 113, 56);
		label.setForeground(new Color(0, 0, 0));
		label.setBackground(new Color(30, 144, 255));
		label.setFont(Fuentes.title_font);//.setFont(new Font("Segoe Script", Font.BOLD, 34));
		panel.add(label);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(240, 248, 255));
		panel_2.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_2.setPreferredSize(new Dimension(10, 80));
		contentPane.add(panel_2, BorderLayout.SOUTH);

		JButton btnEnviar = new JButton("Registrarse");
		btnEnviar.setBounds(386, 11, 162, 35);
		btnEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nombre = txtName.getText();
				String apellidos = txtApellidos.getText();
				String email = txtEmail.getText();
				String web = txtWeb.getText();


				if (web.compareTo("Direcci\u00f3n Web") == 0){
					web = "NULL";
				}
				boolean pub = publico;

				if (Arrays.equals(pwdPass.getPassword(), pwdRepPass.getPassword())){
					@SuppressWarnings("deprecation")
					String password=pwdPass.getText();
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
							Registry registry = LocateRegistry.getRegistry(MainClient.host,1099);
							ConexionInterfaz stub = (ConexionInterfaz) registry.lookup("Conexion");
							if(email.equals("Email") || nombre.equals("Nombre") || apellidos.equals("Apellidos")){
								Point pos = new Point(regFrame.getX() + (regFrame.getSize().width/2) , regFrame.getY() + (regFrame.getSize().height/2));
								new ErrorMsg(pos,5);
								return;
							}
							boolean crear_ok = stub.crearUsuario(email, nombre, apellidos, hash, photo, web, pub);
							if (crear_ok){
								contentPane.removeAll();
								contentPane.repaint();
								regFrame.dispose();
								Point pos = new Point(regFrame.getX() + (regFrame.getSize().width/2) , regFrame.getY() + (regFrame.getSize().height/2));
								new MainWindow(pos,new Dimension(1000,700), email);
							}
							//passwd = null;
						}catch (ConstraintViolationException e){
							Point pos = new Point(regFrame.getX() + (regFrame.getSize().width/2) , regFrame.getY() + (regFrame.getSize().height/2));
							new ErrorMsg(pos,3);
						} catch (Exception e){
							e.printStackTrace();
						}
					} catch (NoSuchAlgorithmException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					Point pos = new Point(regFrame.getX() + (regFrame.getSize().width/2) , regFrame.getY() + (regFrame.getSize().height/2));
					new ErrorMsg(pos,2);
				}

			}
		});
		panel_2.setLayout(null);
		btnEnviar.setForeground(Color.WHITE);
		btnEnviar.setBackground(new Color(102, 204, 51));
		btnEnviar.setFont(Fuentes.button_font);//.setFont(new Font("Tahoma", Font.PLAIN, 22));
		panel_2.add(btnEnviar);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				contentPane.removeAll();
				contentPane.repaint();
				regFrame.dispose();
				Point pos = new Point(regFrame.getX() + (regFrame.getSize().width/2) , regFrame.getY() + (regFrame.getSize().height/2));
				new Login(pos);
			}
		});
		btnVolver.setBackground(new Color(255, 69, 0));
		btnVolver.setForeground(Color.WHITE);
		btnVolver.setFont(Fuentes.button_font);//.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnVolver.setBounds(41, 11, 97, 35);
		panel_2.add(btnVolver);
		btnEnviar.requestFocus();

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(240, 248, 255));
		panel_3.setPreferredSize(new Dimension(275, 10));
		contentPane.add(panel_3, BorderLayout.WEST);

		txtName = new JTextField();
		txtName.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtName.setBounds(31, 81, 187, 30);
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtName.getText().compareTo("Nombre") == 0)
					txtName.setText("");
			}
		});
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtName.getText().length() == 0){
					txtName.setText("Nombre");
				}
			}
		});
		panel_3.setLayout(null);
		txtName.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtName.setText("Nombre");
		panel_3.add(txtName);
		txtName.setColumns(10);

		txtApellidos = new JTextField();
		txtApellidos.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtApellidos.setBounds(31, 170, 187, 30);
		txtApellidos.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtApellidos.getText().compareTo("Apellidos") == 0)
					txtApellidos.setText("");
			}
		});
		txtApellidos.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtApellidos.getText().length() == 0){
					txtApellidos.setText("Apellidos");
				}
			}
		});
		txtApellidos.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtApellidos.setText("Apellidos");
		panel_3.add(txtApellidos);
		txtApellidos.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtEmail.setBounds(31, 259, 187, 30);
		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtEmail.getText().compareTo("Email") == 0)
					txtEmail.setText("");
			}
		});
		txtEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtEmail.getText().length() == 0){
					txtEmail.setText("Email");
				}
			}
		});
		txtEmail.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtEmail.setText("Email");
		panel_3.add(txtEmail);
		txtEmail.setColumns(10);


		pwdRepPass = new JPasswordField();
		pwdRepPass.setBorder(new LineBorder(new Color(30, 144, 255)));
		pwdRepPass.setBounds(31, 437, 187, 30);
		pwdRepPass.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		pwdRepPass.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (pwdRepPass.getText().compareTo("Rep. contrase\u00f1a") == 0){
					pwdRepPass.setText("");
					pwdRepPass.setEchoChar('*');
				}
			}
		});
		pwdRepPass.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if(pwdRepPass.getText().length() == 0){
					pwdRepPass.setText("Rep. contrase\u00f1a");
					pwdRepPass.setEchoChar((char)0);
				}
			}
		});



		pwdPass = new JPasswordField();
		pwdPass.setBorder(new LineBorder(new Color(30, 144, 255)));
		pwdPass.setBounds(31, 348, 187, 30);
		pwdPass.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		pwdPass.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusGained(FocusEvent e) {
				if (pwdPass.getText().compareTo("Contrase\u00f1a") == 0){
					pwdPass.setText("");
					pwdPass.setEchoChar('*');
				}
			}
		});
		pwdPass.addFocusListener(new FocusAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void focusLost(FocusEvent e) {
				if(pwdPass.getText().length() == 0){
					pwdPass.setText("Contrase\u00f1a");
					pwdPass.setEchoChar((char)0);
				}
			}
		});
		pwdPass.setEchoChar((char)0);
		pwdPass.setText("Contrase\u00F1a");
		panel_3.add(pwdPass);
		pwdRepPass.setEchoChar((char)0);
		pwdRepPass.setText("Rep. contrase\u00f1a");
		panel_3.add(pwdRepPass);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(240, 248, 255));
		contentPane.add(panel_4, BorderLayout.CENTER);

		txtWeb = new JTextField();
		txtWeb.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtWeb.setBounds(6, 81, 303, 30);
		txtWeb.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtWeb.setText("Direcci\u00f3n Web");
		txtWeb.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtWeb.getText().compareTo("Direcci\u00f3n Web") == 0)
					txtWeb.setText("");
			}
		});
		txtWeb.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtWeb.getText().length() == 0){
					txtWeb.setText("Direcci\u00f3n Web");
				}
			}
		});
		panel_4.setLayout(null);
		panel_4.add(txtWeb);
		txtWeb.setColumns(10);


		pictureLabel = new JLabel();
		pictureLabel.setBounds(40, 180, 65, 65);
		pictureLabel.setBorder(new LineBorder(new Color(30, 144, 255)));
		//picLabel.setText("Foto");
		pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		panel_4.add(pictureLabel);

		JButton btnBuscarArchivo = new JButton("Buscar");
		btnBuscarArchivo.setVerticalAlignment(JButton.CENTER);
		btnBuscarArchivo.setBackground(Color.white);
		btnBuscarArchivo.setBounds(160, 195, 100, 28);
		btnBuscarArchivo.setFont(Fuentes.button_font);//.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnBuscarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser explorador = new JFileChooser("/home/");

				explorador.setFileFilter(new FileNameExtensionFilter("Imagen JPEG", "jpeg" , "jpg"));
				explorador.setDialogTitle("Buscar imagen...");

				int seleccion = explorador.showOpenDialog(null);

				//analizamos la respuesta
				switch(seleccion) {
				case JFileChooser.APPROVE_OPTION:
					File archivo = explorador.getSelectedFile();


					BufferedImage img_foto;
					try {
						img_foto = ImageIO.read(new FileInputStream(archivo));
						int type = img_foto.getType() == 0? BufferedImage.TYPE_INT_ARGB : img_foto.getType();
						BufferedImage resizedImage = new BufferedImage(65, 65, type);
						Graphics2D g = resizedImage.createGraphics();
						g.drawImage(img_foto, 0, 0, 65, 65, null);
						g.dispose();
						ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
						ImageIO.write(resizedImage, "jpg", bytestream );
						bytestream.flush();
						photo = bytestream.toByteArray();
						bytestream.close();
						pictureLabel.setIcon(new ImageIcon(resizedImage));


					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;

				case JFileChooser.CANCEL_OPTION:
					break;

				case JFileChooser.ERROR_OPTION:
					break;
				}


			}


		});
		panel_4.add(btnBuscarArchivo);


		JCheckBox rdbtnS = new JCheckBox("Quiero que mi muro sea público");
		rdbtnS.setBackground(new Color(240, 248, 255));
		rdbtnS.setBounds(6, 280,300, 33);
		rdbtnS.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(publico == false){
					publico = true;
				}else{
					publico = false;
				}
			}
		});
		panel_4.add(rdbtnS);
		this.requestFocus();







	}
}
