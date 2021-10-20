package es.studium.Login;

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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login implements WindowListener, ActionListener
{
	Frame ventanaLogin = new Frame("Login");
	Dialog dialogoLogin = new Dialog(ventanaLogin, "Error", true); // Es modal
	
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:");
	Label lblError = new Label("Credenciales Incorrectas!!!!");
	TextField txtUsuario = new TextField(20);
	TextField txtClave = new TextField(20);
	Button btnAceptar = new Button("Acceder");
	Button btnLimpiar = new Button("Limpiar");
	
	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	
	public Login()
	{
		ventanaLogin.setLayout(new FlowLayout());
		
		ventanaLogin.add(lblUsuario);
		txtUsuario.setText("admin");
		ventanaLogin.add(txtUsuario);
		ventanaLogin.add(lblClave);
		txtClave.setEchoChar('*');
		txtClave.setText("Studium2020;");
		ventanaLogin.add(txtClave);
		btnAceptar.addActionListener(this);
		ventanaLogin.add(btnAceptar);
		btnLimpiar.addActionListener(this);
		ventanaLogin.add(btnLimpiar);
		ventanaLogin.addWindowListener(this);
		
		ventanaLogin.setSize(250,125);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setResizable(false);
		ventanaLogin.setVisible(true);
	}

	public static void main(String[] args)
	{
		new Login();
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
			bd = new BaseDatos();
			connection = bd.conectar();
			// Buscar lo que el usuario ha escrito en los TextField
			sentencia = "SELECT * FROM usuarios WHERE nombreUsuario='"
			+txtUsuario.getText()+"' AND claveUsuario = SHA2('"
					+txtClave.getText()+"',256);";
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs = statement.executeQuery(sentencia);
				if(rs.next()) // Si ha encontrado algo
				{
					// Si existe en la BD, mostrar Menú Principal
					int tipo = rs.getInt("tipoUsuario");
					new MiSoftware(tipo);
				}
				else // Si no encuentra nada
				{
					// Si no existe en la BD, mostrar Diálogo de Error
					dialogoLogin.setLayout(new FlowLayout());
					dialogoLogin.add(lblError);
					dialogoLogin.addWindowListener(this);
					dialogoLogin.setSize(180,75);
					dialogoLogin.setLocationRelativeTo(null);
					dialogoLogin.setResizable(false);
					dialogoLogin.setVisible(true);
				}
			}
			catch (SQLException sqle)
			{
			}
			
			// Desconectar la BD
			bd.desconectar(connection);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		if(dialogoLogin.isActive())
		{
			dialogoLogin.setVisible(false);
		}
		else
		{
			System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{}

	@Override
	public void windowIconified(WindowEvent arg0)
	{}

	@Override
	public void windowOpened(WindowEvent arg0)
	{}
}
