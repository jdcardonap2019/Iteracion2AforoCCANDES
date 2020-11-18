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

public class CENTRO_COMERCIAL implements VOCENTRO_COMERCIAL
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El nombre del centro comercial
	 */
	private String NOMBRE;
	
	/**
	 * El aforo del centro comercial
	 */
	private int AFORO;
	
	/**
	 * Constructor por defecto
	 */
	public CENTRO_COMERCIAL() 
	{
		this.NOMBRE= "";
		this.AFORO = 0;
	}

	/**
	 * Constructor con valores
	 * @param id - El id de la bebida
	 * @param nombre - El nombre de la bebida
	 * @param tipo - El identificador del tipo de bebida
	 * @param gradoAlcohol - El graddo de alcohol de la bebida (Mayor que 0)
	 */
	public CENTRO_COMERCIAL(String nombre, int aforo) 
	{
		this.NOMBRE = nombre;
		this.AFORO = aforo;
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
	 * @return the aFORO
	 */
	public int getAFORO() {
		return AFORO;
	}

	/**
	 * @param aFORO the aFORO to set
	 */
	public void setAFORO(int aFORO) {
		AFORO = aFORO;
	}
	@Override
	public String toString(){
		return "Nombre= "+NOMBRE+", aforo= "+AFORO;
	}


}
