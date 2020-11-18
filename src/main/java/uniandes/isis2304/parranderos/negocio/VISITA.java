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

import java.sql.Timestamp;


public class VISITA implements VOVISITA
{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * La fecha y hora de la visita
	 */
	private Timestamp FECHAYHORA_OP;
	
	/**
	 * El tipo de operacion de la visita
	 */
	private String TIPO_OP;
	
	/**
	 * La fecha y hora del fin de la visita
	 */
	private Timestamp HORAFIN_OP;
	
	/**
	 * El id del lector que efectuo la operacion
	 */
	private long IDLECTOR;
	/**
	 * El id del carnet que efectuo la operacion
	 */
	private long IDCARNET;
	/**
	 * El id del espacio en el que se efectuo la operacion
	 */
	private long IDESPACIO;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor por defecto
	 */
	public VISITA() 
	{
		this.FECHAYHORA_OP= new Timestamp(0);
		this.TIPO_OP = "";
		this.HORAFIN_OP =new Timestamp(0);
		this.IDLECTOR = 0;
		this.IDCARNET= 0;
		this.IDESPACIO = 0;
	}

	public VISITA(Timestamp fechaYHoraOp, String tipoOp, Timestamp horarioFinOp, long idLector,
			long idCarnet, long idEspacio) 
	{
		this.FECHAYHORA_OP= fechaYHoraOp;
		this.TIPO_OP = tipoOp;
		this.HORAFIN_OP =horarioFinOp;
		this.IDLECTOR = idLector;
		this.IDCARNET= idCarnet;
		this.IDESPACIO = idEspacio;
	}

	/**
	 * @return the fECHAYHORA_OP
	 */
	public Timestamp getFECHAYHORA_OP() {
		return FECHAYHORA_OP;
	}

	/**
	 * @param fECHAYHORA_OP the fECHAYHORA_OP to set
	 */
	public void setFECHAYHORA_OP(Timestamp fECHAYHORA_OP) {
		FECHAYHORA_OP = fECHAYHORA_OP;
	}

	/**
	 * @return the tIPO_OP
	 */
	public String getTIPO_OP() {
		return TIPO_OP;
	}

	/**
	 * @param tIPO_OP the tIPO_OP to set
	 */
	public void setTIPO_OP(String tIPO_OP) {
		TIPO_OP = tIPO_OP;
	}

	/**
	 * @return the hORAFIN_OP
	 */
	public Timestamp getHORAFIN_OP() {
		return HORAFIN_OP;
	}

	/**
	 * @param hORAFIN_OP the hORAFIN_OP to set
	 */
	public void setHORAFIN_OP(Timestamp hORAFIN_OP) {
		HORAFIN_OP = hORAFIN_OP;
	}

	/**
	 * @return the iDLECTOR
	 */
	public long getIDLECTOR() {
		return IDLECTOR;
	}

	/**
	 * @param iDLECTOR the iDLECTOR to set
	 */
	public void setIDLECTOR(long iDLECTOR) {
		IDLECTOR = iDLECTOR;
	}

	/**
	 * @return the iDCARNET
	 */
	public long getIDCARNET() {
		return IDCARNET;
	}

	/**
	 * @param iDCARNET the iDCARNET to set
	 */
	public void setIDCARNET(long iDCARNET) {
		IDCARNET = iDCARNET;
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
	@Override
	public String toString(){
		return "Visita [fechaYHoraOperacion= "+FECHAYHORA_OP+", tipoOperacion= "+TIPO_OP+", horaFinOperacion= "+
	HORAFIN_OP+", idLector= "+IDLECTOR+", idCarnet= "+IDCARNET+", idEspacio= "+IDESPACIO+"]";
	}
	
}
