package game.controller;

import game.entity.GreyLevel;
import game.entity.SimplePlayer;
import game.entity.TileFactory;
import game.entity.TileLevel;
import game.entity.types.Level;
import game.entity.types.Player;
import game.gfx.Lerret;
import game.gfx.LerretFactory;
import game.gfx.SpriteLoader;
import game.input.SimpleKeyboard;
import game.util.Direction;
import game.util.PaintingThread;

import java.io.File;
import java.io.FileReader;

import au.com.bytecode.opencsv.CSVReader;

public class Game {

	protected Lerret lerret;
	protected Level level;
	protected Player player;
	protected SimpleKeyboard keyboard;
	protected PaintingThread painter;
	
	public Game(int horizontalTiles, int verticalTiles, int tilesize, Player player, Level level){
		this.keyboard = new SimpleKeyboard(this);
		this.player = player;
		this.level = level;
		this.lerret = new LerretFactory()
					      .horizontalTiles(horizontalTiles)
					      .verticalTiles(verticalTiles)
					      .tilesize(tilesize)
					      .player(player)
					      .level(level)
					      .title("Kjips Kylling")
					      .create();
		
		// TODO: Putt inn i konstruktøren eller noe?
		lerret.registerKeyboard(keyboard);
		painter = new PaintingThread(lerret);
	}
	
	public void move(Direction where){
		player.move(where, level);
		level.tick();
	}

	public static void main(String[] args) throws Exception {
		SpriteLoader tileSprites = new SpriteLoader(new File ("art/tiles.png"), 64);
		TileFactory tf = new TileFactory(tileSprites);
		TileLevel.load(tf, new File("res/level/simplegrey.level"));
		SpriteLoader playersprites = new SpriteLoader(new File("art/figur.png"), 64);
		Player p = new SimplePlayer(playersprites);
		Level l = new GreyLevel(10,10,64);
		Game g = new Game(10,10,64, p, l);
	}
}