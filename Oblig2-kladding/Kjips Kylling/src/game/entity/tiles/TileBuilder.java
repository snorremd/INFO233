package game.entity.tiles;

import game.gfx.SpriteLoader;

import static game.util.EffectiveJavaHasher.*;

/**
 * Siden StaticTile tar så mange argumenter, er det forferdelig å bruke konstruktøren.
 * Derfor lager vi et builder-objekt som bygger seg steg for steg.
 * Du kan da gi argumentene i den rekkefølgen du vil, og så til slutt kalle {@link TileBuilder#create()}
 * 
 * @author Haakon Løtveit (haakon.lotveit@student.uib.no)
 *
 */
public class TileBuilder {
	int row, col, spriteX, spriteY;
	SpriteLoader spriteloader;
	boolean walkable, pushable, lethal;

	/*
	 * Alle disse metodene er laget automatisk av GNU Emacs.
	 * Nei, jeg har ikke giddet å lage disse for hånd. ;)
	 * Og jeg antar at dere ikke trenger javadocs for dem.
	 */

	public TileBuilder spriteloader(SpriteLoader spriteloader){
		this.spriteloader = spriteloader;
		return this;
	}

	public TileBuilder walkable(boolean walkable){
		this.walkable = walkable;
		return this;
	}

	public TileBuilder lethal(boolean lethal){
		this.lethal = lethal;
		return this;
	}

	public TileBuilder row(int row){
		this.row = row;
		return this;
	}

	public TileBuilder col(int col){
		this.col = col;
		return this;
	}

	public TileBuilder spriteX(int spriteX){
		this.spriteX = spriteX;
		return this;
	}

	public TileBuilder spriteY(int spriteY){
		this.spriteY = spriteY;
		return this;
	}

	public StaticTile create() {
		return new StaticTile(row, col, spriteloader, spriteX, spriteY, walkable, lethal);
	}

	@Override
	public boolean equals(Object obj){
		if(null == obj) return false;
		if(obj instanceof TileBuilder){
			TileBuilder tb = (TileBuilder) obj;
			return  tb.lethal 	== this.lethal 	  &&
					tb.pushable == this.pushable  &&
					tb.col 		== this.col 	  &&
					tb.row 		== this.row 	  &&
					tb.spriteX 	== this.spriteX   &&
					tb.spriteY 	== this.spriteY   &&
					tb.spriteloader.equals(this.spriteloader);
		}
		return false;
	}

	@Override
	public int hashCode(){
		int hash = 17;
		/* Takket være EffectiveJavaHasher er dette enkelt og greit */
		hash = hash * 31 + hashBoolean(lethal);
		hash = hash * 31 + hashBoolean(pushable);
		hash = hash * 31 + hashInteger(col);
		hash = hash * 31 + hashInteger(row);
		hash = hash * 31 + hashInteger(spriteX);
		hash = hash * 31 + hashInteger(spriteY);
		hash = hash * 31 + spriteloader.hashCode();
		return hash;
	}
}
