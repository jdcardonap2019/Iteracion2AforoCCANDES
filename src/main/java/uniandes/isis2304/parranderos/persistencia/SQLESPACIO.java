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
			Timestamp horarioCierreClientes, int aforoActual, int aforoTotal, String estado) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaESPACIO() + "(ID_ESPACIO, HORARIO_APERTURA_EMPLEADOS, "
        		+ "HORARIO_APERTURA_CLIENTES, HORARIO_CIERRE_CLIENTES, AFORO_ACTUAL, AFORO_TOTAL, ESTADO) values (?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(idEspacio, horarioAperturaEmpleados, horarioAperturaClientes, horarioCierreClientes,
        		aforoActual, aforoTotal,estado);
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
	public List<Object> RFC3AdminEstablecimiento(PersistenceManager pm,long idEspacio)
	{
		String sql = "SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC, IdEspacio, AforoEnEstablecimiento/AforoMaximoXd as AforoEnEspacio";
        sql += " FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo";
        sql += " FROM  "+pp.darTablaESPACIO()+"),";
        sql += " (SELECT ID_ESPACIO as IdEspacio, aforo_actual as AforoEnEstablecimiento, aforo_total as AforoMaximoXd";
        sql += " FROM "+pp.darTablaESPACIO();
       	sql	+= " WHERE ID_ESPACIO=?)";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(idEspacio);
		return q.executeList();
	}
	public List<Object> RFC3AdminCentroPorIdEspacio(PersistenceManager pm,long idEspacio)
	{
		String sql = "SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC, IdEspacio, AforoEnEstablecimiento/AforoMaximoXd as AforoEnEspacio";
        sql += " FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo";
        sql += " FROM  "+pp.darTablaESPACIO()+"),";
        sql += " (SELECT ID_ESPACIO as IdEspacio, aforo_actual as AforoEnEstablecimiento, aforo_total as AforoMaximoXd";
        sql += " FROM "+pp.darTablaESPACIO();
       	sql	+= " WHERE ID_ESPACIO=?)";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(idEspacio);
		return q.executeList();
	}
	public List<Object> RFC3AdminCentroPorTipoEstablecimiento(PersistenceManager pm,String tipo)
	{
		String sql = "SELECT AforoEnCC/AforoMaximo as IndiceAforoEnCC,TipoEspacio,  AforoActualEspacio/AforoTotalEspacio as IndiceTipoEstablecimiento";
        sql += " FROM(SELECT SUM(aforo_actual) as AforoEnCC, SUM(aforo_total) as AforoMaximo";
        sql += " FROM  "+pp.darTablaESPACIO()+"),";
        sql += " (SELECT TipoEspacio, SUM(espacio.aforo_actual) as AforoActualEspacio, SUM(espacio.aforo_total) as AforoTotalEspacio";
        sql += " FROM(SELECT IDESPACIO as IdEspacio, TIPO_ESTABLECIMIENTO as TipoEspacio ";
       	sql	+= " FROM "+pp.darTablaLOCAL_COMERCIAL();
       	sql	+= " WHERE TIPO_ESTABLECIMIENTO=?)";
       	sql	+= " INNER JOIN "+pp.darTablaESPACIO()+" ON espacio.id_espacio=IdEspacio group by TipoEspacio)";
       	Query q = pm.newQuery(SQL, sql);
		q.setParameters(tipo);
		return q.executeList();
	}
	public List<Object> RFC4(PersistenceManager pm)
	{
		String sql = "SELECT ID_ESPACIO, AFORO_ACTUAL, AFORO_TOTAL, ESTADO";
        sql += " FROM  "+pp.darTablaESPACIO();
       	sql	+= " WHERE AFORO_ACTUAL<AFORO_TOTAL";
       	Query q = pm.newQuery(SQL, sql);
		return q.executeList();
	}
}
