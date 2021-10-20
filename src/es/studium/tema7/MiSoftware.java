package es.studium.tema7;

//Todos los import
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

//La clase la llamamos MiSoftware y se le implementa dos acciones:
//el WindowListener y el ActionListener
public class MiSoftware implements WindowListener, ActionListener
{
	// Ventana Principal
	Frame ventana = new Frame("Mi Software");

	//CONSTRUCTOR DE LA TABLA CLIENTE:
	//----------------------------------------------------------------
	// Ventana Alta de Cliente
	Frame frmAltaCliente = new Frame("Alta de Cliente");

	//Elementos de la ventana de Alta del Cliente
	Label lblNombreCliente = new Label("Nombre:");
	TextField txtNombreCliente = new TextField(20);
	Label lblCifCliente = new Label("Cif:");
	TextField txtCifCliente = new TextField(20);

	//Botones de la ventana de Alta del Cliente
	Button btnAltaCliente = new Button("Alta");
	Button btnCancelarAltaCliente = new Button("Cancelar");

	//Para el mensaje de Alta del cliente:
	//Di�logo con su etiqueta de aviso
	Dialog dlgConfirmarAltaCliente = new Dialog(frmAltaCliente, "Alta Cliente", true);
	Label lblMensajeAltaCliente = new Label("Alta de Cliente Correcta");

	// Ventana Consulta de Clientes
	Frame frmConsultaClientes = new Frame("Consulta Clientes");

	//Elementos para la Consulta de Clientes:
	//Un �rea de Texto y un bot�n PDF
	TextArea listadoClientes = new TextArea(4, 30);
	Button btnPdfClientes = new Button("PDF");

	// Ventana de Borrado de Cliente
	Frame frmBajaCliente = new Frame("Baja de Cliente");

	//Elementos para la Baja del Cliente:
	//Etiqueta que se ve en la ventana con ese texto
	Label lblMensajeBajaCliente = new Label("Seleccionar el cliente:");
	
	//Un CHOICE para seleccionar el cliente a dar de baja
	Choice choClientes = new Choice();
	
	//El Bot�n BORRAR
	Button btnBorrarCliente = new Button("Borrar");
	
	//El mensaje cuando se ha borrado el cliente (DI�LOGO )
	//Diciendo a la ventana que se asocia, El mensaje que muestra y si es o no MODAL
	Dialog dlgSeguroCliente = new Dialog(frmBajaCliente, "�Seguro?", true);
	
	//Mensaje que aparecer� para saber si seguro o no
	Label lblSeguroCliente = new Label("�Est� seguro de borrar?");
	
	//Bot�n de SI estoy seguro
	Button btnSiSeguroCliente = new Button("S�");
	
	//Bot�n de NO estoy seguro
	Button btnNoSeguroCliente = new Button("No");
	
	//Mensaje de confirmaci�n (otro DIALOGO)
	//Que se asocia a la ventana de baja de cliente, el mensaje que tendr� y que es Modal (true)
	Dialog dlgConfirmacionBajaCliente = new Dialog(frmBajaCliente, "Baja Cliente", true);
	
	//Etiqueta (label) diciendo que la baja es correcta
	Label lblConfirmacionBajaCliente = new Label("Baja de cliente correcta");

	//El men� que tendremos en la ventana principal
	//Para que aparezcan en el men� los elementos (tablas) de nuestra BD
	MenuBar mnBar = new MenuBar();

	//Men� de la tabla Clientes con sus ITEMS (Alta, Baja, Modificaci�n, Consulta)
	Menu mnuClientes = new Menu("Clientes");
	MenuItem mniAltaCliente = new MenuItem("Alta");
	MenuItem mniBajaCliente = new MenuItem("Baja");
	MenuItem mniModificacionCliente = new MenuItem("Modificaci�n");
	MenuItem mniConsultaCliente = new MenuItem("Consulta");

	//Men� de la tabla Empleados con sus ITEMS (Alta, Baja, Modificaci�n, Consulta)
	Menu mnuEmpleados = new Menu("Empleados");
	MenuItem mniAltaEmpleado = new MenuItem("Alta");
	MenuItem mniBajaEmpleado = new MenuItem("Baja");
	MenuItem mniModificacionEmpleado = new MenuItem("Modificaci�n");
	MenuItem mniConsultaEmpleado = new MenuItem("Consulta");

