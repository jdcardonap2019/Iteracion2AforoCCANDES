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

package uniandes.isis2304.parranderos.negocio;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import com.google.gson.JsonObject;
import uniandes.isis2304.parranderos.persistencia.PersistenciaAforo;

/**
 * Clase principal del negocio
 * Sarisface todos los requerimientos funcionales del negocio
 *
 * @author 
 */
public class AFOROCCANDES 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(AFOROCCANDES.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaAforo pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public AFOROCCANDES ()
	{
		pp = PersistenciaAforo.getInstance ();
	}
	
	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public AFOROCCANDES (JsonObject tableConfig)
	{
		pp = PersistenciaAforo.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los PARQUEADERO
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public PARQUEADERO adicionarParqueadero (long idEspacio, float capacidad)
	{
        log.info ("Adicionando Parqueadero con idEspacio: " + idEspacio);
        PARQUEADERO tipoBebida = pp.adicionarParqueadero(idEspacio, capacidad);		
        log.info ("Adicionando Parqueadero con idEspacio: " + tipoBebida);
        return tipoBebida;
	}
	
	
	public long eliminarParqueaderoPorId (long idParqueadero)
	{
		log.info ("Eliminando Parqueadero por id: " + idParqueadero);
        long resp = pp.eliminarParqueaderoPorId(idParqueadero);		
        log.info ("Eliminando Parqueaderopor id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	
	public List<PARQUEADERO> darParqueaderos ()
	{
		log.info ("Consultando Parqueaderos");
        List<PARQUEADERO> tiposBebida = pp.darParqueaderos ();	
        log.info ("Consultando Parqueaderos: " + tiposBebida.size() + " existentes");
        return tiposBebida;
	}

	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */

	public List<VOPARQUEADERO> darVOPARQUEADEROS()
	{
		log.info ("Generando los VO de Parqueaderos:");        
        List<VOPARQUEADERO> voTipos = new LinkedList<VOPARQUEADERO> ();
        for (PARQUEADERO tb : pp.darParqueaderos ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Parqueaderos: " + voTipos.size() + " existentes");
        return voTipos;
	}	
	/* ****************************************************************
	 * 			Métodos para manejar los BANIO
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente una bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre la bebida
	 * @param idTipoBebida - El identificador del tipo de bebida de la bebida - Debe existir un TIPOBEBIDA con este identificador
	 * @param gradoAlcohol - El grado de alcohol de la bebida (Mayor que 0)
	 * @return El objeto Bebida adicionado. null si ocurre alguna Excepción
	 */
	public BAÑO adicionarBaño (long idEspacio, long idBaño, int numeroSanitarios)
	{
		log.info ("Adicionando Baño " + idBaño);
		BAÑO baño = pp.adicionarBaño (idEspacio, idBaño, numeroSanitarios);
        log.info ("Adicionando Baño: " + idBaño);
        return baño;
	}
	
	public long eliminarBañoPorId (long idBaño)
	{
        log.info ("Eliminando baño por id: " + idBaño);
        long resp = pp.eliminarBañoPorId (idBaño);
        log.info ("Eliminando baño por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todas las bebida en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Bebida con todos las bebidas que conoce la aplicación, llenos con su información básica
	 */
	public List<BAÑO> darBaños ()
	{
        log.info ("Consultando Baños");
        List<BAÑO> baños = pp.darBaños();	
        log.info ("Consultando Bebidas: " + baños.size() + " bebidas existentes");
        return baños;
	}

	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOBebida con todos las bebidas que conoce la aplicación, llenos con su información básica
	 */
	public List<VOBAÑO> darVOBaños ()
	{
		log.info ("Generando los VO de los baños");       
        List<VOBAÑO> voBaños = new LinkedList<VOBAÑO> ();
        for (BAÑO beb : pp.darBaños())
        {
        	voBaños.add (beb);
        }
        log.info ("Generando los VO de las bebidas: " + voBaños.size() + " existentes");
        return voBaños;
	}


	/* ****************************************************************
	 * 			Métodos para manejar los CARNETS
	 *****************************************************************/


	public CARNET adicionarCarnet (Long idCarnet, float cedula)
	{
        log.info ("Adicionando Carnet: " + idCarnet);
        CARNET bebedor = pp.adicionarCarnet (idCarnet, cedula);
        log.info ("Adicionando carnet: " + idCarnet);
        return bebedor;
	}


	public long eliminarCarnetPorId (long idCarnet)
	{
        log.info ("Eliminando Carnet por id: " + idCarnet);
        long resp = pp.eliminarCarnetPorId (idCarnet);
        log.info ("Eliminando Carnet por Id: " + resp + " tuplas eliminadas");
        return resp;
	}


	public CARNET darCarnetPorId (long idCarnet)
	{
        log.info ("Dar información de un bebedor por id: " + idCarnet);
        CARNET bebedor = pp.darCarnetPorId (idCarnet);
        log.info ("Buscando bebedor por Id: " + bebedor != null ? bebedor : "NO EXISTE");
        return bebedor;
	}


	public List<CARNET> darCarnets ()
	{
        log.info ("Listando Carnets");
        List<CARNET> bebedores = pp.darCarnets ();	
        log.info ("Listando Carnets: " + bebedores.size() + " Carnets existentes");
        return bebedores;
	}
	

	public List<VOCARNET> darVOCarnets ()
	{
        log.info ("Generando los VO de Carnets");
         List<VOCARNET> voBebedores = new LinkedList<VOCARNET> ();
        for (CARNET bdor : pp.darCarnets())
        {
        	voBebedores.add (bdor);
        }
        log.info ("Generando los VO de Carnets: " + voBebedores.size() + " Carnets existentes");
       return voBebedores;
	}
	
	
	/**
	 * Dado el nombre de una ciudad, encuentra el número de bebedores de esa ciudad que han realizado por lo menos una visita a un bar
	 * Adiciona entradas al log de la aplicación
	 * @param ciudad - La ciudad de interés

	/* ****************************************************************
	 * 			Métodos para manejar los ESPACIOS
	 *****************************************************************/
	/**
	 * Adiciona de manera persistente un bar 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del bar
	 * @param presupuesto - El presupuesto del bar (ALTO, MEDIO, BAJO)
	 * @param ciudad - La ciudad del bar
	 * @param sedes - El número de sedes que tiene el bar en la ciudad (Mayor que 0)
	 * @return El objeto Bar adicionado. null si ocurre alguna Excepción
	 */
	public ESPACIO adicionarEspacio ( Timestamp horarioAperturaEmpleados, Timestamp horarioAperturaClientes,
			Timestamp horarioCierreClientes, int aforoActual, int aforoTota)
	{
        log.info ("Adicionando Espacio con aforo: " + aforoTota );
        ESPACIO espacio = pp.adicionarEspacio (  horarioAperturaEmpleados,  horarioAperturaClientes ,horarioCierreClientes, aforoActual,  aforoTota);
        log.info ("Adicionando Espacior: " +aforoTota );
        return espacio;
	}
	

	
	public long eliminarEspacioPorId (long idEspacio)
	{
        log.info ("Eliminando Espacio por id: " + idEspacio);
        long resp = pp.eliminarEspacioPorId (idEspacio);
        log.info ("Eliminando Espacio: " + resp);
        return resp;
	}
	
	/**
	 * Encuentra todos los bares en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Bar con todos las bares que conoce la aplicación, llenos con su información básica
	 */
	public List<ESPACIO> darEspacios ()
	{
        log.info ("Listando Espacios");
        List<ESPACIO> espacios = pp.darEspacios ();	
        log.info ("Listando Espacios: " + espacios.size() + " espacios existentes");
        return espacios;
	}


	public List<VOESPACIO> darVOBEspacios ()
	{
		log.info ("Generando los VO de Bares");
		List<VOESPACIO> voEspacios = new LinkedList<VOESPACIO> ();
		for (ESPACIO bar: pp.darEspacios ())
		{
			voEspacios.add (bar);
		}
		log.info ("Generando los VO de Espacios: " + voEspacios.size () + " espacios existentes");
		return voEspacios;
	}


	/* ****************************************************************
	 * 			Métodos para manejar la relación LECTOR
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente una preferencia de una bebida por un bebedor
	 * Adiciona entradas al log de la aplicación
	 * @param idBebedor - El identificador del bebedor
	 * @param idBebida - El identificador de la bebida
	 * @return Un objeto Gustan con los valores dados
	 */
	public LECTOR adicionarLector (long idLector, long idEspacio)
	{
        log.info ("Adicionando Lector [" + idLector + ", " + idEspacio + "]");
        LECTOR resp = pp.adicionarLector (idLector, idEspacio);
        log.info ("Adicionando Lector: " + resp + " tuplas insertadas");
        return resp;
	}
	

	public long eliminarLector (long idLector)
	{
        log.info ("Eliminando Lector");
        long resp = pp.eliminarLectorPorId(idLector);
        log.info ("Eliminando Lector: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los gustan en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos Gustan con todos los GUSTAN que conoce la aplicación, llenos con su información básica
	 */
	public List<LECTOR> darLectores ()
	{
        log.info ("Listando Lectores");
        List<LECTOR> lectores = pp.darLectores ();	
        log.info ("Listando Gustan: " + lectores.size() + " preferencias de gusto existentes");
        return lectores;
	}

	public List<VOLECTOR> darVOLector ()
	{
		log.info ("Generando los VO de Lector");
		List<VOLECTOR> voLector = new LinkedList<VOLECTOR> ();
		for (VOLECTOR bar: pp.darLectores ())
		{
			voLector.add (bar);
		}
		log.info ("Generando los VO de Gustan: " + voLector.size () + " Gustan existentes");
		return voLector;
	}

	/* ****************************************************************
	 * 			Métodos para manejar la relación LOCAL_COMERCIAL
	 *****************************************************************/

	/**
	 * Adiciona de manera persistente el hecho que una bebida es servida por un bar
	 * Adiciona entradas al log de la aplicación
	 * @param idBar - El identificador del bar
	 * @param idBebida - El identificador de la bebida
	 * @param horario - El horario en el que se sirve la bebida (DIURNO, NOCTURNO, TODOS)
	 * @return Un objeto Sirven con los valores dados
	 */
	public LOCAL_COMERCIAL adicionarLocalComercial (long idEspacio,long id_local, String nombre, String nombre_empresa, float area, String tipo_establecimiento)
	{
        log.info ("Adicionando Local Comercial:"+ id_local);
        LOCAL_COMERCIAL resp = pp.adicionarLocalComercial (idEspacio, id_local,  nombre,  nombre_empresa,  area,  tipo_establecimiento);
        log.info ("Adicionando Local Comercial: " + resp + " tuplas insertadas");
        return resp;
	}
	

	public long eliminarLocalComercialPorId (long idLocal)
	{
        log.info ("Eliminando sirven");
        long resp = pp.eliminarLocalComercialPorId (idLocal);
        log.info ("Eliminando sirven: " + resp + "tuplas eliminadas");
        return resp;
	}
	
	public long eliminarLocalComercialPorNombre (String nombre)
	{
		log.info ("Eliminando LocalComercial por nombre: " + nombre);
        long resp = pp.eliminarLocalComercialPorNombre (nombre);		
        log.info ("Eliminando LocalComercial por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los SIRVEN en Parranderos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos SIRVEN con todos los GUSTAN que conoce la aplicación, llenos con su información básica
	 */
	public List<LOCAL_COMERCIAL> darLocalesComerciales ()
	{
        log.info ("Listando Locales Comerciales");
        List<LOCAL_COMERCIAL> sirven = pp.darLocalesComerciales();
        log.info ("Listando Locales Comerciales: " + sirven.size() + " Locales Comerciales existentes");
        return sirven;
	}

	/**
	 * Encuentra todos los sirven en Parranderos y los devuelve como VO
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos SIRVEN con todos los SIRVEN que conoce la aplicación, llenos con su información básica
	 */
	public List<VOLOCAL_COMERCIAL> darVOLOCAL_COMERCIAL ()
	{
		log.info ("Generando los VO de Sirven");
		List<VOLOCAL_COMERCIAL> voLocalComercial = new LinkedList<VOLOCAL_COMERCIAL> ();
		for (VOLOCAL_COMERCIAL sirven: pp.darLocalesComerciales ())
		{
			voLocalComercial.add (sirven);
		}
		log.info ("Generando los VO de local comercial: " + voLocalComercial.size () + " local comerciales existentes");
		return voLocalComercial;
	}

	/* ****************************************************************
	 * 			Métodos para manejar la relación VISITA
	 *****************************************************************/

	
	public VISITA adicionarVisita (Timestamp fechaYHora_op , String tipo_op, Timestamp horafin_op,long IDCARNET, long IDLECTOR,long IDESPACIO)
	{
        log.info ("Adicionando visitan [" + IDCARNET + ", " + IDLECTOR +", "+ IDESPACIO+ "]");
        VISITA resp = pp.adicionarVisita ( fechaYHora_op ,  tipo_op,  horafin_op, IDCARNET,  IDLECTOR, IDESPACIO);
        log.info ("Adicionando visitan: " + resp + " tuplas insertadas");
        return resp;
	}
	
	public long eliminarVisita (long idCarnet, long idLector, long idEspacio)
	{
        log.info ("Eliminando visita");
        long resp = pp.eliminarVisita(idCarnet, idLector, idEspacio);
        log.info ("Eliminando visita: " + resp + " tuplas eliminadas");
        return resp;
	}
	

	public List<VISITA> darVisitas ()
	{
        log.info ("Listando Visitas");
        List<VISITA> visitas = pp.darVisitas ();	
        log.info ("Listando Visitas: Listo!");
        return visitas;
	}


	public List<VOVISITA> darVOVisita ()
	{
		log.info ("Generando los VO de Visitan");
		List<VOVISITA> voVisita = new LinkedList<VOVISITA> ();
		for (VOVISITA vis: pp.darVisitas())
		{
			voVisita.add (vis);
		}
		log.info ("Generando los VO de Visita: " + voVisita.size () + " Visitas existentes");
		return voVisita;
	}
	/* ****************************************************************
	 * 			Métodos para manejar los VISTIANTES
	 *****************************************************************/

	
	public VISITANTE adicionarVisitante (float cedula, String nombre, float telefono,String nombre_contacto,float telefono_contacto, String codigo_qr, String correo, Timestamp horario_disponible, String tipo_visitante, long idEspacio)
	{
        log.info ("Adicionando Visitante: " + cedula);
        VISITANTE visitante = pp.adicionarVisitante(cedula, nombre, telefono, nombre_contacto, telefono_contacto, codigo_qr, correo, horario_disponible, tipo_visitante, idEspacio); 
        log.info ("Adicionando bebedor: " + visitante);
        return visitante;
	}


	public long eliminarVisitantePorNombre (String nombre)
	{
        log.info ("Eliminando visitante por nombre: " + nombre);
        long resp = pp.eliminarVisitantePorNombre (nombre);
        log.info ("Eliminando visitante por nombre: " + resp + " tuplas eliminadas");
        return resp;
	}


	public long eliminarVisitantePorCedula (float cedula)
	{
        log.info ("Eliminando visitante por cedula: " + cedula);
        long resp = pp.eliminarVisitantePorCedula (cedula);
        log.info ("Eliminando visitante por cedula: " + resp + " tuplas eliminadas");
        return resp;
	}


	public VISITANTE darBebedorPorCedula (float cedula)
	{
        log.info ("Dar información de un visitante con cedula: " + cedula);
        VISITANTE visitante = pp.darVisitantePorCedula(cedula);
        log.info ("Buscando visitante con  cedula: " + visitante != null ? visitante : "NO EXISTE");
        return visitante;
	}


	public List<VISITANTE> darVisitantesPorNombre (String nombre)
	{
        log.info ("Dar información de visitantes por nombre: " + nombre);
        List<VISITANTE> bebedores = pp.darVisitantePorNombre (nombre);
        log.info ("Dar información de visitantes por nombre: " + bebedores.size() + " bebedores con ese nombre existentes");
        return bebedores;
 	}

	/**
	 * Encuentra la información básica de los bebedores, según su nombre y los devuelve como VO
	 * @param nombre - El nombre de bebedor a buscar
	 * @return Una lista de Bebedores con su información básica, donde todos tienen el nombre buscado.
	 * 	La lista vacía indica que no existen bebedores con ese nombre
	 */
	public List<VOVISITANTE> darVOVisitantesPorNombre (String nombre)
	{
        log.info ("Generando VO de visitantes por nombre: " + nombre);
        List<VOVISITANTE> voBebedores = new LinkedList<VOVISITANTE> ();
       for (VISITANTE bdor : pp.darVisitantePorNombre(nombre))
       {
          	voBebedores.add (bdor);
       }
       log.info ("Generando los VO de visitantes: " + voBebedores.size() + " visitantes existentes");
      return voBebedores;
 	}


	public List<VISITANTE> darVisitantes ()
	{
        log.info ("Listando Visitantes");
        List<VISITANTE> bebedores = pp.darVisitantes ();	
        log.info ("Listando Visitantes: " + bebedores.size() + " visitantes existentes");
        return bebedores;
	}
	

	public List<VOVISITANTE> darVOBeVisitantes ()
	{
        log.info ("Generando los VO de Visitantes");
         List<VOVISITANTE> voBebedores = new LinkedList<VOVISITANTE> ();
        for (VISITANTE bdor : pp.darVisitantes ())
        {
        	voBebedores.add (bdor);
        }
        log.info ("Generando los VO de Visitantes: " + voBebedores.size() + " visitantes existentes");
       return voBebedores;
	}
	
	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	public long [] limpiarAforo ()
	{
        log.info ("Limpiando la BD de AforoCC");
        long [] borrrados = pp.limpiarAforo();	
        log.info ("Limpiando la BD de AforoCC: Listo!");
        return borrrados;
	}
}
