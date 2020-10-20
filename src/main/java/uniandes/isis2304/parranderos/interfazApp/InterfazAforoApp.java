/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.parranderos.interfazApp;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.parranderos.negocio.AFOROCCANDES;
import uniandes.isis2304.parranderos.negocio.VOBAÑO;
import uniandes.isis2304.parranderos.negocio.VOESPACIO;
import uniandes.isis2304.parranderos.negocio.VOPARQUEADERO;
import uniandes.isis2304.parranderos.negocio.VOVISITA;
import uniandes.isis2304.parranderos.negocio.VOVISITANTE;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazAforoApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazAforoApp.class.getName());
	
	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociación a la clase principal del negocio.
     */
    private AFOROCCANDES aforo;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuración de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacción para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Menú de la aplicación
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicación. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazAforoApp( )
    {
        // Carga la configuración de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gráfica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        aforo = new AFOROCCANDES (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * Método para configurar el frame principal de la aplicación
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuración por defecto" );			
			titulo = "Aforo APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * Método para crear el menú de la aplicación con base em el objeto JSON leído
     * Genera una barra de menú y los menús con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los menùs deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creación de la barra de menús
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creación de cada uno de los menús
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creación de cada una de las opciones del menú
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD DE BAÑO
	 *****************************************************************/
    public void adicionarBaño( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Digite el id del espacio, id del baño y el número de sanitarios separado por comas",
    				"Adicionar baño", JOptionPane.QUESTION_MESSAGE);
    		if (id!= null)
    		{
    			String[] datos=id.split(",");
    			String idEspacio=datos[0];
    			String idBaño=datos[1];
    			String numSanitarios=datos[2];
    			long idRealEspacio=Long.valueOf(idEspacio);
    			long idRealBaño=Long.valueOf(idBaño);
    			int numSanitarios2=Integer.parseInt(numSanitarios);
        		VOBAÑO tb = aforo.adicionarBaño(idRealEspacio, idRealBaño, numSanitarios2);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un parqueadero con id: " + idRealBaño);
        		}
        		String resultado = "En adicionarBaño\n\n";
        		resultado += "Baño adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void darBaños( )
    {
    	try 
    	{
			List <VOBAÑO> lista = aforo.darVOBaños();

			String resultado = "En listar Baños";
			resultado +=  "\n" + listarBaños(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void eliminarBañoPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del baño?", "Borrar baño por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = aforo.eliminarBañoPorId(idTipo);

    			String resultado = "En eliminar Baño\n\n";
    			resultado += tbEliminados + " Baños eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
   
    
    /* ****************************************************************
	 * 			CRUD DE PARQUEADERO
	 *****************************************************************/
    public void adicionarParqueadero( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Digite el id del espacio y la capacidad separado por comas",
    				"Adicionar parqueadero", JOptionPane.QUESTION_MESSAGE);
    		if (id!= null)
    		{
    			String[] datos=id.split(",");
    			String idEspacio=datos[0];
    			String capacidad=datos[1];
    			long idRealEspacio=Long.valueOf(idEspacio);
    			int capacidadReal=Integer.parseInt(capacidad);
        		VOPARQUEADERO tb = aforo.adicionarParqueadero(idRealEspacio, capacidadReal);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un parqueadero con id espacio: " + idEspacio);
        		}
        		String resultado = "En adicionarParqueadero\n\n";
        		resultado += "Parqueadero adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void darParqueaderos( )
    {
    	try 
    	{
			List <VOPARQUEADERO> lista = aforo.darVOPARQUEADEROS();

			String resultado = "En listar Parqueaderos";
			resultado +=  "\n" + listarParqueaderos(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void eliminarParqueaderoPorId()
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del parqueadero?", "Borrar parqueadero por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = aforo.eliminarParqueaderoPorId(idTipo);

    			String resultado = "En eliminar Parqueadero\n\n";
    			resultado += tbEliminados + " Parqueaderos eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * 			CRUD DE ESPACIO
	 *****************************************************************/
    public void adicionarEspacio( )
    {
    	try 
    	{
    		String id = JOptionPane.showInputDialog (this, "Digite el horario apertura para empleados, el horario apertura para clientes, horario cierre, aforo total y aforo actual separado por comas",
    				"Adicionar Espacio (Insertar Fechas en formato yyyy-MM-dd HH:mm:ss.SSS)", JOptionPane.QUESTION_MESSAGE);
    		if (id!= null)
    		{
    			String[] datos=id.split(",");
    			String horarioAperturaEmpleados=datos[0];
    			String horarioAperturaClientes=datos[1];
    			String horarioCierreClientes = datos[2];
    			String aforoActual = datos[3];
    			String aforoTotal = datos[4];
    			int aforoTotal1=Integer.parseInt(aforoTotal);
    			int aforoActual1=Integer.parseInt(aforoActual);
    			
    			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    			DateFormat formatter = new SimpleDateFormat(FORMAT);
    			
                Date hCC = formatter.parse(horarioCierreClientes);
                Date hAC= formatter.parse(horarioAperturaClientes);
                Date hAE = formatter.parse(horarioAperturaEmpleados);
    			
                Timestamp  ts1 = new Timestamp(hAE.getTime());
                Timestamp  ts2 = new Timestamp(hAC.getTime());
                Timestamp  ts3 = new Timestamp(hCC.getTime());
                
        		VOESPACIO tb = aforo.adicionarEspacio(ts1,ts2,ts3,aforoActual1,aforoTotal1);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear Espacio con aforo total: " +aforoTotal);
        		}
        		String resultado = "En adicionarParqueadero\n\n";
        		resultado += "Parqueadero adicionado exitosamente: " + tb;
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void darEspacios( )
    {
    	try 
    	{
			List <VOESPACIO> lista = aforo.darVOBEspacios();

			String resultado = "En listar Espacios";
			resultado +=  "\n" + listarEspacios(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operación terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    public void eliminarEspacioPorId()
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del Espacio?", "Borrar parqueadero por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = aforo.eliminarEspacioPorId(idTipo);

    			String resultado = "En eliminar Espacio\n\n";
    			resultado += tbEliminados + " Espacio eliminados\n";
    			resultado += "\n Operación terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    /* ****************************************************************
   	 * 			CRUD DE VISITA
   	 *****************************************************************/
       public void adicionarVisita( )
       {
       	try 
       	{
       		String id = JOptionPane.showInputDialog (this, "Digite la fecha y hora operacion, el tipo de operacion, horario fin operacion, id lector, id espacio e id carnet actual separado por comas",
       				"Adicionar Visitas (Insertar Fechas/Horario en formato yyyy-MM-dd HH:mm:ss.SSS)", JOptionPane.QUESTION_MESSAGE);
       		if (id!= null)
       		{
       			String[] datos=id.split(",");
       			String fechaYHoraOp=datos[0];
       			String tipoOp=datos[1];
       			String horaFin = datos[2];
       			String idLector = datos[3];
       			String idEspacio = datos[4];
       			String idCarnet = datos[5];
       			int idLector1=Integer.parseInt(idLector);
       			int idEspacio1=Integer.parseInt(idEspacio);
       			int idCarnet1 = Integer.parseInt(idCarnet);
       			
       			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
       			DateFormat formatter = new SimpleDateFormat(FORMAT);
       			
                   Date hOp = formatter.parse(fechaYHoraOp);
                   Date hFin= formatter.parse(horaFin);
       			
                   Timestamp  ts1 = new Timestamp(hOp.getTime());
                   Timestamp  ts2 = new Timestamp(hFin.getTime());
                   
           		VOVISITA tb = aforo.adicionarVisita(ts1,tipoOp,ts2,idLector1,idEspacio1,idCarnet1);
           		if (tb == null)
           		{
           			throw new Exception ("No se pudo crear Visita con idLector: " +idLector+",idEspacio: "+idEspacio+"e idCarnet: "+idCarnet);
           		}
           		String resultado = "En adicionarVisita\n\n";
           		resultado += "Parqueadero adicionado exitosamente: " + tb;
       			resultado += "\n Operación terminada";
       			panelDatos.actualizarInterfaz(resultado);
       		}
       		else
       		{
       			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
       		}
   		} 
       	catch (Exception e) 
       	{
//   			e.printStackTrace();
   			String resultado = generarMensajeError(e);
   			panelDatos.actualizarInterfaz(resultado);
   		}
       }
       public void darVisitas( )
       {
       	try 
       	{
   			List <VOVISITA> lista = aforo.darVOVisita();

   			String resultado = "En listar visitas";
   			resultado +=  "\n" + listarVisitas(lista);
   			panelDatos.actualizarInterfaz(resultado);
   			resultado += "\n Operación terminada";
   		} 
       	catch (Exception e) 
       	{
//   			e.printStackTrace();
   			String resultado = generarMensajeError(e);
   			panelDatos.actualizarInterfaz(resultado);
   		}
       }
       public void eliminarVisitaPorId()
       {
       	try 
       	{
       		String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo del Espacio, Lector y Carnet?", "Borrar visita por Id", JOptionPane.QUESTION_MESSAGE);
       		if (idTipoStr != null)
       		{
       			String[] datos=idTipoStr.split(",");
       			String idEspacio1=datos[0];
       			String idLector2=datos[1];
       			String idCarnet3= datos[2];
       			
       			long idEspacio = Long.valueOf(idEspacio1);
       			long idLector = Long.valueOf(idLector2);
       			long idCarnet = Long.valueOf(idCarnet3);
       			
       			long tbEliminados = aforo.eliminarVisita(idCarnet, idLector, idEspacio);

       			String resultado = "En eliminar Visita\n\n";
       			resultado += tbEliminados + " Visita eliminados\n";
       			resultado += "\n Operación terminada";
       			panelDatos.actualizarInterfaz(resultado);
       		}
       		else
       		{
       			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
       		}
   		} 
       	catch (Exception e) 
       	{
//   			e.printStackTrace();
   			String resultado = generarMensajeError(e);
   			panelDatos.actualizarInterfaz(resultado);
   		}
       }
       /* ****************************************************************
      	 * 			CRUD DE VISITANTE
      	 *****************************************************************/
          public void adicionarVisitante( )
          {
          	try 
          	{
          		String id = JOptionPane.showInputDialog (this, "Digite la cedula, nombre,telefono, nombre contacto, telefono contacto, codigoQR, correo, horarioDisponibilidad, tipo de visitante e id espacio separado por comas",
          				"Adicionar Visitante (Insertar Fechas/Horario en formato yyyy-MM-dd HH:mm:ss.SSS) y tipoVisitante acorde a los tipos de la documentacion", JOptionPane.QUESTION_MESSAGE);
          		if (id!= null)
          		{
          			String[] datos=id.split(",");
          			String cedula1=datos[0];
          			String nombre=datos[1];
          			String telefono1 = datos[2];
          			String nombreContacto = datos[3];
          			String telefonoContacto1 = datos[4];
          			String codigoQr = datos[5];
          			String correo=datos[6];
          			String horarioDisponibilidad=datos[7];
          			String tipoVisitante = datos[8];
          			String idEspacio1 = datos[9];

          			long cedula = Long.valueOf(cedula1);
          			long idEspacio= Long.valueOf(idEspacio1);
                         
          			float telefono = Float.valueOf(telefono1);
          			float telefonoContacto = Float.valueOf(telefonoContacto1);
          			
          			final String FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
          			DateFormat formatter = new SimpleDateFormat(FORMAT);
          			
                    Date hOp = formatter.parse(horarioDisponibilidad);
         			
                     Timestamp  ts1 = new Timestamp(hOp.getTime());
                      
              		VOVISITANTE tb = aforo.adicionarVisitante(cedula,nombre,telefono,nombreContacto,telefonoContacto,codigoQr,correo,ts1,tipoVisitante,idEspacio);
              		if (tb == null)
              		{
              			throw new Exception ("No se pudo crear Visitante con cedula:"+cedula);
              		}
              		String resultado = "En adicionarVisitante\n\n";
              		resultado += "Parqueadero adicionado exitosamente: " + tb;
          			resultado += "\n Operación terminada";
          			panelDatos.actualizarInterfaz(resultado);
          		}
          		else
          		{
          			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
          		}
      		} 
          	catch (Exception e) 
          	{
//      			e.printStackTrace();
      			String resultado = generarMensajeError(e);
      			panelDatos.actualizarInterfaz(resultado);
      		}
          }
          public void darVisitantes( )
          {
          	try 
          	{
      			List <VOVISITANTE> lista = aforo.darVOBeVisitantes();

      			String resultado = "En listar Visitante";
      			resultado +=  "\n" + listarVisitantes(lista);
      			panelDatos.actualizarInterfaz(resultado);
      			resultado += "\n Operación terminada";
      		} 
          	catch (Exception e) 
          	{
//      			e.printStackTrace();
      			String resultado = generarMensajeError(e);
      			panelDatos.actualizarInterfaz(resultado);
      		}
          }
          public void eliminarVisitantePorCedula()
          {
          	try 
          	{
          		String idTipoStr = JOptionPane.showInputDialog (this, "Cedula del visitante?", "Borrar visitante por Cedula", JOptionPane.QUESTION_MESSAGE);
          		if (idTipoStr != null)
          		{
          			float id = Float.valueOf(idTipoStr);
          			
          			long tbEliminados = aforo.eliminarVisitantePorCedula(id);

          			String resultado = "En eliminar Visitante\n\n";
          			resultado += tbEliminados + " Visitante eliminados\n";
          			resultado += "\n Operación terminada";
          			panelDatos.actualizarInterfaz(resultado);
          		}
          		else
          		{
          			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
          		}
      		} 
          	catch (Exception e) 
          	{
//      			e.printStackTrace();
      			String resultado = generarMensajeError(e);
      			panelDatos.actualizarInterfaz(resultado);
      		}
          }
          public void eliminarVisitantesPorNombre()
          {
          	try 
          	{
          		String idTipoStr = JOptionPane.showInputDialog (this, "Nombre del visitante a borrar?", "Borrar visitante por nombre", JOptionPane.QUESTION_MESSAGE);
          		if (idTipoStr != null)
          		{      		
          			long tbEliminados = aforo.eliminarVisitantePorNombre(idTipoStr);
          			String resultado = "En eliminar Visitante\n\n";
          			resultado += tbEliminados + " Visitante eliminados\n";
          			resultado += "\n Operación terminada";
          			panelDatos.actualizarInterfaz(resultado);
          		}
          		else
          		{
          			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
          		}
      		} 
          	catch (Exception e) 
          	{
//      			e.printStackTrace();
      			String resultado = generarMensajeError(e);
      			panelDatos.actualizarInterfaz(resultado);
      		}
          }
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecución de la demo y recolección de los resultados
			long eliminados [] = aforo.limpiarAforo();
			
			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Banios eliminados\n";
			resultado += eliminados [1] + " Centro_comercial eliminados\n";
			resultado += eliminados [2] + " Espacio eliminados\n";
			resultado += eliminados [3] + " Lector eliminadas\n";
			resultado += eliminados [4] + " Parqueadero eliminados\n";
			resultado += eliminados [5] + " Visita eliminados\n";
			resultado += eliminados [6] + " Visitantes eliminados\n";
			resultado += eliminados [6] + " Local_comercial eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentación general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentación Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la información acerca del desarrollo de esta apicación
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: AforoCC Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Juan David Cardona y Nicolas Quintero\n";
		resultado += " * 2020 RealGMusic\n";
		resultado += " * \n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una línea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una líea para cada tipo de bebida recibido
     */
    private String listarParqueaderos(List<VOPARQUEADERO> lista) 
    {
    	String resp = "Los parqueaderos existentes son:\n";
    	int i = 1;
        for (VOPARQUEADERO tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    private String listarBaños(List<VOBAÑO> lista) 
    {
    	String resp = "Los baños existentes son:\n";
    	int i = 1;
        for (VOBAÑO tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
   
    private String listarEspacios(List<VOESPACIO> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOESPACIO tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
    }
    
    private String listarVisitas(List<VOVISITA> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOVISITA tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
    }
    private String listarVisitantes(List<VOVISITANTE> lista) 
    {
    	String resp = "Los espacios existentes son:\n";
    	int i = 1;
        for (VOVISITANTE tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
    }
    /**
     * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
     * @param e - La excepción recibida
     * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
    /**
     * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
     * Invoca al método correspondiente según el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazAforoApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este método ejecuta la aplicación, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por línea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazAforoApp interfaz = new InterfazAforoApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
