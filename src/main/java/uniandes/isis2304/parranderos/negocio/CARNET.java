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


public class CARNET implements VOCARNET
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El identificador ÚNICO del carnet
	 */
	private long ID_CARNET;	
	
	/**
	 * La cédula del carnet
	 */
	private float CEDULA;
	/**
	 * Constructor por defecto
	 */
	public CARNET() 
	{
		this.setID_CARNET(0);
		this.setCEDULA(0);
	}

	public CARNET(long idCarnet, float cedula) 
	{
		this.setID_CARNET(idCarnet);
		this.setCEDULA(cedula);
	}

	/**
	 * @return the iD_CARNET
	 */
	public long getID_CARNET() {
		return ID_CARNET;
	}

	/**
	 * @param iD_CARNET the iD_CARNET to set
	 */
	public void setID_CARNET(long iD_CARNET) {
		ID_CARNET = iD_CARNET;
	}

	/**
	 * @return the cEDULA
	 */
	public float getCEDULA() {
		return CEDULA;
	}

	/**
	 * @param cEDULA the cEDULA to set
	 */
	public void setCEDULA(float cEDULA) {
		CEDULA = cEDULA;
	}
	@Override
	public String toString(){
		return "Carnet [idCarnet= "+ID_CARNET+", cedula= "+CEDULA+"]";
	}
}
