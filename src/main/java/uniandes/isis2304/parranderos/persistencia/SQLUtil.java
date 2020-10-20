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

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos para el concepto BAR de Parranderos
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLUtil
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
	public SQLUtil (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqAforo() + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarAforo (PersistenceManager pm)
	{
        Query qBanio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaBAÑO());          
        Query qCentro_Comercial = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCENTRO_COMERCIAL ());
        Query qEspacio = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaESPACIO ());
        Query qLector = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLECTOR ());
        Query qParqueadero = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPARQUEADERO ());
        Query qVisita = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITA ());
        Query qVisitante = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE ());
        Query qLocal_comercial = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE ());

        long baniosEliminados = (long) qBanio.executeUnique ();
        long centro_comercialEliminados = (long) qCentro_Comercial.executeUnique ();
        long espaciosEliminadas = (long) qEspacio.executeUnique ();
        long lectoresEliminadas = (long) qLector.executeUnique ();
        long parqueaderosEliminados = (long) qParqueadero.executeUnique ();
        long visitasEliminados = (long) qVisita.executeUnique ();
        long visitantesEliminados = (long) qVisitante.executeUnique ();
        long localesEliminados= (Long)qLocal_comercial.executeUnique();
        return new long[] {baniosEliminados, centro_comercialEliminados,espaciosEliminadas,lectoresEliminadas,parqueaderosEliminados,visitasEliminados,visitantesEliminados,localesEliminados};
	}

}
