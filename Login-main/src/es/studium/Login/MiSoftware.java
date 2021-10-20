package es.studium.Login;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MiSoftware implements WindowListener, ActionListener
{
	// Ventana Principal
	Frame ventana = new Frame("Mi Software");

	// Ventana Alta de Cliente
	Frame frmAltaCliente = new Frame("Alta de Cliente");
	Label lblNombreCliente = new Label("Nombre:");
	TextField txtNombreCliente = new TextField(20);
	Label lblCifCliente = new Label("Cif:");
	TextField txtCifCliente = new TextField(20);
	Button btnAltaCliente = new Button("Alta");
	Button btnCancelarAltaCliente = new Button("Cancelar");

	Dialog dlgConfirmarAltaCliente = new Dialog(frmAltaCliente, "Alta Cliente", true);
	Label lblMensajeAltaCliente = new Label("Alta de Cliente Correcta");

	// Ventana Consulta de Clientes
	Frame frmConsultaClientes = new Frame("Consulta Clientes");
	TextArea listadoClientes = new TextArea(4, 30);
	Button btnPdfClientes = new Button("PDF");

	// Ventana de Borrado de Cliente
	Frame frmBajaCliente = new Frame("Baja de Cliente");
	Label lblMensajeBajaCliente = new Label("Seleccionar el cliente:");
	Choice choClientes = new Choice();
	Button btnBorrarCliente = new Button("Borrar");
	Dialog dlgSeguroCliente = new Dialog(frmBajaCliente, "¿Seguro?", true);
	Label lblSeguroCliente = new Label("¿Está seguro de borrar?");
	Button btnSiSeguroCliente = new Button("Sí");
	Button btnNoSeguroCliente = new Button("No");
	Dialog dlgConfirmacionBajaCliente = new Dialog(frmBajaCliente, "Baja Cliente", true);
	Label lblConfirmacionBajaCliente = new Label("Baja de cliente correcta");	

	MenuBar mnBar = new MenuBar();

	Menu mnuClientes = new Menu("Clientes");
	MenuItem mniAltaCliente = new MenuItem("Alta");
	MenuItem mniBajaCliente = new MenuItem("Baja");
	MenuItem mniModificacionCliente = new MenuItem("Modificación");
	MenuItem mniConsultaCliente = new MenuItem("Consulta");

	Menu mnuEmpleados = new Menu("Empleados");
	MenuItem mniAltaEmpleado = new MenuItem("Alta");
	MenuItem mniBajaEmpleado = new MenuItem("Baja");
	MenuItem mniModificacionEmpleado = new MenuItem("Modificación");
	MenuItem mniConsultaEmpleado = new MenuItem("Consulta");

	Menu mnuProyectos = new Menu("Proyectos");
	MenuItem mniAltaProyecto = new MenuItem("Alta");
	//MenuItem mniBajaProyecto = new MenuItem("Baja");
	//MenuItem mniModificacionProyecto = new MenuItem("Modificación");
	MenuItem mniConsultaProyecto= new MenuItem("Consulta");

	Menu mnuAsignaciones = new Menu("Asignaciones");
	MenuItem mniAltaAsignacion = new MenuItem("Alta");
	//MenuItem mniBajaAsignacion = new MenuItem("Baja");
	//MenuItem mniModificacionAsignacion = new MenuItem("Modificación");
	MenuItem mniConsultaAsignacion = new MenuItem("Consulta");

	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public MiSoftware(int tipo)
	{
		ventana.setLayout(new FlowLayout());
		mniAltaCliente.addActionListener(this);
		mnuClientes.add(mniAltaCliente);
		if(tipo==0) // ¿Es administrador?
		{
			mniBajaCliente.addActionListener(this);
			mnuClientes.add(mniBajaCliente);
			mniModificacionCliente.addActionListener(this);
			mnuClientes.add(mniModificacionCliente);
			mniConsultaCliente.addActionListener(this);
			mnuClientes.add(mniConsultaCliente);
		}
		mnBar.add(mnuClientes);

		mnuEmpleados.add(mniAltaEmpleado);
		if(tipo==0)
		{
			mnuEmpleados.add(mniBajaEmpleado);
			mnuEmpleados.add(mniModificacionEmpleado);
			mnuEmpleados.add(mniConsultaEmpleado);
		}
		mnBar.add(mnuEmpleados);

		mniAltaProyecto.addActionListener(this);
		mnuProyectos.add(mniAltaProyecto);
		if(tipo==0) // ¿Es administrador?
		{
			//mnuProyectos.add(mniBajaProyecto);
			//mnuProyectos.add(mniModificacionProyecto);
			mnuProyectos.add(mniConsultaProyecto);
		}
		mnBar.add(mnuProyectos);

		mnuAsignaciones.add(mniAltaAsignacion);
		if(tipo==0) // ¿Es administrador?
		{
			//mnuAsignaciones.add(mniBajaAsignacion);
			//mnuAsignaciones.add(mniModificacionAsignacion);
			mnuAsignaciones.add(mniConsultaAsignacion);
		}
		mnBar.add(mnuAsignaciones);

		ventana.setMenuBar(mnBar);

		ventana.setSize(320,160);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
	}

	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowClosing(WindowEvent we)
	{
		if(frmAltaCliente.isActive())
		{
			frmAltaCliente.setVisible(false);
		}
		else if(dlgConfirmarAltaCliente.isActive())
		{
			txtNombreCliente.setText("");
			txtCifCliente.setText("");
			txtNombreCliente.requestFocus();
			dlgConfirmarAltaCliente.setVisible(false);
		}
		else if(frmConsultaClientes.isActive())
		{
			frmConsultaClientes.setVisible(false);
		}
		else if(frmBajaCliente.isActive())
		{
			frmBajaCliente.setVisible(false);
		}
		else if(dlgSeguroCliente.isActive())
		{
			dlgSeguroCliente.setVisible(false);
		}
		else if(dlgConfirmacionBajaCliente.isActive())
		{
			btnSiSeguroCliente.removeActionListener(this);
			btnNoSeguroCliente.removeActionListener(this);
			btnBorrarCliente.removeActionListener(this);
			dlgConfirmacionBajaCliente.setVisible(false);
			dlgSeguroCliente.setVisible(false);
			frmBajaCliente.setVisible(false);
		}
		else
		{
			System.exit(0);
		}
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}
	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(mniAltaCliente))
		{
			frmAltaCliente.setLayout(new FlowLayout());
			frmAltaCliente.add(lblNombreCliente);
			txtNombreCliente.setText("");
			frmAltaCliente.add(txtNombreCliente);
			frmAltaCliente.add(lblCifCliente);
			txtCifCliente.setText("");
			frmAltaCliente.add(txtCifCliente);
			btnAltaCliente.addActionListener(this);
			frmAltaCliente.add(btnAltaCliente);
			btnCancelarAltaCliente.addActionListener(this);
			frmAltaCliente.add(btnCancelarAltaCliente);

			frmAltaCliente.setSize(250,140);
			frmAltaCliente.setResizable(false);
			frmAltaCliente.setLocationRelativeTo(null);
			frmAltaCliente.addWindowListener(this);
			txtNombreCliente.requestFocus();
			frmAltaCliente.setVisible(true);
		}
		else if(evento.getSource().equals(btnAltaCliente))
		{
			bd = new BaseDatos();
			connection = bd.conectar();
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				if(((txtNombreCliente.getText().length())!=0)
						&&((txtCifCliente.getText().length())!=0))
				{
					sentencia = "INSERT INTO clientes VALUES (null, '"
							+ txtNombreCliente.getText()
							+ "', '" +txtCifCliente.getText() + "')";
					statement.executeUpdate(sentencia);
					lblMensajeAltaCliente.setText("Alta de Cliente Correcta");
				}
				else
				{
					lblMensajeAltaCliente.setText("Faltan datos");
				}
			}
			catch (SQLException sqle)
			{
				lblMensajeAltaCliente.setText("Error en ALTA");
			}
			finally
			{
				dlgConfirmarAltaCliente.setLayout(new FlowLayout());
				dlgConfirmarAltaCliente.addWindowListener(this);
				dlgConfirmarAltaCliente.setSize(150,100);
				dlgConfirmarAltaCliente.setResizable(false);
				dlgConfirmarAltaCliente.setLocationRelativeTo(null);
				dlgConfirmarAltaCliente.add(lblMensajeAltaCliente);
				dlgConfirmarAltaCliente.setVisible(true);
				bd.desconectar(connection);
			}
		}
		else if(evento.getSource().equals(mniConsultaCliente))
		{
			frmConsultaClientes.setLayout(new FlowLayout());
			// Conectar
			bd = new BaseDatos();
			connection = bd.conectar();
			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM clientes";
			// La información está en ResultSet
			// Recorrer el RS y por cada registro,
			// meter una línea en el TextArea
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				rs = statement.executeQuery(sentencia);
				listadoClientes.selectAll();
				listadoClientes.setText("");
				listadoClientes.append("id\tNombre\tCif\n");
				while(rs.next())
				{
					listadoClientes.append(rs.getInt("idCliente")
							+"\t"+rs.getString("nombreCliente")
							+"\t"+rs.getString("cifCliente")
							+"\n");
				}
			}
			catch (SQLException sqle)
			{
				listadoClientes.setText("Se ha producido un error en la consulta");
			}
			finally
			{

			}
			listadoClientes.setEditable(false);
			frmConsultaClientes.add(listadoClientes);
			frmConsultaClientes.add(btnPdfClientes);

			frmConsultaClientes.setSize(250,140);
			frmConsultaClientes.setResizable(false);
			frmConsultaClientes.setLocationRelativeTo(null);
			frmConsultaClientes.addWindowListener(this);
			frmConsultaClientes.setVisible(true);
		}
		else if(evento.getSource().equals(mniBajaCliente))
		{
			frmBajaCliente.setLayout(new FlowLayout());
			frmBajaCliente.add(lblMensajeBajaCliente);
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
				while(rs.next())
				{
					choClientes.add(rs.getInt("idCliente")
							+"-"+rs.getString("nombreCliente")
							+"-"+rs.getString("cifCliente"));
				}
			}
			catch (SQLException sqle)
			{

			}
			frmBajaCliente.add(choClientes);
			btnBorrarCliente.addActionListener(this);
			frmBajaCliente.add(btnBorrarCliente);

			frmBajaCliente.setSize(250,140);
			frmBajaCliente.setResizable(false);
			frmBajaCliente.setLocationRelativeTo(null);
			frmBajaCliente.addWindowListener(this);
			frmBajaCliente.setVisible(true);
		}
		else if(evento.getSource().equals(btnBorrarCliente))
		{
			dlgSeguroCliente.setLayout(new FlowLayout());
			dlgSeguroCliente.addWindowListener(this);
			dlgSeguroCliente.setSize(150,100);
			dlgSeguroCliente.setResizable(false);
			dlgSeguroCliente.setLocationRelativeTo(null);
			dlgSeguroCliente.add(lblSeguroCliente);
			btnSiSeguroCliente.addActionListener(this);
			dlgSeguroCliente.add(btnSiSeguroCliente);
			btnNoSeguroCliente.addActionListener(this);
			dlgSeguroCliente.add(btnNoSeguroCliente);
			dlgSeguroCliente.setVisible(true);
		}
		else if(evento.getSource().equals(btnNoSeguroCliente))
		{
			dlgSeguroCliente.setVisible(false);
		}
		else if(evento.getSource().equals(btnSiSeguroCliente))
		{
			// Conectar
			bd = new BaseDatos();
			connection = bd.conectar();
			// Hacer un DELETE FROM clientes WHERE idCliente = X
			String[] elegido = choClientes.getSelectedItem().split("-");
			sentencia = "DELETE FROM clientes WHERE idCliente = "+elegido[0];
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				lblConfirmacionBajaCliente.setText("Baja de Cliente Correcta");
			}
			catch (SQLException sqle)
			{
				lblConfirmacionBajaCliente.setText("Error en Baja");
			}
			finally
			{
				dlgConfirmacionBajaCliente.setLayout(new FlowLayout());
				dlgConfirmacionBajaCliente.addWindowListener(this);
				dlgConfirmacionBajaCliente.setSize(150,100);
				dlgConfirmacionBajaCliente.setResizable(false);
				dlgConfirmacionBajaCliente.setLocationRelativeTo(null);
				dlgConfirmacionBajaCliente.add(lblConfirmacionBajaCliente);
				dlgConfirmacionBajaCliente.setVisible(true);
			}
		}
		else if(evento.getSource().equals(mniAltaProyecto))
		{
			new AltaProyecto();
		}
		else if(evento.getSource().equals(mniModificacionCliente))
		{
			new ModificarCliente();
		}
	}
}
