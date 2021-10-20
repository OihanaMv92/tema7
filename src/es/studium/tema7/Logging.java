package es.studium.tema7;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;

public class Logging implements WindowListener, ActionListener // para que se cierre
{
	 Frame ventanaLogin = new Frame("Loging"); // ventana 
	 Dialog dialogoLogin= new Dialog(ventanaLogin,"Error", true); // true es modal cuando aparece hasta que no lo cierre no puedo hacer nada mas 
	 // Dialogo es la segunda evntana de error. por que el usuario no esta registrado.
	 Label lblUsuario = new Label("Usuario");
	 Label lblClave = new Label("Clave");//Etiqueta
	 Label lblError = new Label("Credenciales Incorrectas!!!");
	 TextField txtClave = new TextField(20); //texto sin modificar
	 TextField txtUsuario = new TextField(20);
	 Button btnAceptar = new Button("Acceder"); //Botones
	 Button btnLimpiar = new Button("Limpiar");
	 
	 // conectar bd
	 String driver = "com.mysql.cj.jdbc.Driver";
     String url = "jdbc:mysql://localhost:3306/misoftware?serverTimezone=UTC";
     String login = "root";
     String password = "Studium2020";
     String sentencia = "";
     Connection connection = null;
     Statement statement = null;
     ResultSet rs = null;
	 
	public Logging()
	{
		ventanaLogin.setLayout(new FlowLayout());
		// añadimos ala ventana todo lo que necesita
		ventanaLogin.add(lblUsuario);
		ventanaLogin.add(txtUsuario);
		ventanaLogin.add(lblClave);
		txtClave.setEchoChar('*'); // para que la contraseña no se vea.
		ventanaLogin.add(txtClave);
		btnAceptar.addActionListener(this);
		ventanaLogin.add(btnAceptar);
		btnLimpiar.addActionListener(this);
		ventanaLogin.add(btnLimpiar);
		ventanaLogin.addWindowListener(this); // para poder cerrar
		
		
		ventanaLogin.setSize(250,125); //tamaño ventana ancho por alto
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setResizable(false);
		ventanaLogin.setVisible(true);
	}
	 
	 public static void main(String[] args)
	{
		new Logging();
	}

	@Override
	public void actionPerformed(ActionEvent botonPulsado)
	{
		if(botonPulsado.getSource().equals(btnLimpiar))
		{
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
		}
		else if(botonPulsado.getSource().equals(btnAceptar))
		{
			// Conectar BD
			// Buscar lo que el usuario ha escrito en los TextField
			// Si existe en la BD, mostrar Menú Principal
			// Si no existe en la BD, mostrar Diálogo de Error
			// Desconectar la BD
			   public Connection conectar()
		        {
		                try
		                {
		                        //Cargar los controladores para el acceso a la BD
		                        Class.forName(driver);
		                        //Establecer la conexión con la BD Empresa
		                        connection = DriverManager.getConnection(url, login, password);
		                } catch (ClassNotFoundException cnfe)
		                {
		                        System.out.println("Error 1-" + cnfe.getMessage());
		                } catch (SQLException sqle)
		                {
		                        System.out.println("Error 2-" + sqle.getMessage());
		                }
		                return connection;
		        }
		        
		}
			
		
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		System.exit(0); // para cerrar ventana
	}
	

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{	
	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
		
	}

}
