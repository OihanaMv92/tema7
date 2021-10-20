package es.studium.Login;

import java.awt.Button;
import java.awt.Choice;
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

public class AltaProyecto implements WindowListener, ActionListener
{
	// Atributos o Componentes
	Frame ventanaAltaProyecto = new Frame("Alta de Proyecto");
	Label lblNombre = new Label("Nombre");
	Label lblFechaInicio = new Label("Fecha Inicio");
	Label lblFechaFin = new Label("FechaFin");
	Label lblCliente = new Label("Cliente");
	Choice choClientes = new Choice();
	TextField txtNombre = new TextField(20);
	TextField txtFechaInicio = new TextField(20);
	TextField txtFechaFin = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");

	Dialog dlgMensajeAltaProyecto = new Dialog(ventanaAltaProyecto, 
			"Confirmación", true);
	Label lblMensaje = new Label("Alta de Proyecto Correcta");

	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AltaProyecto()
	{
		ventanaAltaProyecto.setLayout(new FlowLayout());
		ventanaAltaProyecto.add(lblNombre);
		ventanaAltaProyecto.add(txtNombre);
		ventanaAltaProyecto.add(lblFechaInicio);
		ventanaAltaProyecto.add(txtFechaInicio);
		ventanaAltaProyecto.add(lblFechaFin);
		ventanaAltaProyecto.add(txtFechaFin);
		ventanaAltaProyecto.add(lblCliente);
		// Rellenar el Choice
		// Conectar
		bd = new BaseDatos();
		connection = bd.conectar();
		// Hacer un SELECT * FROM clientes
		sentencia = "SELECT * FROM clientes";
		try
		{
			//Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choClientes.removeAll();
			choClientes.add("Seleccionar un cliente");
			while(rs.next())
			{
				choClientes.add(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente")
						+"-"+rs.getString("cifCliente"));
			}
		}
		catch (SQLException sqle)
		{}
		ventanaAltaProyecto.add(choClientes);
		btnAceptar.addActionListener(this);
		ventanaAltaProyecto.add(btnAceptar);
		btnCancelar.addActionListener(this);
		ventanaAltaProyecto.add(btnCancelar);
		ventanaAltaProyecto.setSize(300,180);
		ventanaAltaProyecto.setResizable(false);
		ventanaAltaProyecto.setLocationRelativeTo(null);
		ventanaAltaProyecto.addWindowListener(this);
		ventanaAltaProyecto.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evento)
	{
		// TODO Auto-generated method stub
		if(evento.getSource().equals(btnCancelar))
		{
			ventanaAltaProyecto.setVisible(false);
		}
		else
		{
			// Conectar
			bd = new BaseDatos();
			connection = bd.conectar();
			try
			{
				sentencia = "INSERT INTO proyectos VALUES(null, '"
						+txtNombre.getText()+"','"
						+txtFechaInicio.getText()+"','"
						+txtFechaFin.getText()+"',"
						+choClientes.getSelectedItem().split("-")[0]
						+")";
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				statement.executeUpdate(sentencia);
			}
			catch (SQLException sqle)
			{
				lblMensaje.setText("Error en Alta");
			}
			finally
			{
				dlgMensajeAltaProyecto.setLayout(new FlowLayout());
				dlgMensajeAltaProyecto.addWindowListener(this);
				dlgMensajeAltaProyecto.setSize(150,100);
				dlgMensajeAltaProyecto.setResizable(false);
				dlgMensajeAltaProyecto.setLocationRelativeTo(null);
				dlgMensajeAltaProyecto.add(lblMensaje);
				dlgMensajeAltaProyecto.setVisible(true);
				bd.desconectar(connection);
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		// TODO Auto-generated method stub
		ventanaAltaProyecto.setVisible(false);
		dlgMensajeAltaProyecto.setVisible(false);
		ventanaAltaProyecto.removeWindowListener(this);
		dlgMensajeAltaProyecto.removeWindowListener(this);
		btnAceptar.removeActionListener(this);
		btnCancelar.removeActionListener(this);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}