	//Men� de la tabla Proyectos con sus ITEMS (Alta, Baja, Modificaci�n, Consulta)
	Menu mnuProyectos = new Menu("Proyectos");
	MenuItem mniAltaProyecto = new MenuItem("Alta");
	MenuItem mniBajaProyecto = new MenuItem("Baja");
	MenuItem mniModificacionProyecto = new MenuItem("Modificaci�n");
	MenuItem mniConsultaProyecto= new MenuItem("Consulta");

	//Men� de la tabla Asignaciones con sus ITEMS (Alta, Baja, Modificaci�n, Consulta)
	Menu mnuAsignaciones = new Menu("Asignaciones");
	MenuItem mniAltaAsignacion = new MenuItem("Alta");
	MenuItem mniBajaAsignacion = new MenuItem("Baja");
	MenuItem mniModificacionAsignacion = new MenuItem("Modificaci�n");
	MenuItem mniConsultaAsignacion = new MenuItem("Consulta");

	//---------------------------------------------------------------------------
	//PAR�METROS CONEXI�N A LA BD:
	//---------------------------------------------------------------------------
	//El driver para poder conectarse a MySQL Server
	String driver = "com.mysql.cj.jdbc.Driver";

	//La direcci�n de la base de datos
	String url = "jdbc:mysql://localhost:3306/misoftware?serverTimezone=UTC";

	//El usuario con el que conectamos (podemos emplear otro usuario que tengamos)
	String login = "root";

	//La contrase�a de conexi�n (Puede variar de un usuario a otro)
	String password = "Studium2020;";

	//Declaraci�n de una variable tipo Cadena en la que 
	//pondremos las instrucciones o sentencias para MySQL Server
	String sentencia = "";

	//------------------------------------------------------------------------------
	//Elementos necesarios para realizar la conexi�n: Connection, Statement, ResulSet
	//-------------------------------------------------------------------------------
	//El m�todo que se ha creado m�s abajo para la conexi�n (Mirarlo abajo) iniciamos en nulo
	Connection connection = null;

	//El Statement que lo creamos con ese nombre(lo que est� azul) y lo ponemos nulo
	Statement statement = null;

	//El ResultSet lo creamos con ese nombre y lo iniciamos en nulo
	//con esto sacaremos la informaci�n de la BD
	ResultSet rs = null;

	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	//Comienza la construcci�n de la Clase 
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public MiSoftware()
	{
		//El Layout (c�mo se organizar�n los elementos) de la ventana principal
		ventana.setLayout(new FlowLayout());

		//------------------------------------------------------------------------
		//A�ADIENDO LOS ITEMS y LUEGO LOS MEN�S:
		//------------------------------------------------------------------------

		//...................................................................
		//PARA EL CLIENTE: Siempre se a�ade primero el Item y despues el Men�
		//...................................................................
		//A�adimos el Listener primero al Item Alta de Cliente antes de a�adirlo
		mniAltaCliente.addActionListener(this);
		//A�adimos el Item Alta del Cliente
		mnuClientes.add(mniAltaCliente);

		//Lo mismo para Baja del Cliente
		mniBajaCliente.addActionListener(this);
		mnuClientes.add(mniBajaCliente);

		//Igual para Modificaci�n del Cliente
		mnuClientes.add(mniModificacionCliente);
		mniConsultaCliente.addActionListener(this);

		//Igual para Consulta del cliente
		mniConsultaCliente.addActionListener(this);
		mnuClientes.add(mniConsultaCliente);

		//A�adimos el Menu de Clientes
		mnBar.add(mnuClientes);

		//......................................................................
		//PARA EL EMPLEADO: Siempre se a�ade primero el Item y despues el Men�
		//.......................................................................
		//A�adimos el Listener primero al Item Alta de Empleado antes de a�adirlo
		mniAltaEmpleado.addActionListener(this);
		mnuEmpleados.add(mniAltaEmpleado);

		//Igual para la Baja del Empleado
		mniBajaEmpleado.addActionListener(this);
		mnuEmpleados.add(mniBajaEmpleado);

		//Igual para Modificaci�n del Empleado
		mniModificacionEmpleado.addActionListener(this);
		mnuEmpleados.add(mniModificacionEmpleado);

		//Igual para Consulta del empleado
		mniConsultaEmpleado.addActionListener(this);
		mnuEmpleados.add(mniConsultaEmpleado);

		//A�adimos el Menu de Empleados
		mnBar.add(mnuEmpleados);

		//......................................................................
		//PARA LA TABLA PROYECTOS: Siempre se a�ade primero el Item y despues el Men�
		//.......................................................................
		//A�adimos el Listener primero al Item Alta de Proyecto 
		//antes de a�adirlo al men� de Proyectos
		mniAltaProyecto.addActionListener(this);
		mnuProyectos.add(mniAltaProyecto);

		//Lo mismo con el Item de Baja de Proyecto
		mniBajaProyecto.addActionListener(this);
		mnuProyectos.add(mniBajaProyecto);

		//Lo mismo con el Item de Modificaci�n de Proyecto
		mniModificacionProyecto.addActionListener(this);
		mnuProyectos.add(mniModificacionProyecto);

		//Lo mismo con el Item de Consulta de Proyecto
		mniConsultaProyecto.addActionListener(this);
		mnuProyectos.add(mniConsultaProyecto);

		//A�adimos el Menu de Proyectos
		mnBar.add(mnuProyectos);

		//......................................................................
		//PARA LA TABLA ASIGNACIONES: Siempre se a�ade primero el Item y despues el Men�
		//.......................................................................
		//A�adimos el Listener primero al Item Alta de Asignaciones
		//antes de a�adirlo al men� de Asignaciones
		mniAltaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniAltaAsignacion);

