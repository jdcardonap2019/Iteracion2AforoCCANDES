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

import java.sql.Timestamp;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.ESPACIO;

class SQLESPACIO 
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
	public SQLESPACIO (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	public long adicionarEspacio(PersistenceManager pm, long idEspacio, Timestamp horarioAperturaEmpleados, Timestamp horarioAperturaClientes,
			Timestamp horarioCierreClientes, int aforoActual, int aforoTotal) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaESPACIO() + "(ID_ESPACIO, HORARIO_APERTURA_EMPLEADOS, "
        		+ "HORARIO_APERTURA_CLIENTES, HORARIO_CIERRE_CLIENTES, AFORO_ACTUAL, AFORO_TOTAL) values (?, ?, ?, ?, ?, ?)");
        q.setParameters(idEspacio, horarioAperturaEmpleados, horarioAperturaClientes, horarioCierreClientes,
        		aforoActual, aforoTotal);
        return (long)q.executeUnique();            
	}
	
	public long eliminarEspacioPorId(PersistenceManager pm, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaESPACIO () + " WHERE ID_ESPACIO = ?");
        q.setParameters(idEspacio);
        return (long) q.executeUnique();            
	}

	public List<ESPACIO> darEspacios(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaESPACIO());
		q.setResultClass(ESPACIO.class);
		return (List<ESPACIO>) q.execute();
	}
	public long aumentarAforoEnElEspacio(PersistenceManager pm, long idEspacio)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCENTRO_COMERCIAL()+ " SET AFORO = AFORO + 1 WHERE ID_ESPACIO=?");
        q.setParameters(idEspacio);
        return (long) q.executeUnique();
	}
	public List<ESPACIO> darEspaciosPorAforo(PersistenceManager pm, int aforoTotal)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaESPACIO()+" WHERE AFORO_TOTAL=?");
		q.setParameters(aforoTotal);
		q.setResultClass(ESPACIO.class);
		return (List<ESPACIO>) q.execute();
	}
}
