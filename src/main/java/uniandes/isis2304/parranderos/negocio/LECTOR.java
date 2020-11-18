package uniandes.isis2304.parranderos.negocio;


public class LECTOR implements VOLECTOR{
	/**
	 * El identificador del lector
	 */
	private long ID_LECTOR;
	/**
	 * El identificador del espacio
	 */
	private long IDESPACIO;
	public LECTOR() 
	{
		this.ID_LECTOR= 0;
		this.IDESPACIO = 0;
	}

	public LECTOR(long idLector, long idEspacio) 
	{		
		this.ID_LECTOR= idLector;
		this.IDESPACIO = idEspacio;
	}
	/**
	 * @return the iD_LECTOR
	 */
	public long getID_LECTOR() {
		return ID_LECTOR;
	}
	/**
	 * @param iD_LECTOR the iD_LECTOR to set
	 */
	public void setID_LECTOR(long iD_LECTOR) {
		ID_LECTOR = iD_LECTOR;
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
		return "Lector [idLector= "+ID_LECTOR+", idEspacio= "+IDESPACIO+"]";
	}

}
