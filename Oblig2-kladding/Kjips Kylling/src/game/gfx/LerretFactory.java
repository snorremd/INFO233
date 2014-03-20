package game.gfx;

import game.entity.types.Level;
import game.entity.types.Player;
import game.input.SimpleKeyboard;

/**
 * Dette er et objekt for å lett lage objekter av lerret-klassen og er fra en tidligere utgave av spillet
 * der klassen var betraktelig mer komplisert enn den er nå.
 * 
 * Slik den er nå er den en fancy konstruktørting som sannsynligvis ingen skjønner vitsen med.
 * 
 * Den har noen kommentarer i seg om hvordan cloneable, toString, hashCode og equals må være,
 * men ellers er det ikke noe interessant her.
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class LerretFactory extends java.lang.Object implements java.lang.Cloneable{
	protected Level level;
	protected Player player;
	protected SimpleKeyboard keyboard;
	
	/**
	 * En enkel konstruktør.
	 */
	public LerretFactory(){
		level = null;
		player = null;
		keyboard = null;
	}
	
	/**
	 * Lar deg sette Playerobjektet som skal styre brettet.
	 * @param player et spillerobjekt som skal styre en spiller rundt på et brett.
	 * @return denne fabrikken, slik at du kan kjede sammen kall.
	 */
	public LerretFactory player(Player player){
		this.player = player;
		return this;
	}
	
	/**
	 * Lar deg setttet Levelobjektet som skal være banen på spillet.
	 * @param level brettet som skal lastes inn.
	 * @return denne fabrikken, slik at du kan kjede sammen kall.
	 */
	public LerretFactory level(Level level){
		this.level = level;
		return this;
	}
	
	/**
	 * Lar deg sette tastaturet som tar seg av trykkingen og slikt.
	 * @param keyboard tastaturlytteren som skal brukes.
	 * @return denne fabrikken, slik at du kan kjede sammen kall.
	 */
	public LerretFactory keyboard(SimpleKeyboard keyboard){
		this.keyboard = keyboard;
		return this;
	}
	
	/**
	 * Det er her det store skjer.
	 * Basert på hvilke verdier du har kalt, blir et nytt Lerret objekt returnert her.
	 * Dersom du kaller create() to ganger for du to lerret med de samme argumentene. Du får *ikke* det samme objektet to ganger, men to nye.
	 *  
	 * @return et nytt Lerret-objekt. Dersom du IKKE har satt en Player, et Level, eller tileSize, verticalTiles eller horizontalTiles til lovlige verdier (dvs tall over 0), returneres null
	 */
	public Lerret create(){
		if(null == player ||
		   null == level ||
		   null == keyboard){
			return null;
		}
		return new Lerret(level,
				          player,
				          keyboard);
	}
	
	
	@Override
	public int hashCode(){
		/* denne metoden er tatt rett ut av Effective Java, side 47. */
		
		int result = 17;
		result = 31 * result + level.hashCode();
		
		return result;
	}
	
	/*
	 * Jeg har sett en del bruk av getClass for å sammenligne.
	 * Det er feil ting å gjøre. Bruk instanceof istedenfor, siden den takler subklassing og slikt
	 * Det er en vane som sparer deg for en plutselige bugs du aldri så komme.
	 * 
	 * Hvis du *vet* at getClass er bedre en instanceof for deg i et gitt tilfelle, bruk getClass.
	 * Ellers, bruk instanceof.
	 * (Se Liskov Substitution Principle hvis du vil vite mer)
	 * 
	 * I tillegg, hashCode-metoden din må hashe på alle de samme feltene som equals bruker, og bare de samme. 
	 */
	@Override
	public boolean equals(Object obj){
		if(obj instanceof LerretFactory){
			LerretFactory lf = (LerretFactory) obj;
			return lf.level.equals(this.level);
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return String.format("LerretFactory<horizontalTiles: %d, verticalTiles %s, tilesize %d>",
				null == level? 0 : level.tileColumns(),
				null == level? 0 : level.tileRows());
	}
	
	/*
	 * Cloning per cloneable er sært.
	 * Veldig sært. Cthulu var på utplassering hos Sun fra NAV når Cloneable ble implementert.
	 * Derfor, hvis alt virker, vær glad.
	 * Hvis ikke, vurder å lag en copy() metode eller lignende istedenfor.
	 * 
	 * Flere java-eksperter har gått ut å sagt at clone/cloneable er knekt hinsides alt håp.
	 * 
	 * En tommelfingerregel er at dersom du vurderer å bruke clone, lag en konstruktør istedenfor, av typen:
	 * public Foo(Foo toCopy)
	 * 
	 * Du kan jo sammenligne selv.
	 */
	@Override
	public LerretFactory clone(){
		try{			
			return (LerretFactory) super.clone();
		}
		catch(CloneNotSupportedException wtf){
			// Dette skal ikke kunne gå an å skje, da java.lang.Object SKAL implementere clone.
			// Men siden det kan være en feil i JVMen eller kompilatoren, er det greit å sjekke.			
			throw new AssertionError("java.lang.Object reports that it doesn't support cloning");
		}
	}
	/* Vanligvis har en konstruktørene sammen, dette er et unntak. */
	public LerretFactory(LerretFactory toClone){
		player = toClone.player;
		level = toClone.level;
		keyboard = toClone.keyboard;
	}
	
	/* 
	 * Merk at vi her bare kopierer rett over, men det vil IKKE være en god ide i det generelle tilfellet.
	 * Sett at vi har en klasse ArrayFoo som har et array i seg slik:
	 * class ArrayFoo{
	 *   private int[] magicNumbers;
	 *   
	 *   // Og så konstruktører, metoder, osv.
	 * }
	 * 
	 * La oss så late som om vi kopierte slik:
	 * 
	 * public ArrayFoo(ArrayFoo toCopy){
	 * 	 this.magicNumbers = toCopy.magicNumbers;
	 * }
	 * 
	 * Dersom en kopierer på den måten, og en verdi magicNumbers i en av Fooene endrer seg, vil
	 * *ALLE* som har blitt kopiert på den måten endre seg.
	 * Dette er en Dum Ting™. Du må derfor passe på å kopiere objekter defensivt.
	 * String og ints kan ikke endre seg, og kan derfor kopieres rett over.
	 * Arrays og objekter og slikt kan IKKE uten videre kopieres rett over. 
	 * (Gjett om clone gjør dette for deg automatisk?)
	 * 
	 * Se http://www.artima.com/intv/bloch13.html for mer om dette.
	 */	
	
}
