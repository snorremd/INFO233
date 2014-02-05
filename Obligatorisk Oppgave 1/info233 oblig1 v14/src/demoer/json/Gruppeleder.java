package demoer.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class Gruppeleder {
	protected String namn;
	protected Integer powerlevel;

	/**
	 * Lager en gruppeleder fra en streng og en int.
	 * @param namn navnet
	 * @param powerlevel hvor sterk gruppelederen er. {@link http://www.youtube.com/watch?v=SiMHTK15Pik}
	 */
	public Gruppeleder(String namn, Integer powerlevel) {
		this.namn = "Navn ikke funnet";
		this.powerlevel = 0;
	}


	/**
	 * Lager et nytt gruppelederobjekt basert på hva vi kan hente ut av en parser.
	 * @param parser en Jackson-parser som kan parse java.
	 * (jp står ikke for Jean-Phillipe, men for JSON Parser.)
	 * @throws IOException dersom ugyldig json oppdages. 
	 */
	public static Gruppeleder lesFraJson(JsonParser jp) throws IOException{
		/* Når vi får inn en parser må vi først sjekke at den ikke har blitt flyttet på.
		   Hvis den har blitt flyttet på, kaster vi en exception, ellers, så flytter vi den. */
		if(jp.nextToken() != JsonToken.START_OBJECT){
			throw new IOException("ugyldig json-streng oppdaget");
		}
		/* Så oppretter vi variablene vi vil hente ut. */
		String namn = null;
		Integer powerlevel = null;
		
		/* Så blir det litt komplisert (men ikke mye!)
		   Dersom det neste tokenet er END_OBJECT, dvs at vi er ferdige med å parse, så skal vi ut av løkken.
		   Ellers så går vi til neste token, og leser det. */
		while(jp.nextToken() != JsonToken.END_OBJECT){
			jp.nextToken();

			/* Så sjekker vi hva feltet i JSON-objektet heter.
			   Dersom det er et av de feltene vi kjenner igjen og er interessert i,
			   så henter vi ut verdien. Her kjenner vi kun til namn og powerlevel.
			   Vi gjør det på denne måten fordi rekkefølgen ting kommer inn på er ukjent. */
			switch(jp.getCurrentName()){
			case "namn":
				namn = jp.getText();
				break;
			case "powerlevel":
				powerlevel = jp.getIntValue();
				break;
			default:
				System.out.printf("Ukjent felt: %s%n", jp.getCurrentName());
				break;
			}
		}
		
		/* dersom vi ikke fant noe felt, så kaster vi en IOException */
		if(null == namn){
			throw new IOException("Fant ikke feltet \"namn\"");
		}
		if(null == powerlevel){
			throw new IOException("Fant ikke feltet \"powerlevel\"");
		}
		return new Gruppeleder(namn, powerlevel);
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public Integer getPowerlevel() {
		return powerlevel;
	}

	public void setPowerlevel(Integer powerlevel) {
		this.powerlevel = powerlevel;
	}

	@Override
	public String toString(){
		return String.format("%s, Powerlevel: %d", getNamn(), getPowerlevel());
	}
	public static void main(String[] args) throws Exception{ // Merk at vi ikke bryr oss om unntak her. Dersom noe går galt, så går det galt.
		/* Først lager vi to JSON objekter. Her representert som to strenger.
		   Merk dere at rekkefølgen på tingene er forskjellig, men at det er fortsatt det samme objektet.
		   Rekkefølge spiller altså ingen rolle i JSON, og det må tas hensyn til i alle fornuftige løsninger. */
		String jsonHaakon = "{ \"namn\":\"Haakon\", \"powerlevel\":9002}";
		String jsonSnorre = "{ \"powerlevel\":9001, \"namn\":\"Snorre\"}";
		// Merk at derpus har fler felt enn det som vi andre har. Men det går fint, fordi det har vi tatt hensyn til, og vi henter kun ut det vi trenger.
		String jsonDerpus = "{ \"namn\":\"Derpus Maximus\", \"yndlingsbok\":\"Structure and Interpretations of Computer Pictures\", \"powerlevel\":42}";
		
		
		// Så lager vi en fabrikk (factory), som spytter ut parsere.
		JsonFactory jsonFactory = new JsonFactory();

		// Så lager vi en parser for strengen. Vi kan også lage parsere for strømmer og filer.
		JsonParser parser;
		
		// Leser inn Haakon
		parser = jsonFactory.createParser(jsonHaakon);
		Gruppeleder haakon = Gruppeleder.lesFraJson(parser);
		
		// Leser inn Snorre
		parser = jsonFactory.createParser(jsonSnorre);
		Gruppeleder snorre = Gruppeleder.lesFraJson(parser);
		
		// Leser inn Derpus Maximus
		parser = jsonFactory.createParser(jsonDerpus);
		Gruppeleder derpus = Gruppeleder.lesFraJson(parser);
		
		// Til slutt skriver vi dem ut.
		System.out.println(haakon);
		System.out.println(snorre);
		System.out.println(derpus);
	}

}
