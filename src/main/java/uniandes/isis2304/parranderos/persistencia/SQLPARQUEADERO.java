package uniandes.isis2304.parranderos.persistencia;


import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.PARQUEADERO;

public class SQLPARQUEADERO {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaAforo.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaAforo pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLPARQUEADERO (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un BEBEDOR a la base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idBebedor - El identificador del bebedor
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarParqueadero (PersistenceManager pm, long idEspacio,long id_parqueadero, float capacidad) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPARQUEADERO() + "(IDESPACIO,ID_PARQUEADERO, CAPACIDAD) values (?, ?, ?)");
        q.setParameters(idEspacio, id_parqueadero, capacidad);
        return (long) q.executeUnique();
	}


	public long eliminarParqueaderoPorId(PersistenceManager pm, long idParqueadero)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPARQUEADERO() + " WHERE ID_PARQUEADERO = ?");
        q.setParameters(idParqueadero);
        return (long) q.executeUnique();            
	}


	public PARQUEADERO darParqueaderoPorId (PersistenceManager pm, long idParqueadero) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPARQUEADERO () + " WHERE ID_PARQUEADERO = ?");
		q.setResultClass(PARQUEADERO.class);
		q.setParameters(idParqueadero);
		return (PARQUEADERO) q.executeUnique();
	}

	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS BEBEDORES de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos BEBEDOR
	 */
	public List<PARQUEADERO> darParqueaderos (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPARQUEADERO ());
		q.setResultClass(PARQUEADERO.class);
		return (List<PARQUEADERO>) q.executeList();
	}



}
