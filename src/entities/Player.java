package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import utils.Constants;

import static utils.Helpers.canMoveHere;

public class Player extends Entity {

	private boolean goingLeft;
	private boolean goingRight;
	private boolean inMiddleArea;

	public Player(float x, float y, short width, short height, TileMap tileMap) {
		super(x, y, width, height, tileMap);
		init();
		initHitbox(x, y, Constants.Entities.Player.HITBOX_WIDTH, Constants.Entities.Player.HITBOX_HEIGHT);
		inMiddleArea = false;
	}

	@Override
	public void init() {
		// default values
		state = Constants.Entities.Player.IDLE;
		speedX = Constants.Entities.Player.DEFAULT_WALK_SPEED;
		maxHealth = Constants.Entities.Player.DEFAULT_HEALTH;
		health = maxHealth;
		width = Constants.Entities.Player.SPRITE_WIDTH;
		height = Constants.Entities.Player.SPRITE_HEIGHT;
		ticker = (byte) 0;
		idx = (byte) 0;
		facingRight = true;
		facingLeft = false;

		// sprite loading
		loadSprite();
		setupTileCount();
	}

	@Override
	public void setupTileCount() {
		tileColCount = new byte[Constants.Entities.Player.SPRITE_HEIGHT];
		tileColCount[0] = Constants.Entities.Player.IDLE_TILE_COUNT;
		tileColCount[1] = Constants.Entities.Player.WALK_TILE_COUNT;
		tileColCount[2] = Constants.Entities.Player.RUN_TILE_COUNT;
		tileColCount[3] = Constants.Entities.Player.JUMP_TILE_COUNT;
		tileColCount[4] = Constants.Entities.Player.ATK_1_TILE_COUNT;
		tileColCount[5] = Constants.Entities.Player.ATK_2_TILE_COUNT;
		tileColCount[6] = Constants.Entities.Player.ATK_3_TILE_COUNT;
		tileColCount[7] = Constants.Entities.Player.ATK_RUN_TILE_COUNT;
		tileColCount[8] = Constants.Entities.Player.DEFEND_TILE_COUNT;
		tileColCount[9] = Constants.Entities.Player.PROTECT_TILE_COUNT;
		tileColCount[10] = Constants.Entities.Player.HURT_TILE_COUNT;
		tileColCount[11] = Constants.Entities.Player.DEAD_TILE_COUNT;
	}

	@Override
	public void move() {
		// Temp
		float tempSpeedX = 0;
		float tempSpeedY = 0;

		if (moving) {
			switch (direction) {
			case Constants.Entities.Player.DIR_LEFT:
				// Temp
				tempSpeedX = -speedX;

				facingLeft = true;
				facingRight = false;
				break;
			case Constants.Entities.Player.DIR_RIGHT:
				// Temp
				tempSpeedX = speedX;

				facingRight = true;
				facingLeft = false;
				break;
			case Constants.Entities.Player.DIR_UP:
				// Temp
				tempSpeedY = -speedY;

				break;
			case Constants.Entities.Player.DIR_DOWN:
				// Temp
				tempSpeedY = speedY;

				break;
			default:
				break;
			}
		}

		if (!inMiddleArea) {
			if (x >= tileMap.getColCount() * Constants.Tile.WIDTH - Constants.Panel.WIDTH / 2) {
				if (canMoveHere(x + tempSpeedX, y + tempSpeedY, hitbox.width, hitbox.height, tileMap.getMap(),
						inMiddleArea, hitbox, tileMap)) {
					hitbox.x += tempSpeedX;
					hitbox.y += tempSpeedY;
					x += tempSpeedX;
					y += tempSpeedY;
				}
			} else {
				if (canMoveHere(hitbox.x + tempSpeedX, hitbox.y + tempSpeedY, hitbox.width, hitbox.height,
						tileMap.getMap(), inMiddleArea, hitbox, tileMap)) {
					hitbox.x += tempSpeedX;
					hitbox.y += tempSpeedY;
					x += tempSpeedX;
					y += tempSpeedY;
				}
			}
		} else {
			if (canMoveHere(x + tempSpeedX, y + tempSpeedY, hitbox.width, hitbox.height, tileMap.getMap(), inMiddleArea,
					hitbox, tileMap)) {
				x += tempSpeedX;
				y += tempSpeedY;
				if (direction == Constants.Entities.Player.DIR_DOWN || direction == Constants.Entities.Player.DIR_UP) {
					hitbox.y += tempSpeedY;
				}
			}
		}

	}

