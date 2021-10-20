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
	//Diálogo con su etiqueta de aviso
	Dialog dlgConfirmarAltaCliente = new Dialog(frmAltaCliente, "Alta Cliente", true);
	Label lblMensajeAltaCliente = new Label("Alta de Cliente Correcta");

	// Ventana Consulta de Clientes
	Frame frmConsultaClientes = new Frame("Consulta Clientes");

	//Elementos para la Consulta de Clientes:
	//Un ärea de Texto y un botón PDF
	TextArea listadoClientes = new TextArea(4, 30);
	Button btnPdfClientes = new Button("PDF");

	// Ventana de Borrado de Cliente
	Frame frmBajaCliente = new Frame("Baja de Cliente");

	//Elementos para la Baja del Cliente:
	//Etiqueta que se ve en la ventana con ese texto
	Label lblMensajeBajaCliente = new Label("Seleccionar el cliente:");
	
	//Un CHOICE para seleccionar el cliente a dar de baja
	Choice choClientes = new Choice();
	
	//El Botón BORRAR
	Button btnBorrarCliente = new Button("Borrar");
	
	//El mensaje cuando se ha borrado el cliente (DIÁLOGO )
	//Diciendo a la ventana que se asocia, El mensaje que muestra y si es o no MODAL
	Dialog dlgSeguroCliente = new Dialog(frmBajaCliente, "¿Seguro?", true);
	
	//Mensaje que aparecerá para saber si seguro o no
	Label lblSeguroCliente = new Label("¿Está seguro de borrar?");
	
	//Botón de SI estoy seguro
	Button btnSiSeguroCliente = new Button("Sí");
	
	//Botón de NO estoy seguro
	Button btnNoSeguroCliente = new Button("No");
	
	//Mensaje de confirmación (otro DIALOGO)
	//Que se asocia a la ventana de baja de cliente, el mensaje que tendrá y que es Modal (true)
	Dialog dlgConfirmacionBajaCliente = new Dialog(frmBajaCliente, "Baja Cliente", true);
	
	//Etiqueta (label) diciendo que la baja es correcta
	Label lblConfirmacionBajaCliente = new Label("Baja de cliente correcta");

	//El menú que tendremos en la ventana principal
	//Para que aparezcan en el menú los elementos (tablas) de nuestra BD
	MenuBar mnBar = new MenuBar();

	//Menú de la tabla Clientes con sus ITEMS (Alta, Baja, Modificación, Consulta)
	Menu mnuClientes = new Menu("Clientes");
	MenuItem mniAltaCliente = new MenuItem("Alta");
	MenuItem mniBajaCliente = new MenuItem("Baja");
	MenuItem mniModificacionCliente = new MenuItem("Modificación");
	MenuItem mniConsultaCliente = new MenuItem("Consulta");

	//Menú de la tabla Empleados con sus ITEMS (Alta, Baja, Modificación, Consulta)
	Menu mnuEmpleados = new Menu("Empleados");
	MenuItem mniAltaEmpleado = new MenuItem("Alta");
	MenuItem mniBajaEmpleado = new MenuItem("Baja");
	MenuItem mniModificacionEmpleado = new MenuItem("Modificación");
	MenuItem mniConsultaEmpleado = new MenuItem("Consulta");

	//Menú de la tabla Proyectos con sus ITEMS (Alta, Baja, Modificación, Consulta)
	Menu mnuProyectos = new Menu("Proyectos");
	MenuItem mniAltaProyecto = new MenuItem("Alta");
	MenuItem mniBajaProyecto = new MenuItem("Baja");
	MenuItem mniModificacionProyecto = new MenuItem("Modificación");
	MenuItem mniConsultaProyecto= new MenuItem("Consulta");

	//Menú de la tabla Asignaciones con sus ITEMS (Alta, Baja, Modificación, Consulta)
	Menu mnuAsignaciones = new Menu("Asignaciones");
	MenuItem mniAltaAsignacion = new MenuItem("Alta");
	MenuItem mniBajaAsignacion = new MenuItem("Baja");
	MenuItem mniModificacionAsignacion = new MenuItem("Modificación");
	MenuItem mniConsultaAsignacion = new MenuItem("Consulta");

	//---------------------------------------------------------------------------
	//PARÁMETROS CONEXIÓN A LA BD:
	//---------------------------------------------------------------------------
	//El driver para poder conectarse a MySQL Server
	String driver = "com.mysql.cj.jdbc.Driver";

	//La dirección de la base de datos
	String url = "jdbc:mysql://localhost:3306/misoftware?serverTimezone=UTC";

	//El usuario con el que conectamos (podemos emplear otro usuario que tengamos)
	String login = "root";

	//La contraseña de conexión (Puede variar de un usuario a otro)
	String password = "Studium2020;";

	//Declaración de una variable tipo Cadena en la que 
	//pondremos las instrucciones o sentencias para MySQL Server
	String sentencia = "";

	//------------------------------------------------------------------------------
	//Elementos necesarios para realizar la conexión: Connection, Statement, ResulSet
	//-------------------------------------------------------------------------------
	//El método que se ha creado más abajo para la conexión (Mirarlo abajo) iniciamos en nulo
	Connection connection = null;

	//El Statement que lo creamos con ese nombre(lo que está azul) y lo ponemos nulo
	Statement statement = null;

	//El ResultSet lo creamos con ese nombre y lo iniciamos en nulo
	//con esto sacaremos la información de la BD
	ResultSet rs = null;

	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	//Comienza la construcción de la Clase 
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public MiSoftware()
	{
		//El Layout (cómo se organizarán los elementos) de la ventana principal
		ventana.setLayout(new FlowLayout());

		//------------------------------------------------------------------------
		//AÑADIENDO LOS ITEMS y LUEGO LOS MENÚS:
		//------------------------------------------------------------------------

		//...................................................................
		//PARA EL CLIENTE: Siempre se añade primero el Item y despues el Menú
		//...................................................................
		//Añadimos el Listener primero al Item Alta de Cliente antes de añadirlo
		mniAltaCliente.addActionListener(this);
		//Añadimos el Item Alta del Cliente
		mnuClientes.add(mniAltaCliente);

		//Lo mismo para Baja del Cliente
		mniBajaCliente.addActionListener(this);
		mnuClientes.add(mniBajaCliente);

		//Igual para Modificación del Cliente
		mnuClientes.add(mniModificacionCliente);
		mniConsultaCliente.addActionListener(this);

		//Igual para Consulta del cliente
		mniConsultaCliente.addActionListener(this);
		mnuClientes.add(mniConsultaCliente);

		//Añadimos el Menu de Clientes
		mnBar.add(mnuClientes);

		//......................................................................
		//PARA EL EMPLEADO: Siempre se añade primero el Item y despues el Menú
		//.......................................................................
		//Añadimos el Listener primero al Item Alta de Empleado antes de añadirlo
		mniAltaEmpleado.addActionListener(this);
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

		//Añadimos el Menu de Empleados
		mnBar.add(mnuEmpleados);

		//......................................................................
		//PARA LA TABLA PROYECTOS: Siempre se añade primero el Item y despues el Menú
		//.......................................................................
		//Añadimos el Listener primero al Item Alta de Proyecto 
		//antes de añadirlo al menú de Proyectos
		mniAltaProyecto.addActionListener(this);
		mnuProyectos.add(mniAltaProyecto);

		//Lo mismo con el Item de Baja de Proyecto
		mniBajaProyecto.addActionListener(this);
		mnuProyectos.add(mniBajaProyecto);

		//Lo mismo con el Item de Modificación de Proyecto
		mniModificacionProyecto.addActionListener(this);
		mnuProyectos.add(mniModificacionProyecto);

		//Lo mismo con el Item de Consulta de Proyecto
		mniConsultaProyecto.addActionListener(this);
		mnuProyectos.add(mniConsultaProyecto);

		//Añadimos el Menu de Proyectos
		mnBar.add(mnuProyectos);

		//......................................................................
		//PARA LA TABLA ASIGNACIONES: Siempre se añade primero el Item y despues el Menú
		//.......................................................................
		//Añadimos el Listener primero al Item Alta de Asignaciones
		//antes de añadirlo al menú de Asignaciones
		mniAltaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniAltaAsignacion);

		//Lo mismo con el Item de Baja de Asignaciones
		mniBajaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniBajaAsignacion);

		//Lo mismo con el Item de Modificación de Asignaciones
		mniModificacionAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniModificacionAsignacion);

		//Lo mismo con el Item de Consulta de Asignaciones
		mniConsultaAsignacion.addActionListener(this);
		mnuAsignaciones.add(mniConsultaAsignacion);

		//Añadimos el Menu de Asignaciones
		mnBar.add(mnuAsignaciones);

		//..........................................................
		//Añadimos el Menú que tendremos en la ventana principal
		//Mostrando los elementos (Tablas) de la BD
		//..........................................................
		ventana.setMenuBar(mnBar);

		//=====================================================
		//Las características de la ventana principal:
		//=====================================================
		//Tamaño
		ventana.setSize(320,160);
		//Que no se pueda modificar
		ventana.setResizable(false);
		//Que se muestre en el centro de la pantalla
		ventana.setLocationRelativeTo(null);
		//Añadiendo el Listener para poder cerrar en el aspa
		ventana.addWindowListener(this);
		//Haciéndola visible
		ventana.setVisible(true);
	}

	//***************************************************
	//MÉTODO MAIN CON EL QUE SE CREA LA CLASE CONSTRUIDA
	//***************************************************
	public static void main(String[] args)
	{
		new MiSoftware();
	}

	//===========================================================
	//El método para poder cerrar las ventanas con en Window Event 
	//===========================================================
	//Llamamos al evento we
	public void windowClosing(WindowEvent we)
	{
		//Si está activa la ventana de Alta de cliente que cierre en el aspa
		if(frmAltaCliente.isActive())
		{
			//Por eso lo de visible false
			frmAltaCliente.setVisible(false);
		}
		//Si está activo el diálogo de Alta de cliente que cierre en el aspa
		else if(dlgConfirmarAltaCliente.isActive())
		{
			//También decidimos borrar todo lo que tenga la caja de texto
			//de los elemento, para que no se guarden para la próxima
			//y se dupliquen los datos mostrados
			txtNombreCliente.setText("");
			txtCifCliente.setText("");

			//Colocamos el FOCO en la caja de texto que queremos
			//en este caso es en la del nombre
			txtNombreCliente.requestFocus();

			//cerramos el diálogo de confirmación de alta de cliente
			dlgConfirmarAltaCliente.setVisible(false);
		}
		//Si está activa la ventana de Consulta de cliente que cierre en el aspa
		else if(frmConsultaClientes.isActive())
		{
			//Por eso lo de visible false
			frmConsultaClientes.setVisible(false);
		}
		//Si está activa la ventana de Baja de cliente que cierre en el aspa
		else if(frmBajaCliente.isActive())
		{
			//Por eso lo de visible false
			frmBajaCliente.setVisible(false);
		}
		//Si está activo el diálogo de Seguro para la Baja de cliente
		//que cierre en el aspa
		else if(dlgSeguroCliente.isActive())
		{
			//Por eso lo de visible false
			dlgSeguroCliente.setVisible(false);
		}
		//Si está activo el diálogo de Confirmación de Baja de cliente
		//que cierre en el aspa
		else if(dlgConfirmacionBajaCliente.isActive())
		{
			//Por eso lo de visible false para cada elemento 
			//de los que hemos colocado aquí
			dlgConfirmacionBajaCliente.setVisible(false);
			dlgSeguroCliente.setVisible(false);
			frmBajaCliente.setVisible(false);
		}
		//Si lo que está activa es la ventana principal que cierre en el aspa
		else
		{
			System.exit(0);
		}
	}

	//Los Window Event que no se van a usar en esta ocasión agrupados
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
			
			//Añadimos la etiquete nombre
			frmAltaCliente.add(lblNombreCliente);
			
			//Antes de añadir la caja de texto lo vaciamos
			txtNombreCliente.setText("");
			//Después lo añadimos a la ventana
			frmAltaCliente.add(txtNombreCliente);
			
			//Añadimos la etiquete para el cif
			frmAltaCliente.add(lblCifCliente);
			
			//Antes de añadir la caja de texto lo vaciamos
			txtCifCliente.setText("");
			//Después lo añadimos a la ventana
			frmAltaCliente.add(txtCifCliente);
			
			//Ponemos el Listener al botón de Alta del Cliente antes de añadirlo
			btnAltaCliente.addActionListener(this);
			//Luego lo añadimos a la ventana
			frmAltaCliente.add(btnAltaCliente);
			
			//Ponemos el Listener al botón de Cancelar antes de añadirlo
			btnCancelarAltaCliente.addActionListener(this);
			//Luego lo añadimos a la ventana
			frmAltaCliente.add(btnCancelarAltaCliente);

			//Características de la ventana de Alta del Cliente:
			//El tamaño
			frmAltaCliente.setSize(260,130);
			//No redimensionable
			frmAltaCliente.setResizable(false);
			//Colocada en el centro de la pantalla
			frmAltaCliente.setLocationRelativeTo(null);
			//Añadimos el Listener para que se pueda cerrar en el aspa
			frmAltaCliente.addWindowListener(this);
			//Colocamos el Foco en la caja de texto que queremos
			//En este caso en la del nombre
			txtNombreCliente.requestFocus();
			//Hacemos visible la ventana de Alta de Cliente
			frmAltaCliente.setVisible(true);
		}
		//...............................................................
		//En caso que se pulse el botón ACEPTAR de Alta del Cliente
		//...............................................................
		else if(evento.getSource().equals(btnAltaCliente))
		{
			//Llamamos al métod para la conexión que se creó más abajo
			connection = conectar();

			//Que intente la acción
			try
			{
				//Crear una sentencia para statement
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				//Comparamos diciendo en el caso que no se dejen vacíos 
				//las cajas de textos para insertar los datos
				//La caja de texto para nombre y (&&) la caja para Cif
				//que la longitud del texto que tiene no sea cero (nula o vacía)
				if(((txtNombreCliente.getText().length())!=0)
						&& ((txtCifCliente.getText().length())!=0))
				{
					//Crear un objeto ResultSet para guardar lo obtenido
					//y ejecutar la sentencia SQL
					//Se usa UPDATE (executeUpdate) para insertar o modificar la BD
					sentencia = "INSERT INTO clientes VALUES (null, '"
							+ txtNombreCliente.getText() //Coge lo que hay en esa caja de texto
							+ "', '" +txtCifCliente.getText() + "')";  //Coge lo que hay en esa caja de texto
					//Ejecutamos la sentencia después de tomar los datos insertados
					statement.executeUpdate(sentencia);
					//Muestra el mensaje por pantalla (En la ventana destinada para ello)
					lblMensajeAltaCliente.setText("Alta de Cliente Correcta");
				}
				//En caso que dejemos alguna caja de texto vacía nos aparecerá ese mensaje
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
			//Se ejecurá siempre aunque haya errores
			finally
			{
				//Las caracteristicas del DIÁLOGO para el Alta del Cliente:
				//--------------------------------------------------------
				//El Layout para el diálogo
				dlgConfirmarAltaCliente.setLayout(new FlowLayout());
				//El Listener para poder cerrarlo en el aspa
				dlgConfirmarAltaCliente.addWindowListener(this);
				//El tamaño del Dialogo
				dlgConfirmarAltaCliente.setSize(150,100);
				//Que no se pueda modificar
				dlgConfirmarAltaCliente.setResizable(false);
				//Colocándolo en el centro de la pantalla
				dlgConfirmarAltaCliente.setLocationRelativeTo(null);
				//Añadiendo la etiqueta con el mensaje al Dialogo (que es un contenedor)
				dlgConfirmarAltaCliente.add(lblMensajeAltaCliente);
				//Haciendo visible el Diálogo
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
			// Conectar usando el método para ello (Abajo del todo declarado)
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

				// La información está en ResultSet (rs)
				// Recorrer el RS y por cada registro,
				// meter una línea en el TextArea
				while(rs.next())
				{
					//Añade al listado lo que contiene la BD en la tabla
					//de Clientes:
					//Sobre sus atributos: idCliente, nombreCliente, cifCliente
					//Tabulándolo con \t y poniéndo cada bloque de datos 
					//uno debajo de otro con el salto de línea \n
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
			//Haya o no errores aparecerá lo que se encuentra en "finally"
			finally
			{
				//Que no se pueda editar(borrar o escribir en el listado
				listadoClientes.setEditable(false);
				//Añadimos el listado a la ventana de consulta clientes
				frmConsultaClientes.add(listadoClientes);
				//Añadimos el botón PDF a la ventana Consulta Clientes 
				//(sin funcionalidad de momento)
				frmConsultaClientes.add(btnPdfClientes);

				//Características de la ventana de Consulta de Clientes:
				//El tamaño
				frmConsultaClientes.setSize(250,160);
				//No redimensionable
				frmConsultaClientes.setResizable(false);
				//Colocarlo en el centro
				frmConsultaClientes.setLocationRelativeTo(null);
				//Añadiendo el Listener para poder cerra en aspa
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
			//Añadiendo la etiqueta para el mensaje de la Baja Cliente
			frmBajaCliente.add(lblMensajeBajaCliente);

			// Para Rellenar el Choice:
			// Hay que Conectar a la BD llamando al método crfeado más abajo
			connection = conectar();
			//La sentencia para seleccionar los datos que tiene la tabla de Clientes:
			// Hacer un SELECT * FROM clientes
			sentencia = "SELECT * FROM clientes";

			//Pueba si se puede
			try
			{
				//Crear una sentencia para la conexión (siempre la misma)
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

				//Crear un objeto ResultSet para guardar lo obtenido
				//y ejecutar la sentencia SQL
				//Como vamos a modificar datos que ya hay en la BD se emplea Query
				rs = statement.executeQuery(sentencia);

				//Vaciamos el Choice antes de rellenarlo para que no se dupliquen los datos
				choClientes.removeAll();

				//Recorremos toda la tabla mientras haya elementos insertados y qué leer con next
				while(rs.next())
				{
					//Colocamos en el Choice los datos de idCliente, nombreCliente y cifCliente
					//que tenemos en la BD separados por guines de ahí la concatenación
					//con los guines
					choClientes.add(rs.getInt("idCliente")
							+ "-" + rs.getString("nombreCliente")
							+ "-" + rs.getString("cifCliente"));
				}
			}
			//En caso de error en MySQL o en la conexión
			catch (SQLException sqle)
			{
				lblMensajeAltaCliente.setText("Error en BAJA");
			}
			//Se ejecutará aunque haya errores
			finally
			{
				//Añadimos a la ventana de Baja de Clientes el Choice
				frmBajaCliente.add(choClientes);
				//Primero añadimos el Listener al botón Borrar Cliente
				btnBorrarCliente.addActionListener(this);
				//Luego añadimos el botón a la ventana de Baja de Cliente
				frmBajaCliente.add(btnBorrarCliente);

				//Características de la ventana de Baja de Cliente
				//El tamaño
				frmBajaCliente.setSize(250,140);
				//No redimensinable
				frmBajaCliente.setResizable(false);
				//Centrado en la pantalla
				frmBajaCliente.setLocationRelativeTo(null);
				//Añadido el Listener para poder cerrar con el aspa
				frmBajaCliente.addWindowListener(this);
				//Hacemos visible la ventana de Baja Cliente
				frmBajaCliente.setVisible(true);
			}
		}

		//...............................................................
		//En caso que se pulse el botón BORRAR de Baja del Cliente
		//...............................................................
		else if(evento.getSource().equals(btnBorrarCliente))
		{
			//El layout del diálogo para asegurar el borrado
			dlgSeguroCliente.setLayout(new FlowLayout());
			//Añadiendo Listener al diálogo para cerrar en el aspa
			dlgSeguroCliente.addWindowListener(this);
			//El tamaño del diálogo
			dlgSeguroCliente.setSize(150,100);
			//No redimensinable
			dlgSeguroCliente.setResizable(false);
			//Centrado en pantalla
			dlgSeguroCliente.setLocationRelativeTo(null);
			//Añadimos la etiqueta que saldrá dentro del diálogo
			dlgSeguroCliente.add(lblSeguroCliente);
			//Primero poner Listener al botón SI del diálogo
			btnSiSeguroCliente.addActionListener(this);
			//Después añadir el botón SI al diálogo
			dlgSeguroCliente.add(btnSiSeguroCliente);
			//Primero añadir Listener al botón NO del diálogo
			btnNoSeguroCliente.addActionListener(this);
			//Después añadir el botón NO al diálogo
			dlgSeguroCliente.add(btnNoSeguroCliente);
			//Hacer visible el diálogo
			dlgSeguroCliente.setVisible(true);
		}
		//Dos casos posibles: pulsar SI o pulsar NO:
		//..........................................
		//Si se pulsa NO
		else if(evento.getSource().equals(btnNoSeguroCliente))
		{
			//Cerramos el diálogo de seguro querer borrar
			dlgSeguroCliente.setVisible(false);
		}
		//Si se pulsa SI
		else if(evento.getSource().equals(btnSiSeguroCliente))
		{
			// Conectar llamando al método creado para ello más abajo
			connection = conectar();

			// Usar una sentencia: Hacer un DELETE FROM clientes WHERE idCliente = X
			//Quiere decir, eliminar en la tabla Clientes 
			//el dato que corresponda con el id X (el que sea elegido)

			//Para ello, metemos dentro de una tabla de tipo Caden los datos por separado
			//usando la característica split. Como pusimos guiones 
			//ese es el dato común que usamos para realizar la separación
			String[] elegido = choClientes.getSelectedItem().split("-");

			//Colocamos la Sentencia y decimos que borre 
			//el dato que coincida con el id seleccionado
			//que lo hemos insertado en el lugar 0 de la tabla de tipo Cadena creada con el split
			sentencia = "DELETE FROM clientes WHERE idCliente = " + elegido[0];

			//Hecho esto, que pruebe a hacer
			try
			{
				//Crear una sentencia para la conexión (siempre igual)
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
			//Se cumplirá siempre
			finally
			{
				////El Layout para el diálogo de Confirmación de baja de Cliente 
				dlgConfirmacionBajaCliente.setLayout(new FlowLayout());
				//El listener para poder cerrarlo en el aspa
				dlgConfirmacionBajaCliente.addWindowListener(this);
				//El tamaño del diálogo
				dlgConfirmacionBajaCliente.setSize(150,100);
				//no redimensinable
				dlgConfirmacionBajaCliente.setResizable(false);
				//Colocarlo en el centro de la pantalla
				dlgConfirmacionBajaCliente.setLocationRelativeTo(null);
				//Añadiendo la etiqueta donde aparecerá el mensaje
				dlgConfirmacionBajaCliente.add(lblConfirmacionBajaCliente);
				//Haciendo visible el diálogo
				dlgConfirmacionBajaCliente.setVisible(true);
			}
		}
	}

	//================================================
	//El método para la conexión que se usará siempre
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

			//Establecer la conexión con la BD Empresa
			//Con la dirección en la que se encuentra: url
			//El usuario con el que vamos a conectar: login
			//La clave para la conexión: password
			connection = DriverManager.getConnection(url, login, password);
		}
		//En caso de error de no localizar algo o 
		//que no funcione algún dato de url, login o pass
		//Aparece el error 1 seguido del mensaje que se guarda en "cnfe"
		catch (ClassNotFoundException cnfe) //llamamos a la excepción cnfe
		{
			System.out.println("Error 1-" + cnfe.getMessage());
		}
		//En caso de error en MySQL salta el error 2 
		//seguido del mensaje que se guarda en "cnfe"
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		//Lo que devuelve el método es connection 
		//Precisamente es lo que hace cuando llamamos al método
		//Uno de los valores necesarios para la conexión era
		//connection que lo iniciamos en null
		return connection;
	}
}