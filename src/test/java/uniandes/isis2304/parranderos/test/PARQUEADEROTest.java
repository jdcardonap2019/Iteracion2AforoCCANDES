package uniandes.isis2304.parranderos.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileReader;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import uniandes.isis2304.parranderos.negocio.AFOROCCANDES;
import uniandes.isis2304.parranderos.negocio.VOPARQUEADERO;
public class PARQUEADEROTest
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PARQUEADEROTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private AFOROCCANDES aforo;
	
    /* ****************************************************************
	 * 			Métodos de prueba para la tabla PARQUEADEROS - Creación y borrado
	
	 *****************************************************************/
    /**
     * 
    @Test
	public void CRDPARQUEADERO() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando las operaciones CRD sobre PARQUEADERO");
			aforo = new AFOROCCANDES (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de CRD de PARQUEADERO incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de CRD de PARQUEADERO incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
    	try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOPARQUEDAERO> lista = aforo.darVOPARQUEADEROS();
			assertEquals ("No debe haber tipos de bebida creados!!", 0, lista.size ());

			// Lectura de los tipos de bebida con un tipo de bebida adicionado
			String idEspacio="23";
			String idParqueadero="1";
			int capacidad=1;
			Long idRealEspacio=Long.getLong(idEspacio);
			Long idRealParqueadero=Long.getLong(idParqueadero);
			VOPARQUEDAERO parqueadero1 = aforo.adicionarParqueadero(idRealEspacio, idRealParqueadero, capacidad);
			lista = aforo.darVOPARQUEADEROS();
			assertEquals ("Debe haber un tipo de bebida creado !!", 1, lista.size ());
			assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", parqueadero1, lista.get (0));

			// Lectura de los tipos de bebida con dos tipos de bebida adicionados
			String idEspacio2="24";
			String idParqueadero2="2";
			int capacidad2=1;
			Long idRealEspacio2=Long.getLong(idEspacio2);
			Long idRealParqueadero2=Long.getLong(idParqueadero2);
			VOPARQUEDAERO parqueadero2 = aforo.adicionarParqueadero(idRealEspacio2, idRealParqueadero2, capacidad2);
			lista = aforo.darVOPARQUEADEROS();
			assertEquals ("Debe haber dos tipos de bebida creados !!", 2, lista.size ());
			assertTrue ("El primer tipo de bebida adicionado debe estar en la tabla", parqueadero1.equals (lista.get (0)) || parqueadero1.equals (lista.get (1)));
			assertTrue ("El segundo tipo de bebida adicionado debe estar en la tabla", parqueadero2.equals (lista.get (0)) || parqueadero2.equals (lista.get (1)));

			// Prueba de eliminación de un tipo de bebida, dado su identificador
			long tbEliminados = aforo.eliminarParqueaderoPorId(parqueadero1.getID_PARQUEADERO());
			assertEquals ("Debe haberse eliminado un tipo de bebida !!", 1, tbEliminados);
			lista = aforo.darVOPARQUEADEROS();
			assertEquals ("Debe haber un solo tipo de bebida !!", 1, lista.size ());
			assertFalse ("El primer tipo de bebida adicionado NO debe estar en la tabla", parqueadero1.equals (lista.get (0)));
			assertTrue ("El segundo tipo de bebida adicionado debe estar en la tabla", parqueadero2.equals (lista.get (0)));
			
			// Prueba de eliminación de un tipo de bebida, dado su identificador
			tbEliminados = aforo.eliminarParqueaderoPorId(parqueadero2.getID_PARQUEADERO());
			assertEquals ("Debe haberse eliminado un tipo de bebida !!", 1, tbEliminados);
			lista = aforo.darVOPARQUEADEROS();
			assertEquals ("La tabla debió quedar vacía !!", 0, lista.size ());
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla TipoBebida.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas sobre la tabla TipoBebida");
		}
		finally
		{
			aforo.limpiarAforo();
    		aforo.cerrarUnidadPersistencia ();    		
		}
	}

    /**
     * Método de prueba de la restricción de unicidad sobre el nombre de TipoBebida
 
	@Test
	public void unicidadPARQUEADEROTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del nombre del tipo de bebida");
			aforo = new AFOROCCANDES (openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Tipobebida incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Tipobebida incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOPARQUEDAERO> lista = aforo.darVOPARQUEADEROS();
			assertEquals ("No debe haber tipos de bebida creados!!", 0, lista.size ());

			// Lectura de los tipos de bebida con un tipo de bebida adicionado
			String idEspacio="23";
			String idParqueadero="1";
			int capacidad=1;
			Long idRealEspacio=Long.getLong(idEspacio);
			Long idRealParqueadero=Long.getLong(idParqueadero);
			VOPARQUEDAERO parqueadero1 = aforo.adicionarParqueadero(idRealEspacio, idRealParqueadero, capacidad);
			lista = aforo.darVOPARQUEADEROS();
			assertEquals ("Debe haber un tipo de bebida creado !!", 1, lista.size ());

			VOPARQUEDAERO parqueadero2 = aforo.adicionarParqueadero(idRealEspacio, idRealParqueadero, capacidad);
			assertNull ("No puede adicionar dos tipos de bebida con el mismo idEspacio o idParqueadero!!", parqueadero2);
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla TipoBebida.\n";
			msg += "Revise el log de parranderos y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla TipoBebida");
		}    				
		finally
		{
			aforo.limpiarAforo();
    		aforo.cerrarUnidadPersistencia ();    		
		}
	}

	/* ****************************************************************
	 * 			Métodos de configuración
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
     * 			NULL si hay un error en el archivo.

    private JsonObject openConfig (String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración de tablas válido");
		} 
		catch (Exception e)
		{
			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "TipoBebidaTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
    */
}
