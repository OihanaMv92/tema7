package es.studium.tema7;

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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Es.Studium.Practica.ClinicaVeterinaria;

public class Veterinario  implements WindowListener, ActionListener
{
	// Ventana Principal
	Frame ventana = new Frame("Veterinario");

	// Ventana Alta de Empleado
	Frame frmAltaEmpleado = new Frame("Alta de Empleado");
	//Elementos de la ventana de Alta del empleado
	Label lblNombreEmpleado = new Label("Nombre:");
	TextField txtNombreEmpleado = new TextField(20);
	Label lblApellidoEmpleado = new Label("Apellido:");
	TextField txtApellidoEmpleado = new TextField(20);
	Label lblDireccionEmpleado = new Label("Direccion:");
	TextField txtDireccionEmpleado = new TextField(20);
	Label lblTelefonoEmpleado = new Label("Telefono:");
	TextField txtTelefonoEmpleado = new TextField(20);
	//Botones de la ventana de Alta
	Button btnAltaEmpleado = new Button("Alta");
	Button btnCancelarAltaEmpleado = new Button("Cancelar");
	//Diálogo con su etiqueta de aviso
	Dialog dlgConfirmarAltaEmpleado = new Dialog(frmAltaEmpleado, "Alta Empleado", true);
	Label lblMensajeAltaEmpleado = new Label("Alta de Empleado Correcta");
	// Ventana Consulta de Clientes
	Frame frmConsultaEmpleado = new Frame("Consulta Empleado");
	//Un area de Texto y un botón PDF
	TextArea listadoEmpleado = new TextArea(4, 30);
	Button btnPdfEmpleado = new Button("PDF");
	//Borrar Empleado
	Frame frmBajaEmpleado = new Frame("Baja de Empleado");
	//Etiqueta que se ve en la ventana con ese texto
	Label lblMensajeBajaEmpleado = new Label("Seleccionar el Empleado:");
	//Un CHOICE para seleccionar el empleado a dar de baja
	Choice choEmpleado = new Choice();
	//El Botón BORRAR
	Button btnBorrarEmpleado = new Button("Borrar");
	//El mensaje cuando se ha borrado el cliente (DIÁLOGO )
	Dialog dlgSeguroEmpleado = new Dialog(frmBajaEmpleado, "¿Seguro?", true);	
	//Mensaje que aparecerá para saber si seguro o no
	Label lblSeguroEmpleado = new Label("¿Está seguro de borrar?");
	Button btnSiSeguroEmpleado = new Button("Sí");
	Button btnNoSeguroEmpleado = new Button("No");
	//Mensaje de confirmación
	//Que se asocia a la ventana de baja.
	Dialog dlgConfirmacionBajaEmpleado = new Dialog(frmBajaEmpleado, "Baja Empleado", true);
	//Etiqueta diciendo que la baja es correcta
	Label lblConfirmacionBajaEmpleado = new Label("Baja correcta");
	//El menú que tendremos en la ventana principal
	// Ventana Modificación Cliente
		Frame frmModificarEmpleado = new Frame("Editar Cliente");
		Label lblEditarEmpleado = new Label("Seleccionar el cliente:");
		Choice choEmpleadoEditar = new Choice();
		Button btnEditaEmpleado = new Button("Editar");
	//Para que aparezcan en el menú los elementos (tablas) de nuestra BD
	MenuBar mnBar = new MenuBar();
	//Empleados
	Menu mnuEmpleados = new Menu("Empleados");
	MenuItem mniAltaEmpleado = new MenuItem("Alta");
	MenuItem mniBajaEmpleado = new MenuItem("Baja");
	MenuItem mniModificacionEmpleado = new MenuItem("Modificación");
	MenuItem mniConsultaEmpleado = new MenuItem("Consulta");
	//Consultas
	Menu mnuConsultas = new Menu("Consultas");
	MenuItem mniAltaConsultas = new MenuItem("Alta");
	MenuItem mniBajaConsultas = new MenuItem("Baja");
	MenuItem mniModificacionConsultas = new MenuItem("Modificación");
	MenuItem mniConsultaConsultas = new MenuItem("Consulta");
	//Animales
	Menu mnuAnimales = new Menu("Animales");
	MenuItem mniAltaAnimal = new MenuItem("Alta");
	MenuItem mniConsultaAnimal = new MenuItem("Consulta");
	//Resultante
	Menu mnuResultante = new Menu("Resultante");
	MenuItem mniAltaResultante = new MenuItem("Alta");
	MenuItem mniConsultaResultante = new MenuItem("Consulta");
	//PARÁMETROS CONEXIÓN A LA BD:
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/veterinario1?serverTimezone=UTC";
	String login = "root";
	String password = "Studium2020;";
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	public Veterinario()
	{
		//El Layout para la ventana de Alta
		frmAltaEmpleado.setLayout(new FlowLayout());
		//Añadimos la etiquete nombre
		frmAltaEmpleado.add(lblNombreEmpleado);
		//Antes de añadir la caja de texto lo vaciamos
		txtNombreEmpleado.setText("");
		//Después lo añadimos a la ventana
		frmAltaEmpleado.add(txtNombreEmpleado);

		//Añadimos la etiquete para el Apellido
		frmAltaEmpleado.add(lblApellidoEmpleado);
		txtApellidoEmpleado.setText("");
		frmAltaEmpleado.add(txtApellidoEmpleado);
		btnAltaEmpleado.addActionListener(this);
		frmAltaEmpleado.add(btnAltaEmpleado);
		//Telefono
		frmAltaEmpleado.add(lblTelefonoEmpleado);
		txtTelefonoEmpleado.setText("");
		frmAltaEmpleado.add(txtTelefonoEmpleado);
		btnAltaEmpleado.addActionListener(this);
		frmAltaEmpleado.add(btnAltaEmpleado);
		//Direccion
		frmAltaEmpleado.add(lblDireccionEmpleado);
		txtDireccionEmpleado.setText("");
		frmAltaEmpleado.add(txtDireccionEmpleado);
		btnAltaEmpleado.addActionListener(this);
		frmAltaEmpleado.add(btnAltaEmpleado);
		

		//ventana principal
		ventana.setLayout(new FlowLayout());
		mniAltaEmpleado.addActionListener(this);
//----------------------------------------------------------------------------------------------------
		//Añadimos el Listener primero al Item Alta ANIMAL           
		mniAltaEmpleado.addActionListener(this);
		mnuAnimales.add(mniAltaAnimal);
		//Igual para Consulta del Animales
		mniConsultaAnimal.addActionListener(this);
		mnuAnimales.add(mniConsultaAnimal);
//--------------------------------------------------------------------------------------------------------		
		//Resultante
		mniAltaResultante.addActionListener(this);
		mnuResultante.add(mniAltaResultante);
		//Igual para Consulta del Animales
		mniConsultaResultante.addActionListener(this);
		mnuResultante.add(mniConsultaResultante);
//--------------------------------------------------------------------------------------------------------------
		
		
		
		//Añadimos el Listener primero al Item Alta de Empleado antes de añadirlo
		mniAltaAnimal.addActionListener(this);
		mnuEmpleados.add(mniAltaEmpleado);

		//Igual para la Baja del Empleado
		mniBajaEmpleado.addActionListener(this);
		mnuEmpleados.add(mniBajaEmpleado);

		//Igual para Modificación del Empleado
		mniModificacionEmpleado.addActionListener(this);
		mnuEmpleados.add(mniModificacionEmpleado);

		//Igual para Consulta del empleado
		mniConsultaEmpleado.addActionListener(this);
		mnuEmpleados.add(mniConsultaEmpleado);

		//Añadimos el Menu de Consultas
		mnBar.add(mnuEmpleados);
		mniAltaConsultas.addActionListener(this);
		mnuConsultas.add(mniAltaConsultas);
		//----------------------------------------------------------- Animal
		mnBar.add(mnuAnimales);
		mniAltaAnimal.addActionListener(this);
		mnuAnimales.add(mniAltaAnimal);
		//----------------------------------------------------------Resultante
		mnBar.add(mnuResultante);
		mniAltaResultante.addActionListener(this);
		mnuResultante.add(mniAltaResultante);
		
		//---------------------------------------------------------------
		//Lo mismo con el Item de Baja
		mniBajaConsultas.addActionListener(this);
		mnuConsultas.add(mniBajaConsultas);
		mniModificacionConsultas.addActionListener(this);
		mnuConsultas.add(mniModificacionConsultas);
		mniConsultaConsultas.addActionListener(this);
		mnuConsultas.add(mniConsultaConsultas);
		mnBar.add(mnuConsultas);
		
		
		//Añadimos el Menú que tendremos en la ventana principal
		ventana.setMenuBar(mnBar);
		//Las características de la ventana principal:
		ventana.setSize(320,160);
		//Que no se pueda modificar
		ventana.setResizable(false);
		//Que se muestre en el centro de la pantalla
		ventana.setLocationRelativeTo(null);
		//Añadiendo el Listener para poder cerrar
		ventana.addWindowListener(this);
		//Haciéndola visible
		ventana.setVisible(true);
	}
	public static void main(String[] args)
	{
		new Veterinario();
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
		if(frmConsultaEmpleado.isActive())
		{
			frmConsultaEmpleado.setVisible(false);
		}
		else if(frmBajaEmpleado.isActive())
		{
			frmBajaEmpleado.setVisible(false);
		}
		else if(dlgSeguroEmpleado.isActive())
		{
			dlgSeguroEmpleado.setVisible(false);
		}
		else if(dlgConfirmacionBajaEmpleado.isActive())
		{
			dlgConfirmacionBajaEmpleado.setVisible(false);
			dlgSeguroEmpleado.setVisible(false);
			frmBajaEmpleado.setVisible(false);
		}
		else if(frmModificarEmpleado.isActive())
		{
			frmModificarEmpleado.setVisible(false);
		}
		else
		{
			System.exit(0);
		}
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

	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(mniAltaEmpleado))
		{
			new Veterinario();
		}
		else if(evento.getSource().equals(mniConsultaEmpleado))
		{
			frmConsultaEmpleado.setLayout(new FlowLayout());

			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM Empleado";
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
				listadoEmpleado.selectAll();
				listadoEmpleado.setText("");
				listadoEmpleado.append("id\tNombre\tCif\n");
				while(rs.next())
				{
					listadoEmpleado.append(rs.getInt("idCliente")
							+"\t"+rs.getString("nombreCliente")
							+"\t"+rs.getString("cifCliente")
							+"\n");
				}
			}
			catch (SQLException sqle)
			{
			}
			finally
			{

			}
			listadoEmpleado.setEditable(false);
			frmConsultaEmpleado.add(listadoEmpleado);
			frmConsultaEmpleado.add(btnPdfEmpleado);

