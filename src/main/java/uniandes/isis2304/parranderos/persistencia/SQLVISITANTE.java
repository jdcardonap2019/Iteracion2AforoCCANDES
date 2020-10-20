package uniandes.isis2304.parranderos.persistencia;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.parranderos.negocio.CARNET;
import uniandes.isis2304.parranderos.negocio.VISITANTE;

public class SQLVISITANTE {
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
	public SQLVISITANTE (PersistenciaAforo pp)
	{
		this.pp = pp;
	}
	

	public long adicionarVisitante (PersistenceManager pm, float cedula, String nombre, float telefono,String nombre_contacto,float telefono_contacto, String codigo_qr, String correo, Timestamp horario_disponible, String tipo_visitante, long idEspacio) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVISITANTE() + "(CEDULA,NOMBRE,TELEFONO,NOMBRE_CONTACTO,TELEFONO_CONTACTO,CODIGO_QR,CORREO,HORARIO_DISPONIBLE,TIPO_VISITANTE,IDESPACIO) values (?, ?, ?, ?,?,?,?,?,?,?)");
        q.setParameters( cedula,  nombre,  telefono, nombre_contacto, telefono_contacto, codigo_qr, correo,  horario_disponible,  tipo_visitante,  idEspacio);
        return (long) q.executeUnique();
	}


	public long eliminarVisitantePorNombre (PersistenceManager pm, String nombre)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE () + " WHERE NOMBRE = ?");
        q.setParameters(nombre);
        return (long) q.executeUnique();            
	}

	
	public long eliminarVisitantePorCedula (PersistenceManager pm, float cedula)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVISITANTE () + " WHERE CEDULA = ?");
        q.setParameters(cedula);
        return (long) q.executeUnique();            
	}


	public VISITANTE darBebedorPorCEDULA (PersistenceManager pm, float cedula) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE () + " WHERE CEDULA = ?");
		q.setResultClass(VISITANTE.class);
		q.setParameters(cedula);
		return (VISITANTE) q.executeUnique();
	}


	public List<VISITANTE> darBebedoresPorNombre (PersistenceManager pm, String nombreBebedor) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE () + " WHERE NOMBRE = ?");
		q.setResultClass(VISITANTE.class);
		q.setParameters(nombreBebedor);
		return (List<VISITANTE>) q.executeList();
	}


	public List<VISITANTE> darVisitantes (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVISITANTE ());
		q.setResultClass(VISITANTE.class);
		return (List<VISITANTE>) q.executeList();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS BEBEDORES Y DE SUS VISITAS REALIZADAS de la 
	 * base de datos de Parranderos
	 * @param pm - El manejador de persistencia
	 * @param idBebedor - El identificador del bebedor
	 * @return Una lista de arreglos de objetos, de tamaño 7. Los elementos del arreglo corresponden a los datos de 
	 * los bares visitados y los datos propios de la visita:
	 * 		(id, nombre, ciudad, presupuesto, cantsedes) de los bares y (fechavisita, horario) de las visitas
	 */
	public List<Object []> darVisitasRealizadas (PersistenceManager pm, long idBebedor)
	{
        String sql = "SELECT bar.id, bar.nombre, bar.ciudad, bar.presupuesto, bar.cantsedes, vis.fechavisita, vis.horario";
        sql += " FROM ";
        sql += pp.darTablaBebedor () + " bdor, ";
        sql += pp.darTablaVisitan () + " vis, ";
        sql += pp.darTablaBar () + " bar ";
       	sql	+= " WHERE ";
       	sql += "bdor.id = ?";
       	sql += " AND bdor.id = vis.idbebedor";
       	sql += " AND vis.idbar = bar.id";
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(idBebedor);
		return q.executeList();
	}




	


}