	@Override
	public void update() {
		switch (state) {
		case Constants.Entities.Player.WALK:
			speedX = Constants.Entities.Player.DEFAULT_WALK_SPEED;
			speedY = Constants.Entities.Player.DEFAULT_WALK_SPEED;
			break;
		case Constants.Entities.Player.RUN:
			speedX = Constants.Entities.Player.DEFAULT_RUN_SPEED;
			speedY = Constants.Entities.Player.DEFAULT_RUN_SPEED;
			break;
		case Constants.Entities.Player.JUMP:
			speedY = Constants.Entities.Player.DEFAULT_JUMP_HEIGHT;
			break;
		case Constants.Entities.Player.IDLE:
		case Constants.Entities.Player.ATK_1:
		case Constants.Entities.Player.ATK_2:
		case Constants.Entities.Player.ATK_3:
		case Constants.Entities.Player.ATK_RUN:
		case Constants.Entities.Player.DEFEND:
		case Constants.Entities.Player.PROTECT:
		case Constants.Entities.Player.HURT:
		case Constants.Entities.Player.DEAD:
			speedX = 0;
			break;
		default:
			break;
		}

		move();
		// checkTileMapCollision();
		// setPosition(xtemp, ytemp);
	}

	@Override
	public void attack() {
	}

	@Override
	public void die() {
	}

	@Override
	public void draw(Graphics g) {
		// setMapPosition();
		if (facingRight && !facingLeft) {
			if (x >= Constants.Panel.WIDTH / 2
					&& x <= Constants.Tile.WIDTH * tileMap.getColCount() - Constants.Panel.WIDTH / 2) {
				inMiddleArea = true;
				g.drawImage(spriteTile[state][idx],
						Constants.Panel.WIDTH / 2 - Constants.Entities.Player.HITBOX_X_OFFSET,
						(int) (hitbox.y - Constants.Entities.Player.HITBOX_Y_OFFSET),
						Constants.Entities.Player.SPRITE_WIDTH, Constants.Entities.Player.SPRITE_HEIGHT, null);
			} else {
				inMiddleArea = false;
				g.drawImage(spriteTile[state][idx], (int) (hitbox.x - Constants.Entities.Player.HITBOX_X_OFFSET),
						(int) (hitbox.y - Constants.Entities.Player.HITBOX_Y_OFFSET),
						Constants.Entities.Player.SPRITE_WIDTH, Constants.Entities.Player.SPRITE_HEIGHT, null);
			}
		} else {
			if (x >= Constants.Panel.WIDTH / 2
					&& x <= Constants.Tile.WIDTH * tileMap.getColCount() - Constants.Panel.WIDTH / 2) {
				inMiddleArea = true;
				g.drawImage(spriteTile[state][idx],
						Constants.Panel.WIDTH / 2 + width - Constants.Entities.Player.HITBOX_X_OFFSET,
						(int) (hitbox.y - Constants.Entities.Player.HITBOX_Y_OFFSET),
						-Constants.Entities.Player.SPRITE_WIDTH, Constants.Entities.Player.SPRITE_HEIGHT, null);
			} else {
				inMiddleArea = false;
				g.drawImage(spriteTile[state][idx],
						(int) (hitbox.x - Constants.Entities.Player.HITBOX_X_OFFSET) + width,
						(int) (hitbox.y - Constants.Entities.Player.HITBOX_Y_OFFSET),
						-Constants.Entities.Player.SPRITE_WIDTH, Constants.Entities.Player.SPRITE_HEIGHT, null);
			}
		}

		updateTicker();
		drawHitbox(g);

		// debugging
		System.out.println("x: " + x + " y: " + y);
		System.out.println("hb-x: " + hitbox.x + " hb-y: " + hitbox.y);
	}

	@Override
	public void loadSprite() {
		// get the input stream
		InputStream is = getClass().getResourceAsStream(Constants.Entities.Player.SPRITE);

		// load image
		try {
			spriteSheet = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// load individual tiles into array
		spriteTile = new BufferedImage[Constants.Entities.Player.SPRITE_ROWS][Constants.Entities.Player.SPRITE_COLS];

		for (int i = 0; i < Constants.Entities.Player.SPRITE_ROWS; i++)
			for (int j = 0; j < Constants.Entities.Player.SPRITE_COLS; j++) {
				spriteTile[i][j] = spriteSheet.getSubimage(j * Constants.Entities.Player.SPRITE_WIDTH,
						i * Constants.Entities.Player.SPRITE_HEIGHT, Constants.Entities.Player.SPRITE_WIDTH,
						Constants.Entities.Player.SPRITE_HEIGHT);
			}
	}

	public void halt() {
		moving = false;
		setGoingLeft(false);
		setGoingRight(false);
		state = Constants.Entities.Player.IDLE;
	}

	public boolean isGoingLeft() {
		return goingLeft;
	}

	public void setGoingLeft(boolean goingLeft) {
		this.goingLeft = goingLeft;
	}

	public boolean isGoingRight() {
		return goingRight;
	}

	public void setGoingRight(boolean goingRight) {
		this.goingRight = goingRight;
	}
}