		//Lo mismo con el Item de Baja de Asignaciones
		mniBajaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniBajaAsignacion);

		//Lo mismo con el Item de Modificaci�n de Asignaciones
		mniModificacionAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniModificacionAsignacion);

		//Lo mismo con el Item de Consulta de Asignaciones
		mniConsultaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniConsultaAsignacion);

		//A�adimos el Menu de Asignaciones
		mnBar.add(mnuAsignaciones);

		//..........................................................
		//A�adimos el Men� que tendremos en la ventana principal
		//Mostrando los elementos (Tablas) de la BD
		//..........................................................
		ventana.setMenuBar(mnBar);

		//=====================================================
		//Las caracter�sticas de la ventana principal:
		//=====================================================
		//Tama�o
		ventana.setSize(320,160);
		//Que no se pueda modificar
		ventana.setResizable(false);
		//Que se muestre en el centro de la pantalla
		ventana.setLocationRelativeTo(null);
		//A�adiendo el Listener para poder cerrar en el aspa
		ventana.addWindowListener(this);
		//Haci�ndola visible
		ventana.setVisible(true);
	}

	//***************************************************
	//M�TODO MAIN CON EL QUE SE CREA LA CLASE CONSTRUIDA
	//***************************************************
	public static void main(String[] args)
	{
		new MiSoftware();
	}

	//===========================================================
	//El m�todo para poder cerrar las ventanas con en Window Event 
	//===========================================================
	//Llamamos al evento we
	public void windowClosing(WindowEvent we)
	{
		//Si est� activa la ventana de Alta de cliente que cierre en el aspa
		if(frmAltaCliente.isActive())
		{
			//Por eso lo de visible false
			frmAltaCliente.setVisible(false);
		}
		//Si est� activo el di�logo de Alta de cliente que cierre en el aspa
		else if(dlgConfirmarAltaCliente.isActive())
		{
			//Tambi�n decidimos borrar todo lo que tenga la caja de texto
			//de los elemento, para que no se guarden para la pr�xima
			//y se dupliquen los datos mostrados
			txtNombreCliente.setText("");
			txtCifCliente.setText("");

			//Colocamos el FOCO en la caja de texto que queremos
			//en este caso es en la del nombre
			txtNombreCliente.requestFocus();

			//cerramos el di�logo de confirmaci�n de alta de cliente
			dlgConfirmarAltaCliente.setVisible(false);
		}
		//Si est� activa la ventana de Consulta de cliente que cierre en el aspa
		else if(frmConsultaClientes.isActive())
		{
			//Por eso lo de visible false
			frmConsultaClientes.setVisible(false);
		}
		//Si est� activa la ventana de Baja de cliente que cierre en el aspa
		else if(frmBajaCliente.isActive())
		{
			//Por eso lo de visible false
			frmBajaCliente.setVisible(false);
		}
		//Si est� activo el di�logo de Seguro para la Baja de cliente
		//que cierre en el aspa
		else if(dlgSeguroCliente.isActive())
		{
			//Por eso lo de visible false
			dlgSeguroCliente.setVisible(false);
		}
		//Si est� activo el di�logo de Confirmaci�n de Baja de cliente
		//que cierre en el aspa
		else if(dlgConfirmacionBajaCliente.isActive())
		{
			//Por eso lo de visible false para cada elemento 
			//de los que hemos colocado aqu�
			dlgConfirmacionBajaCliente.setVisible(false);
			dlgSeguroCliente.setVisible(false);
			frmBajaCliente.setVisible(false);
		}
		//Si lo que est� activa es la ventana principal que cierre en el aspa
		else
		{
			System.exit(0);
		}
	}

	//Los Window Event que no se van a usar en esta ocasi�n agrupados
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	//______________________________________________________________________
	//LAS ACCIONES QUE SE PUEDEN LLEVAR A CABO EN CADA CASO
	//______________________________________________________________________
	public void actionPerformed(ActionEvent evento)
	{
		//.....................................................
		//En caso que se seleccione Alta del Cliente
		//......................................................
		if(evento.getSource().equals(mniAltaCliente))
		{
			//El Layout para la ventana de Alta del Cliente
			frmAltaCliente.setLayout(new FlowLayout());
			
			//A�adimos la etiquete nombre
			frmAltaCliente.add(lblNombreCliente);
			
			//Antes de a�adir la caja de texto lo vaciamos
			txtNombreCliente.setText("");
			//Despu�s lo a�adimos a la ventana
			frmAltaCliente.add(txtNombreCliente);
			
			//A�adimos la etiquete para el cif
			frmAltaCliente.add(lblCifCliente);
			
			//Antes de a�adir la caja de texto lo vaciamos
			txtCifCliente.setText("");
			//Despu�s lo a�adimos a la ventana
			frmAltaCliente.add(txtCifCliente);
			
			//Ponemos el Listener al bot�n de Alta del Cliente antes de a�adirlo
			btnAltaCliente.addActionListener(this);
			//Luego lo a�adimos a la ventana
			frmAltaCliente.add(btnAltaCliente);
			
			//Ponemos el Listener al bot�n de Cancelar antes de a�adirlo
			btnCancelarAltaCliente.addActionListener(this);
			//Luego lo a�adimos a la ventana
			frmAltaCliente.add(btnCancelarAltaCliente);

			//Caracter�sticas de la ventana de Alta del Cliente:
			//El tama�o
			frmAltaCliente.setSize(260,130);
			//No redimensionable
			frmAltaCliente.setResizable(false);
			//Colocada en el centro de la pantalla
			frmAltaCliente.setLocationRelativeTo(null);
			//A�adimos el Listener para que se pueda cerrar en el aspa
			frmAltaCliente.addWindowListener(this);
			//Colocamos el Foco en la caja de texto que queremos
			//En este caso en la del nombre
			txtNombreCliente.requestFocus();
			//Hacemos visible la ventana de Alta de Cliente
			frmAltaCliente.setVisible(true);
		}
		//...............................................................
		//En caso que se pulse el bot�n ACEPTAR de Alta del Cliente
		//...............................................................
		else if(evento.getSource().equals(btnAltaCliente))
		{
			//Llamamos al m�tod para la conexi�n que se cre� m�s abajo
			connection = conectar();

			//Que intente la acci�n
			try
			{
				//Crear una sentencia para statement
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				//Comparamos diciendo en el caso que no se dejen vac�os 
				//las cajas de textos para insertar los datos
				//La caja de texto para nombre y (&&) la caja para Cif
				//que la longitud del texto que tiene no sea cero (nula o vac�a)
				if(((txtNombreCliente.getText().length())!=0)
						&& ((txtCifCliente.getText().length())!=0))
				{
					//Crear un objeto ResultSet para guardar lo obtenido
					//y ejecutar la sentencia SQL
					//Se usa UPDATE (executeUpdate) para insertar o modificar la BD
					sentencia = "INSERT INTO clientes VALUES (null, '"
							+ txtNombreCliente.getText() //Coge lo que hay en esa caja de texto
							+ "', '" +txtCifCliente.getText() + "')";  //Coge lo que hay en esa caja de texto
					//Ejecutamos la sentencia despu�s de tomar los datos insertados
					statement.executeUpdate(sentencia);
					//Muestra el mensaje por pantalla (En la ventana destinada para ello)
					lblMensajeAltaCliente.setText("Alta de Cliente Correcta");
				}
				//En caso que dejemos alguna caja de texto vac�a nos aparecer� ese mensaje
				else
				{
					lblMensajeAltaCliente.setText("Faltan datos");
				}
			}
			//En caso de error en MySQL salta el mensaje de error
			catch (SQLException sqle)
			{
				lblMensajeAltaCliente.setText("Error en ALTA");
			}
			//Se ejecur� siempre aunque haya errores
			finally
			{
				//Las caracteristicas del DI�LOGO para el Alta del Cliente:
				//--------------------------------------------------------
				//El Layout para el di�logo
				dlgConfirmarAltaCliente.setLayout(new FlowLayout());
				//El Listener para poder cerrarlo en el aspa
				dlgConfirmarAltaCliente.addWindowListener(this);
				//El tama�o del Dialogo
				dlgConfirmarAltaCliente.setSize(150,100);
				//Que no se pueda modificar
				dlgConfirmarAltaCliente.setResizable(false);
				//Coloc�ndolo en el centro de la pantalla
				dlgConfirmarAltaCliente.setLocationRelativeTo(null);
				//A�adiendo la etiqueta con el mensaje al Dialogo (que es un contenedor)
				dlgConfirmarAltaCliente.add(lblMensajeAltaCliente);
				//Haciendo visible el Di�logo
				dlgConfirmarAltaCliente.setVisible(true);
			}
		}

		//.....................................................
		//En caso que se seleccione Consulta del Cliente
		//......................................................
		else if(evento.getSource().equals(mniConsultaCliente))
		{
			//El Layout para la ventana de la Consulta
			frmConsultaClientes.setLayout(new FlowLayout());
			// Conectar usando el m�todo para ello (Abajo del todo declarado)
			connection = conectar();
			//La sentencia en este caso para la BD:
			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM empleados";
			try
			{
				//Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				//Se usa Query para consultas 
				rs = statement.executeQuery(sentencia);

				//Para borrar lo que tenga el listado cada vez que lo iniciamos
				listadoClientes.selectAll();
				listadoClientes.setText("");

				//Agregando a lo que hay esto antes: id, Nombre, Cif (separados por tabulador \t)
				listadoClientes.append("id \tNombre \tCif\n");

				// La informaci�n est� en ResultSet (rs)
				// Recorrer el RS y por cada registro,
				// meter una l�nea en el TextArea
				while(rs.next())
				{
					//A�ade al listado lo que contiene la BD en la tabla
					//de Clientes:
					//Sobre sus atributos: idCliente, nombreCliente, cifCliente
					//Tabul�ndolo con \t y poni�ndo cada bloque de datos 
					//uno debajo de otro con el salto de l�nea \n
					listadoClientes.append(rs.getInt("idCliente")
							+ "\t" + rs.getString("nombreCliente")
							+ "\t" + rs.getString("cifCliente")
							+ "\n");
				}
			}
			//En caso de ERROR en MySQL aparece ese mensaje en la ventana
			catch (SQLException sqle)
			{
				lblMensajeAltaCliente.setText("Error en CONSULTA");
			}
			//Haya o no errores aparecer� lo que se encuentra en "finally"
			finally
			{
				//Que no se pueda editar(borrar o escribir en el listado
				listadoClientes.setEditable(false);
				//A�adimos el listado a la ventana de consulta clientes
				frmConsultaClientes.add(listadoClientes);
				//A�adimos el bot�n PDF a la ventana Consulta Clientes 
				//(sin funcionalidad de momento)
				frmConsultaClientes.add(btnPdfClientes);

				//Caracter�sticas de la ventana de Consulta de Clientes:
				//El tama�o
				frmConsultaClientes.setSize(250,160);
				//No redimensionable
				frmConsultaClientes.setResizable(false);
				//Colocarlo en el centro
				frmConsultaClientes.setLocationRelativeTo(null);
				//A�adiendo el Listener para poder cerra en aspa
				frmConsultaClientes.addWindowListener(this);
				//Hacer visible la ventana de Consulta Clientes
				frmConsultaClientes.setVisible(true);
			}
		}
		//.....................................................
		//En caso que se seleccione Baja del Cliente
		//......................................................
		else if(evento.getSource().equals(mniBajaCliente))
		{
			//El Layout para la ventana Baja Cliente
			frmBajaCliente.setLayout(new FlowLayout());
			//A�adiendo la etiqueta para el mensaje de la Baja Cliente
			frmBajaCliente.add(lblMensajeBajaCliente);

			// Para Rellenar el Choice:
			// Hay que Conectar a la BD llamando al m�todo crfeado m�s abajo
			connection = conectar();
			//La sentencia para seleccionar los datos que tiene la tabla de Clientes:
			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM clientes";

			//Pueba si se puede
			try
			{
				//Crear una sentencia para la conexi�n (siempre la misma)
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				//Como vamos a modificar datos que ya hay en la BD se emplea Query
				rs = statement.executeQuery(sentencia);

				//Vaciamos el Choice antes de rellenarlo para que no se dupliquen los datos
				choClientes.removeAll();

				//Recorremos toda la tabla mientras haya elementos insertados y qu� leer con next
				while(rs.next())
				{
					//Colocamos en el Choice los datos de idCliente, nombreCliente y cifCliente
					//que tenemos en la BD separados por guines de ah� la concatenaci�n
					//con los guines
					choClientes.add(rs.getInt("idCliente")
							+ "-" + rs.getString("nombreCliente")
							+ "-" + rs.getString("cifCliente"));
				}
			}
			//En caso de error en MySQL o en la conexi�n
			catch (SQLException sqle)
			{
				lblMensajeAltaCliente.setText("Error en BAJA");
			}
			//Se ejecutar� aunque haya errores
			finally
			{
				//A�adimos a la ventana de Baja de Clientes el Choice
				frmBajaCliente.add(choClientes);
				//Primero a�adimos el Listener al bot�n Borrar Cliente
				btnBorrarCliente.addActionListener(this);
				//Luego a�adimos el bot�n a la ventana de Baja de Cliente
				frmBajaCliente.add(btnBorrarCliente);

				//Caracter�sticas de la ventana de Baja de Cliente
				//El tama�o
				frmBajaCliente.setSize(250,140);
				//No redimensinable
				frmBajaCliente.setResizable(false);
				//Centrado en la pantalla
				frmBajaCliente.setLocationRelativeTo(null);
				//A�adido el Listener para poder cerrar con el aspa
				frmBajaCliente.addWindowListener(this);
				//Hacemos visible la ventana de Baja Cliente
				frmBajaCliente.setVisible(true);
			}
		}

		//...............................................................
		//En caso que se pulse el bot�n BORRAR de Baja del Cliente
		//...............................................................
		else if(evento.getSource().equals(btnBorrarCliente))
		{
			//El layout del di�logo para asegurar el borrado
			dlgSeguroCliente.setLayout(new FlowLayout());
			//A�adiendo Listener al di�logo para cerrar en el aspa
			dlgSeguroCliente.addWindowListener(this);
			//El tama�o del di�logo
			dlgSeguroCliente.setSize(150,100);
			//No redimensinable
			dlgSeguroCliente.setResizable(false);
			//Centrado en pantalla
			dlgSeguroCliente.setLocationRelativeTo(null);
			//A�adimos la etiqueta que saldr� dentro del di�logo
			dlgSeguroCliente.add(lblSeguroCliente);
			//Primero poner Listener al bot�n SI del di�logo
			btnSiSeguroCliente.addActionListener(this);
			//Despu�s a�adir el bot�n SI al di�logo
			dlgSeguroCliente.add(btnSiSeguroCliente);
			//Primero a�adir Listener al bot�n NO del di�logo
			btnNoSeguroCliente.addActionListener(this);
			//Despu�s a�adir el bot�n NO al di�logo
			dlgSeguroCliente.add(btnNoSeguroCliente);
			//Hacer visible el di�logo
			dlgSeguroCliente.setVisible(true);
		}
		//Dos casos posibles: pulsar SI o pulsar NO:
		//..........................................
		//Si se pulsa NO
		else if(evento.getSource().equals(btnNoSeguroCliente))
		{
			//Cerramos el di�logo de seguro querer borrar
			dlgSeguroCliente.setVisible(false);
		}
		//Si se pulsa SI
		else if(evento.getSource().equals(btnSiSeguroCliente))
		{
			// Conectar llamando al m�todo creado para ello m�s abajo
			connection = conectar();

			// Usar una sentencia: Hacer un DELETE FROM clientes WHERE idCliente = X
			//Quiere decir, eliminar en la tabla Clientes 
			//el dato que corresponda con el id X (el que sea elegido)

			//Para ello, metemos dentro de una tabla de tipo Caden los datos por separado
			//usando la caracter�stica split. Como pusimos guiones 
			//ese es el dato com�n que usamos para realizar la separaci�n
			String[] elegido = choClientes.getSelectedItem().split("-");

			//Colocamos la Sentencia y decimos que borre 
			//el dato que coincida con el id seleccionado
			//que lo hemos insertado en el lugar 0 de la tabla de tipo Cadena creada con el split
			sentencia = "DELETE FROM clientes WHERE idCliente = " + elegido[0];

			//Hecho esto, que pruebe a hacer
			try
			{
				//Crear una sentencia para la conexi�n (siempre igual)
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				//Ejecutamos UPDATE porque vamos a borrar 
				statement.executeUpdate(sentencia);
				//La etiqueta mostrando el mensaje de correcto
				lblConfirmacionBajaCliente.setText("Baja de Cliente Correcta");
			}
			//Caso de error en MySQL
			catch (SQLException sqle)
			{
				//Mensaje de error en la baja
				lblConfirmacionBajaCliente.setText("Error en Baja");
			}
			//Se cumplir� siempre
			finally
			{
				////El Layout para el di�logo de Confirmaci�n de baja de Cliente 
				dlgConfirmacionBajaCliente.setLayout(new FlowLayout());
				//El listener para poder cerrarlo en el aspa
				dlgConfirmacionBajaCliente.addWindowListener(this);
				//El tama�o del di�logo
				dlgConfirmacionBajaCliente.setSize(150,100);
				//no redimensinable
				dlgConfirmacionBajaCliente.setResizable(false);
				//Colocarlo en el centro de la pantalla
				dlgConfirmacionBajaCliente.setLocationRelativeTo(null);
				//A�adiendo la etiqueta donde aparecer� el mensaje
				dlgConfirmacionBajaCliente.add(lblConfirmacionBajaCliente);
				//Haciendo visible el di�logo
				dlgConfirmacionBajaCliente.setVisible(true);
			}
		}
	}

	//================================================
	//El m�todo para la conexi�n que se usar� siempre
	//=================================================
	public Connection conectar()  //lo llamamos conectar
	{
		//Que pruebe a hacer
		try
		{
			//Cargar los controladores para el acceso a la BD
			//Diciendo la Clase con el nombre que pusimos al archivo .jar
			//el que tenemos que asociar para que se pueda conectar a la BD de MySQL Server
			Class.forName(driver); //Lo llamamos driver

			//Establecer la conexi�n con la BD Empresa
			//Con la direcci�n en la que se encuentra: url
			//El usuario con el que vamos a conectar: login
			//La clave para la conexi�n: password
			connection = DriverManager.getConnection(url, login, password);
		}
		//En caso de error de no localizar algo o 
		//que no funcione alg�n dato de url, login o pass
		//Aparece el error 1 seguido del mensaje que se guarda en "cnfe"
		catch (ClassNotFoundException cnfe) //llamamos a la excepci�n cnfe
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		//En caso de error en MySQL salta el error 2 
		//seguido del mensaje que se guarda en "cnfe"
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		//Lo que devuelve el m�todo es connection 
		//Precisamente es lo que hace cuando llamamos al m�todo
		//Uno de los valores necesarios para la conexi�n era
		//connection que lo iniciamos en null
		return connection;
	}
}