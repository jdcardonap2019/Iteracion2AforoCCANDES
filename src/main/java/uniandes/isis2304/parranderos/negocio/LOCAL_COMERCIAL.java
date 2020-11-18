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


public class LOCAL_COMERCIAL implements VOLOCAL_COMERCIAL
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del espacio
	 */
	private long IDESPACIO;
	
	/**
	 * El identificador del local mismo
	 */
	private long ID_LOCAL;
	
	/**
	 * El nombre del local comercial
	 */
	private String NOMBRE;
	/**
	 * El nombre de la empresa o dueño/a del local comercial
	 */
	private String NOMBRE_EMPRESA;
	/**
	 * El area que cupa el local comercial
	 */
	private float AREA;
	/**
	 * El area que cupa el local comercial
	 */
	private String TIPO_ESTABLECIMIENTO;

	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public LOCAL_COMERCIAL () 
	{
		this.IDESPACIO = 0;
		this.ID_LOCAL = 0;
		this.NOMBRE = "";
		this.NOMBRE_EMPRESA = "";
		this.AREA = 0;
		this.TIPO_ESTABLECIMIENTO = "";
	}

	/**
	 * Constructor con valores
	 * @param idBar - El identificador del bar. Debe exixtir un bar con dicho identificador
	 * @param idBebida - El identificador de la bebida. Debe existir una bebida con dicho identificador
	 * @param horario - El horario en el que el bar sirve la bebida (DIURNO, NOCTURNO, TODOS)
	 */
	public LOCAL_COMERCIAL (long idEspacio, long idLocal, String nombre,String nombreEmpresa,float area,String tipoEstablecimiento) 
	{
		this.IDESPACIO = idEspacio;
		this.ID_LOCAL = idLocal;
		this.NOMBRE = nombre;
		this.NOMBRE_EMPRESA = nombreEmpresa;
		this.AREA = area;
		this.TIPO_ESTABLECIMIENTO = tipoEstablecimiento;
	}

	/**
	 * @return the iDESPACIO
	 */
	public long getIDESPACIO() {
		return IDESPACIO;
	}

	/**
	 * @param iDESPACIO the iDESPACIO to set
	 */
	public void setIDESPACIO(long iDESPACIO) {
		IDESPACIO = iDESPACIO;
	}

	/**
	 * @return the iD_LOCAL
	 */
	public long getID_LOCAL() {
		return ID_LOCAL;
	}

	/**
	 * @param iD_LOCAL the iD_LOCAL to set
	 */
	public void setID_LOCAL(long iD_LOCAL) {
		ID_LOCAL = iD_LOCAL;
	}

	/**
	 * @return the nOMBRE
	 */
	public String getNOMBRE() {
		return NOMBRE;
	}

	/**
	 * @param nOMBRE the nOMBRE to set
	 */
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}

	/**
	 * @return the nOMBRE_EMPRESA
	 */
	public String getNOMBRE_EMPRESA() {
		return NOMBRE_EMPRESA;
	}

	/**
	 * @param nOMBRE_EMPRESA the nOMBRE_EMPRESA to set
	 */
	public void setNOMBRE_EMPRESA(String nOMBRE_EMPRESA) {
		NOMBRE_EMPRESA = nOMBRE_EMPRESA;
	}

	/**
	 * @return the aREA
	 */
	public float getAREA() {
		return AREA;
	}

	/**
	 * @param aREA the aREA to set
	 */
	public void setAREA(float aREA) {
		AREA = aREA;
	}

	/**
	 * @return the tIPO_ESTABLECIMIENTO
	 */
	public String getTIPO_ESTABLECIMIENTO() {
		return TIPO_ESTABLECIMIENTO;
	}

	/**
	 * @param tIPO_ESTABLECIMIENTO the tIPO_ESTABLECIMIENTO to set
	 */
	public void setTIPO_ESTABLECIMIENTO(String tIPO_ESTABLECIMIENTO) {
		TIPO_ESTABLECIMIENTO = tIPO_ESTABLECIMIENTO;
	}
	@Override
	public String toString(){
		return "Local comercial [idEspacio= "+IDESPACIO+", idLocal= "+ID_LOCAL+", nombre= "+NOMBRE+
				", nombreEmpresa= "+NOMBRE_EMPRESA+", area= "+AREA+", tipo= "+TIPO_ESTABLECIMIENTO+"]";
	}
	
}
