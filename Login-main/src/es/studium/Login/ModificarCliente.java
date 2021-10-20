package es.studium.Login;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificarCliente implements WindowListener, ActionListener
{
	Frame ventana = new Frame("Editar Cliente");
	Label lblCliente = new Label("Elegir Cliente");
	List listClientes = new List(8, false);
	Button btnEditar = new Button("Editar");
	Button btnCancelar = new Button("Cancelar");

	Frame ventanaEdicion = new Frame("Editando Cliente");
	Label lblIdCliente = new Label("idCliente:");
	Label lblNombreCliente = new Label("Nombre:");
	Label lblCifCliente = new Label("Cif:");
	TextField txtId = new TextField(20);
	TextField txtNombre = new TextField(20);
	TextField txtCif = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar2 = new Button("Cancelar");

	Dialog dlgMensajeModificacionCliente = new Dialog(ventanaEdicion, 
			"Confirmación", true);
	Label lblMensaje = new Label("Modificación de Cliente Correcta");

	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificarCliente()
	{
		ventana.setLayout(new FlowLayout());
		// Listeners
		ventana.addWindowListener(this);
		btnEditar.addActionListener(this);
		btnCancelar.addActionListener(this);
		ventanaEdicion.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar2.addActionListener(this);
		dlgMensajeModificacionCliente.addWindowListener(this);
		
		ventana.add(lblCliente);
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
			listClientes.removeAll();
			while(rs.next())
			{
				listClientes.add(rs.getInt("idCliente")
						+"-"+rs.getString("nombreCliente")
						+"-"+rs.getString("cifCliente"));
			}
		}
		catch (SQLException sqle)
		{

		}
		ventana.add(listClientes);
		ventana.add(btnEditar);
		ventana.add(btnCancelar);
		ventana.setSize(200,250);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evento)
	{
		// TODO Auto-generated method stub
		if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		else if(evento.getSource().equals(btnCancelar2))
		{
			ventana.setVisible(false);
			ventanaEdicion.setVisible(false);
		}
		else if(evento.getSource().equals(btnEditar))
		{
			ventanaEdicion.setLayout(new FlowLayout());
			
			// Capturar los datos del elemento elegido del List
			String[] valores = listClientes.getSelectedItem().split("-");
			ventanaEdicion.add(lblIdCliente);
			txtId.setEnabled(false);
			txtId.setText(valores[0]);
			ventanaEdicion.add(txtId);
			ventanaEdicion.add(lblNombreCliente);
			txtNombre.setText(valores[1]);
			ventanaEdicion.add(txtNombre);
			ventanaEdicion.add(lblCifCliente);
			txtCif.setText(valores[2]);
			ventanaEdicion.add(txtCif);
			ventanaEdicion.add(btnAceptar);
			ventanaEdicion.add(btnCancelar2);

			ventanaEdicion.setSize(200,250);
			ventanaEdicion.setResizable(false);
			ventanaEdicion.setLocationRelativeTo(null);
			
			ventanaEdicion.setVisible(true);
		}
		else if(evento.getSource().equals(btnAceptar))
		{
			// Conectar
			bd = new BaseDatos();
			connection = bd.conectar();
			// Hacer un SELECT * FROM clientes
			sentencia = "UPDATE clientes SET nombreCliente='"
					+txtNombre.getText()+"', cifCliente='"
					+txtCif.getText()+"' WHERE idCliente="
					+txtId.getText();
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				statement.executeUpdate(sentencia);
				lblMensaje.setText("Actualización de cliente Correcta");
			}
			catch (SQLException sqle)
			{
				lblMensaje.setText("Error");
			}
			finally
			{
				dlgMensajeModificacionCliente.setLayout(new FlowLayout());
				
				dlgMensajeModificacionCliente.setSize(150,100);
				dlgMensajeModificacionCliente.setResizable(false);
				dlgMensajeModificacionCliente.setLocationRelativeTo(null);
				dlgMensajeModificacionCliente.add(lblMensaje);
				dlgMensajeModificacionCliente.setVisible(true);
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
		ventana.setVisible(false);
		ventanaEdicion.setVisible(false);
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