			frmConsultaEmpleado.setSize(250,140);
			frmConsultaEmpleado.setResizable(false);
			frmConsultaEmpleado.setLocationRelativeTo(null);
			frmConsultaEmpleado.addWindowListener(this);
			frmConsultaEmpleado.setVisible(true);
		}
		else if(evento.getSource().equals(mniBajaEmpleado))
		{
			frmBajaEmpleado.setLayout(new FlowLayout());
			frmBajaEmpleado.add(lblMensajeBajaEmpleado);
			// Rellenar el Choice

			sentencia = "SELECT * FROM empleados";
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				rs = statement.executeQuery(sentencia);
				choEmpleado.removeAll();
				while(rs.next())
				{
					choEmpleado.add(rs.getInt("idCliente")
							+"-"+rs.getString("nombreCliente")
							+"-"+rs.getString("cifCliente"));
				}
			}
			catch (SQLException sqle){}
			frmBajaEmpleado.add(choEmpleado);
			btnBorrarEmpleado.addActionListener(this);
			frmBajaEmpleado.add(btnBorrarEmpleado);

			frmBajaEmpleado.setSize(250,140);
			frmBajaEmpleado.setResizable(false);
			frmBajaEmpleado.setLocationRelativeTo(null);
			frmBajaEmpleado.addWindowListener(this);
			frmBajaEmpleado.setVisible(true);
		}
		else if(evento.getSource().equals(btnBorrarEmpleado))
		{
			dlgSeguroEmpleado.setLayout(new FlowLayout());
			dlgSeguroEmpleado.addWindowListener(this);
			dlgSeguroEmpleado.setSize(150,100);
			dlgSeguroEmpleado.setResizable(false);
			dlgSeguroEmpleado.setLocationRelativeTo(null);
			dlgSeguroEmpleado.add(lblSeguroEmpleado);
			btnSiSeguroEmpleado.addActionListener(this);
			dlgSeguroEmpleado.add(btnSiSeguroEmpleado);
			btnNoSeguroEmpleado.addActionListener(this);
			dlgSeguroEmpleado.add(btnNoSeguroEmpleado);
			dlgSeguroEmpleado.setVisible(true);
		}
		else if(evento.getSource().equals(btnNoSeguroEmpleado))
		{
			dlgSeguroEmpleado.setVisible(false);
		}
		else if(evento.getSource().equals(btnSiSeguroEmpleado))
		{

			// Hacer un DELETE FROM 
			String[] elegido = choEmpleado.getSelectedItem().split("-");
			sentencia = "DELETE FROM clientes WHERE idCliente = "+elegido[0];
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				lblConfirmacionBajaEmpleado.setText("Baja de Cliente Correcta");
			}
			catch (SQLException sqle)
			{
				lblConfirmacionBajaEmpleado.setText("Error en Baja");
			}
			finally
			{
				dlgConfirmacionBajaEmpleado.setLayout(new FlowLayout());
				dlgConfirmacionBajaEmpleado.addWindowListener(this);
				dlgConfirmacionBajaEmpleado.setSize(150,100);
				dlgConfirmacionBajaEmpleado.setResizable(false);
				dlgConfirmacionBajaEmpleado.setLocationRelativeTo(null);
				dlgConfirmacionBajaEmpleado.add(lblConfirmacionBajaEmpleado);
				dlgConfirmacionBajaEmpleado.setVisible(true);
			}
		}
		else if(evento.getSource().equals(mniModificacionEmpleado))
		{
			frmModificarEmpleado.setLayout(new FlowLayout());

			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM empleado";
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				rs = statement.executeQuery(sentencia);
				choEmpleadoEditar.removeAll();
				while(rs.next())
				{
					choEmpleadoEditar.add(rs.getInt("idEmpleado")
							+ "-" + rs.getString("nombreEmpleado")
							+ "-" + rs.getString("apellidoEmpleado")
							+ "-" + rs.getString("direccionEmpleado")
							+ "-" + rs.getInt("telefonoEmpleado"));;
				}
			}
			catch (SQLException sqle)
			{
				
			}
			frmModificarEmpleado.add(choEmpleadoEditar);
			//btnEditarEmpleados.addActionListener(this);
			//frmModificarEmpleado.add(btnEditarEmpleado);

			frmModificarEmpleado.setSize(250,140);
			frmModificarEmpleado.setResizable(false);
			frmModificarEmpleado.setLocationRelativeTo(null);
			frmModificarEmpleado.addWindowListener(this);
			//frmModificarCliente.setVisible(true);
		}
		//else if(evento.getSource().equals(btnEditarEmpleado))
		{
			//new EditarCliente(choEmpleadoEditar.getSelectedItem());
		}
		else if(evento.getSource().equals(mniAltaConsultas))
		{
			new AltaConsultas();
		}
	}
}
