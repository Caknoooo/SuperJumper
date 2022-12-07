package gamestates;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Player;
import tilemap.Background;
import tilemap.TileMap;
import utils.Constants;

public class Level2State extends GameState {
	// Attribute 
	private TileMap tileMap;
	private GameStateManager gsm;
	private Background bg;
	private Player player;
	
	// Temp
	private int eventFinish;
	private int eventDead;
	private ArrayList<Rectangle> ls;
	
	public Level2State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		// Tilemap
		tileMap = new TileMap(Constants.Tile.WIDTH, Constants.Tile.HEIGHT);
		tileMap.loadTiles(Constants.TileSets.LVL_2);
		tileMap.loadMap(Constants.Maps.LVL_2);
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		// Background
		bg = new Background(Constants.Backgrounds.LVL_2);

		// LevelManager and Player
		this.player = new Player(100, 100, 
				(short) Constants.Entities.Player.SPRITE_WIDTH, (short) Constants.Entities.Player.SPRITE_HEIGHT, tileMap);
		
		
	}

	@Override
	public void update() {
		// Update Player
		player.update();
		
		// Set position tileMap
		tileMap.setPosition(Constants.Panel.WIDTH / 2 - player.getX(), Constants.Panel.HEIGHT / 2 - player.getY());
		
		// Set background position
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
	}

	@Override
	public void draw(Graphics2D g) {
		// Draw Bg
		bg.draw(g);
		
		// Draw TileMap
		tileMap.draw(g);
		
		//Draw player
		player.draw(g);
		
	}

}
