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

public class PARQUEADERO implements VOPARQUEADERO
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador del espacio
	 */
	private long IDESPACIO;

	/**
	 * El id del parqueadero
	 */
	private long ID_PARQUEADERO;
	/**
	 * La capacidad del parqueadero
	 */
	private float CAPACIDAD;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public PARQUEADERO() 
	{
		this.CAPACIDAD = 0;
		this.IDESPACIO= 0;
		this.ID_PARQUEADERO=0;
	}

	/**
	 * Constructor con valores
	 * @param id - El identificador del tipo de bebida
	 * @param nombre - El nombre del tipo de bebida
	 */
	public PARQUEADERO(long idEspacio, long idParqueaadero, float capacidad) 
	{
		this.IDESPACIO = idEspacio;
		this.ID_PARQUEADERO = idParqueaadero;
		this.CAPACIDAD=capacidad;
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
	 * @return the iD_PARQUEADERO
	 */
	public long getID_PARQUEADERO() {
		return ID_PARQUEADERO;
	}

	/**
	 * @param iD_PARQUEADERO the iD_PARQUEADERO to set
	 */
	public void setID_PARQUEADERO(long iD_PARQUEADERO) {
		ID_PARQUEADERO = iD_PARQUEADERO;
	}

	/**
	 * @return the cAPACIDAD
	 */
	public float getCAPACIDAD() {
		return CAPACIDAD;
	}

	/**
	 * @param cAPACIDAD the cAPACIDAD to set
	 */
	public void setCAPACIDAD(float cAPACIDAD) {
		CAPACIDAD = cAPACIDAD;
	}


}
