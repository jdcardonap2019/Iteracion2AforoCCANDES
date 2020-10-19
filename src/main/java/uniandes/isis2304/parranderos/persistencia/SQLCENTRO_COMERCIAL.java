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



import javax.jdo.PersistenceManager;
import javax.jdo.Query;

class SQLCENTRO_COMERCIAL 
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
	public SQLCENTRO_COMERCIAL (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	public long aumentarAforoEnElCC(PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCENTRO_COMERCIAL()+ " SET AFORO = AFORO + 1");
        return (long) q.executeUnique();
	}
	public long cambiarNombre(PersistenceManager pm, String nombre) 
	{
		 Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCENTRO_COMERCIAL()+ " SET NOMBRE = "+nombre);
	     return (long) q.executeUnique();
	}

}
