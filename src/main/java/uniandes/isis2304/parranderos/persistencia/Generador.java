package persistencia;
import java.util.Date;

import com.github.javafaker.Faker;

public class Generador {

	private PersistenciaAforoCC pp;

	Faker faker = new Faker();

	public void generarTodo() {
		generarHorario();
		generarCC();
		generarLector();
		generarVisitantes();
		generarCarnet();
		generarEspacio();
		generarLocal();
		generarLectorCC();
		generarLectorEspacio();
		generarVisita();
	}
	
	
	public void generarCC() 
	{
		pp.adicionarCC("Uniandes", 10000, 1, 0, "verde");
	}
	public void generarVisitantes () 
	{
		for (int i = 0; i < 100000; i++) 
		{
			String name = faker.name().fullName(); 
			String correo = faker.internet().emailAddress();
			String numTelefono = faker.number().digits(10);
			String name2 = faker.name().fullName(); 
			String numTelefono2 = faker.phoneNumber().cellPhone();
			Double temperatura = faker.number().randomDouble(1, 35, 40);
			Double alAzar = faker.number().randomDouble(0, 1, 4);
			String tipo = "";
			if(alAzar==1) {tipo="Empleado";}
			if(alAzar > 2) {tipo="Cliente";}
			if(alAzar==2) {tipo="Domiciliario";}
			String estado = "";
			if(alAzar==1) {tipo="Positivo";}
			if(alAzar==2) {tipo="Rojo";}
			if(alAzar==3) {tipo="Naranja";}
			if(alAzar==4) {tipo="Verde";}
			pp.adicionarVisitante(name, tipo, Integer.parseInt(numTelefono), correo, name2, Integer.parseInt(numTelefono2), estado, temperatura);
		}	
	}

	public void generarEspacio() 
	{
		for(int i= 0;i<1000;i++) 
		{
			String nom = faker.company().name();
			Double area = faker.number().randomDouble(1, 20, 100);
			Double alAzar = faker.number().randomDouble(0, 1, 5);
			String t = "";
			if(alAzar==1) {t="Desocupado";}
			if(alAzar==2) {t="Verde";}
			if(alAzar==3) {t="Deshabilitado";}
			if(alAzar==4) {t="Rojo";}
			if(alAzar==5) {t="Naranja";}	
			pp.adicionarEspacio(1, nom, area, t, t);
		}
	}

	public void generarLocal() 
	{
		for(int i= 1;i<1001;i++) 
		{
			String nom = faker.company().name();
			Double area = faker.number().randomDouble(1, 20, 100);
			Double alAzar = faker.number().randomDouble(0, 1, 5);
			String t = "";
			int aforo =0;
			if(alAzar==1) {t="Locales"; aforo = (int) (pp.darEspacioPorId(i).getArea()/15);}
			if(alAzar==2) {t="Ascensor"; aforo = 2;}
			if(alAzar==3) {t="Bano"; aforo = 4;}
			if(alAzar==4) {t="Parqueadero"; aforo = 100;}
			if(alAzar==5) {t="Zona Circulacion"; aforo = 0;}

			int iHorario = faker.number().numberBetween(1, 30);
			pp.adicionarEstablecimiento(i, iHorario, nom, t, aforo);
		}

	}

	@SuppressWarnings("deprecation")
	public void generarHorario() {
		
		Date cci =new Date();
		cci.setHours(10);
		
		Date ccf=cci;
		ccf.setHours(22);
		
		pp.adicionarHorario(cci.toString(), ccf.toString());
		
		for(int i = 1; i<30;i++) {
			Date dateI = new Date();

			int random3= (int)faker.number().randomDouble(0, 6, 12);
			int horas = (int)faker.number().randomDouble(0, 8, 12);

			dateI.setHours(random3);
			Date dateF = dateI;

			if(dateI.getHours()+horas<24) 
			{
				dateF.setHours(dateI.getHours()+horas);
			}
			else 
			{
				dateF.setHours(24);
			}

			pp.adicionarHorario(dateI.toString(), dateF.toString());
		}
	}

	public void generarCarnet() {
		for (int i = 0; i < 75000; i++) 
		{
			pp.adicionarCarnet(i);
		}	
	}

	public void generarLector() 
	{
		for(int i=0; i<1007;i++) 
		{
			String nombre = faker.code().imei();
			pp.adicionarLector(nombre);
		}
	}

	public void generarLectorCC() 
	{
		for(int i=1;i<7;i++) 
		{
			pp.adicionarLectorCC(i, 1);
		}
	}
	public void generarLectorEspacio() 
	{

		for(int i = 0;i<1000;i++) {
			pp.adicionarLectorEspacio(i+7, i);
		}
	}

	public void generarVisita() 
	{
		for(int i = 0; i<820955;i++) 
		{
			int id_lector = faker.number().numberBetween(1, 1007);
			int id_visitante = faker.number().numberBetween(1,100000);
			
			Date dateI = new Date();
			int mes = (int)faker.number().numberBetween(1, 12);
			int dia = (int)faker.number().numberBetween(1, 28);
			int random3= (int)faker.number().randomDouble(0, 6, 12);
			int horas = (int)faker.number().randomDouble(0, 8, 12);
			
			dateI.setMonth(mes);
			dateI.setDate(dia);
			dateI.setHours(random3);
			
			Date dateF = dateI;

			if(dateI.getHours()+horas<24) 
			{
				dateF.setHours(dateI.getHours()+horas);
			}
			else 
			{
				dateF.setHours(24);
			}

			pp.adicionarVisita(id_visitante, id_lector, dateI.toString(), dateF.toString());
		}

	}
}
