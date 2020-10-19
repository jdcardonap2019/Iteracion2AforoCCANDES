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

package uniandes.isis2304.parranderos.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.BAÑO;


class SQLBAÑO 
{
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
	public SQLBAÑO (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	public long adicionarBaño (PersistenceManager pm, long idEspacio, long idBaño, int numSanitarios) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaBAÑO() + "(IDESPACIO, ID_BAÑO, NUMERO_SANITARIOS) values (?, ?, ?)");
        q.setParameters(idEspacio, idBaño,numSanitarios);
        return (long) q.executeUnique();
	}

	public long eliminarBañosPorId(PersistenceManager pm, long idBaño)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaBAÑO () + " WHERE ID_BAÑO = ?");
        q.setParameters(idBaño);
        return (long) q.executeUnique();
	}

	public BAÑO darBañoPorId (PersistenceManager pm, long idBaño) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaBAÑO() + " WHERE ID_BAÑO = ?");
		q.setResultClass(BAÑO.class);
		q.setParameters(idBaño);
		return (BAÑO) q.executeUnique();
	}

	public List<BAÑO> darBaños (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaBAÑO());
		q.setResultClass(BAÑO.class);
		return (List<BAÑO>) q.executeList();
	}	
	public long aumentarAforoEnElBaño(PersistenceManager pm, long idBaño)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCENTRO_COMERCIAL()+ " SET AFORO = AFORO + 1 WHERE ID_BAÑO=?");
        q.setParameters(idBaño);
        return (long) q.executeUnique();
	}
}
