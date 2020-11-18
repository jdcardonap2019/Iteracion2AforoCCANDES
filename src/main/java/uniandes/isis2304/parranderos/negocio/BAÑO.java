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

public class BAÑO implements VOBAÑO
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del espacio BAÑO
	 */
	private long IDESPACIO;
	
	/**
	 * El identificador ÚNICO del BAÑO
	 */
	private long ID_BAÑO;

	/**
	 * El número de sanitarios en el baño
	 */
	private int NUMERO_SANITARIOS;
	

	/* ****************************************************************
	 * 			Métodos 
	 *****************************************************************/
    /**
     * Constructor por defecto
     */
	public BAÑO() 
    {
    	this.ID_BAÑO= 0;
    	this.IDESPACIO= 0;
		this.NUMERO_SANITARIOS = 0;
	}

    public BAÑO(long idEspacio, long idBaño, int numeroSanitarios) 
    {
    	this.ID_BAÑO = idBaño;
		this.IDESPACIO = idEspacio;
		this.NUMERO_SANITARIOS = numeroSanitarios;
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
	 * @return the iD_BAÑO
	 */
	public long getID_BAÑO() {
		return ID_BAÑO;
	}

	/**
	 * @param iD_BAÑO the iD_BAÑO to set
	 */
	public void setID_BAÑO(long iD_BAÑO) {
		ID_BAÑO = iD_BAÑO;
	}

	/**
	 * @return the nUMERO_SANITARIOS
	 */
	public int getNUMERO_SANITARIOS() {
		return NUMERO_SANITARIOS;
	}

	/**
	 * @param nUMERO_SANITARIOS the nUMERO_SANITARIOS to set
	 */
	public void setNUMERO_SANITARIOS(int nUMERO_SANITARIOS) {
		NUMERO_SANITARIOS = nUMERO_SANITARIOS;
	}
	@Override
	public String toString(){
		return "Baño [idEspacio= "+IDESPACIO+", idBaño= "+ID_BAÑO+", numeroSanitarios= "+NUMERO_SANITARIOS+"]";
	}

}
