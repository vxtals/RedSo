package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import server.SesionInterfaz;



public class EditPanel extends JPanel {

	private static final long serialVersionUID = -9113896668942653052L;

	private JTextField txtName;
	private JTextField txtApellidos;
	private JLabel txtEmail;
	private JTextField txtWeb;
	private JLabel pictureLabel;
	private byte[] photo = null;

	private String email_user;
	private String[] user_data;

	boolean isPublic;
	private MainWindow mainFrame;

	public EditPanel(MainWindow mainFrame2, String email){
		this.mainFrame = mainFrame2;
		this.email_user = email;

		this.setMaximumSize(new Dimension(1000,700));
		this.setBackground(new Color(30, 144, 255));
		this.setLayout(new BorderLayout(0, 0));

		user_data = null;
		byte[] foto_perfil = null;

		try{
			Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
			SesionInterfaz stub = (SesionInterfaz) registry.lookup(email);
			user_data = stub.getDatos(email);
			isPublic = stub.getPublic(email);
			foto_perfil = stub.getFoto(email);
		} catch (Exception e){
			e.printStackTrace();
		}

		JPanel panelCentral = new JPanel();
		panelCentral.setBackground(new Color(240, 248, 255));
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

		panelCentral.setLayout(null);

		txtName = new JTextField();
		txtName.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtName.setBounds(31, 20, 187, 30);
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtName.getText().compareTo(user_data[1]) == 0)
					txtName.setText("");
			}
		});
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtName.getText().length() == 0){
					txtName.setText(user_data[1]);
				}
			}
		});
		txtName.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtName.setText(user_data[1]);
		panelCentral.add(txtName);
		txtName.setColumns(10);

		txtApellidos = new JTextField();
		txtApellidos.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtApellidos.setBounds(31, 55, 187, 30);
		txtApellidos.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtApellidos.getText().compareTo(user_data[2]) == 0)
					txtApellidos.setText("");
			}
		});
		txtApellidos.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtApellidos.getText().length() == 0){
					txtApellidos.setText(user_data[2]);
				}
			}
		});
		txtApellidos.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		txtApellidos.setText(user_data[2]);
		panelCentral.add(txtApellidos);
		txtApellidos.setColumns(10);

		txtWeb = new JTextField();
		txtWeb.setBorder(new LineBorder(new Color(30, 144, 255)));
		txtWeb.setBounds(31, 90, 303, 30);
		txtWeb.setFont(Fuentes.calibri_font_campo);//.setFont(new Font("Calibri", Font.PLAIN, 24));
		if(user_data[4].compareTo("NULL")==0){
			txtWeb.setText("Dirección web");
			txtWeb.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (txtWeb.getText().compareTo("Dirección web") == 0)
						txtWeb.setText("");
				}
			});
			txtWeb.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if(txtWeb.getText().length() == 0){
						txtWeb.setText("Dirección web");
					}
				}
			});
		}else{
			txtWeb.setText(user_data[4]);
			txtWeb.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (txtWeb.getText().compareTo(user_data[4]) == 0)
						txtWeb.setText("");
				}
			});
			txtWeb.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					if(txtWeb.getText().length() == 0){
						txtWeb.setText(user_data[4]);
					}
				}
			});
		}
		panelCentral.add(txtWeb);
		txtWeb.setColumns(10);

		txtEmail = new JLabel();
		txtEmail.setBounds(31, 125, 187, 30);

		txtEmail.setFont(Fuentes.calibri_font_campo);
		txtEmail.setText("Email: " + user_data[3]);
		panelCentral.add(txtEmail);

		pictureLabel = new JLabel();
		pictureLabel.setBounds(31, 180, 65, 65);
		pictureLabel.setBorder(new LineBorder(new Color(30, 144, 255)));
		pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		panelCentral.add(pictureLabel);

		BufferedImage img_foto;
		try {
			if (foto_perfil != null){
				img_foto = ImageIO.read(new ByteArrayInputStream(foto_perfil));
				pictureLabel.setIcon(new ImageIcon(img_foto));
			}else{
				pictureLabel.setText("Foto");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JButton btnBuscarArchivo = new JButton("Buscar");
		btnBuscarArchivo.setVerticalAlignment(JButton.CENTER);
		btnBuscarArchivo.setBackground(Color.white);
		btnBuscarArchivo.setBounds(110, 212, 100, 28);
		btnBuscarArchivo.setFont(Fuentes.button_font);
		btnBuscarArchivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser explorador = new JFileChooser("/home/");

				explorador.setFileFilter(new FileNameExtensionFilter("Imagen JPEG", "jpeg" , "jpg"));
				explorador.setDialogTitle("Buscar imagen...");

				int seleccion = explorador.showOpenDialog(null);

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
					//dio click en cancelar o cerro la ventana
					break;

				case JFileChooser.ERROR_OPTION:
					//llega aqui si sucede un error
					break;
				}


			}


		});

		JCheckBox rdbtnS = new JCheckBox("Quiero que mi muro sea público");
		rdbtnS.setBackground(new Color(240, 248, 255));
		rdbtnS.setBounds(31,280,300, 33);
		rdbtnS.setSelected(isPublic);
		rdbtnS.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(isPublic == false){
					isPublic = true;
				}else{
					isPublic = false;
				}
			}
		});
		panelCentral.add(rdbtnS);
		panelCentral.add(btnBuscarArchivo);

		JButton btnEnviar = new JButton("Actualizar");
		btnEnviar.setBounds(31, 400, 162, 35);
		btnEnviar.setForeground(Color.WHITE);
		btnEnviar.setBackground(new Color(102, 204, 51));
		btnEnviar.setFont(Fuentes.button_font);//.setFont(new Font("Tahoma", Font.PLAIN, 22));
		btnEnviar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String nombre = txtName.getText();
				String apellidos = txtApellidos.getText();
				String web = txtWeb.getText();

				if (nombre.compareTo(user_data[1]) == 0){
					nombre = null;
				}
				if (apellidos.compareTo(user_data[2]) == 0){
					apellidos = null;
				}
				if (web.compareTo(user_data[4]) == 0){
					web = null;
				}


				try{
					Registry registry = LocateRegistry.getRegistry(MainClient.host, 1099);
					SesionInterfaz stub = (SesionInterfaz) registry.lookup(email_user);
					boolean modify_ok = stub.modifyProfile(nombre, apellidos, web, isPublic, photo);
					if (modify_ok){
						Point pos = new Point(mainFrame.getX() + (mainFrame.getSize().width/2) , mainFrame.getY() + (mainFrame.getSize().height/2));
						new ErrorMsg(pos, 4);
					}
				} catch (Exception e){
					e.printStackTrace();
				}


			}
		});
		panelCentral.add(btnEnviar);


	}

}
