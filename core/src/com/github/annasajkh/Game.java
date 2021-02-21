package com.github.annasajkh;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter
{
	
	static ShapeRenderer shapeRenderer;
	static Pieces[][] pieces;
	SpriteBatch spriteBatch;
	OrthographicCamera camera;
	static Vector3 mousePos = new Vector3();
	static Pieces carring;
	static List<Pieces> LegalMoves;
	static List<Pieces> whitePieces;
	static List<Pieces> blackPieces;
	static Pieces whiteKing;
	static Pieces blackKing;
	static byte isFirstTime = 2;
	static boolean whiteTurn = true;
	static boolean blackCheck = false;
	static boolean whiteCheck = false;
	
	
	public void drawBoard(ShapeRenderer shapeRenderer)
	{
		boolean flip = true;
		
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if(flip)
				{
					shapeRenderer.setColor(Color.LIME);
				}
				else
				{
					shapeRenderer.setColor(Color.GREEN);
				}
				shapeRenderer.rect(i * 75, j * 75, 75, 75);
				if (carring != null)
				{
					shapeRenderer.setColor(Color.YELLOW);
					shapeRenderer.rect(carring.j * 75, carring.i * 75, 75, 75);
				}
				for(Pieces pieces : Game.LegalMoves)
				{
					shapeRenderer.setColor(Color.YELLOW);
					shapeRenderer.rect(pieces.j * 75, pieces.i * 75, 75, 75);
				}
				flip = !flip;
			}
			flip = !flip;
		}
	}
	
	public void drawPieces(SpriteBatch spriteBatch)
	{
		if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && carring != null)
		{
			carring.move();
			LegalMoves.clear();
		}
		
		if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && carring != null)
		{
			Game.pieces[carring.i][carring.j] = carring;
			carring = null;
		}
		
		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				if(Game.carring != null)
				{
					if(!Game.carring.equals(pieces[i][j]))
					{
						pieces[i][j].draw(spriteBatch,j * 75 ,i * 75);
					}
				}
				else
				{
					pieces[i][j].draw(spriteBatch,j * 75 ,i * 75);
				}
			}
		}
		if(Game.carring != null)
		{
			Game.carring.draw(spriteBatch,mousePos.x - 75 * 0.5f,mousePos.y - 75 * 0.5f);
		}
	}
	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = Gdx.graphics.getWidth() * 0.5f;
		camera.position.y = Gdx.graphics.getHeight() * 0.5f;
		pieces = new Pieces[8][8];
		
		LegalMoves = new ArrayList<>();
		whitePieces = new ArrayList<>();
		blackPieces = new ArrayList<>();
		
		Pieces.Type[][] setBoard = {
				{Pieces.Type.ROOK,Pieces.Type.KNIGHT,Pieces.Type.PAWN,Pieces.Type.KING,Pieces.Type.QUEEN,Pieces.Type.PAWN,Pieces.Type.KNIGHT,Pieces.Type.ROOK},
				{Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP},
				{Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY},
				{Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY},
				{Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY},
				{Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY,Pieces.Type.EMPTY},
				{Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP,Pieces.Type.BISHOP},
				{Pieces.Type.ROOK,Pieces.Type.KNIGHT,Pieces.Type.PAWN,Pieces.Type.KING,Pieces.Type.QUEEN,Pieces.Type.PAWN,Pieces.Type.KNIGHT,Pieces.Type.ROOK}
		};
		
		boolean flip = false;
		
		for (int i = 0; i < pieces.length; i++)
		{
			for (int j = 0; j < pieces[i].length; j++)
			{
				pieces[i][j] = new Pieces(setBoard[i][j],flip,i,j);
				
				if(flip && pieces[i][j].type == Pieces.Type.KING)
				{
					blackKing = pieces[i][j];
				}
				else if(!flip && pieces[i][j].type == Pieces.Type.KING)
				{
					whiteKing = pieces[i][j];
				}
				
				if(flip && pieces[i][j].type != Pieces.Type.KING)
				{
					blackPieces.add(pieces[i][j]);
				}
				else if(!flip && pieces[i][j].type != Pieces.Type.KING)
				{
					whitePieces.add(pieces[i][j]);
				}
			}
			
			if( i == 2)
			{
				flip = !flip;
			}
		}
	}

	@Override
	public void render()
	{

		shapeRenderer.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		camera.update();
		mousePos.x = Gdx.input.getX();
		mousePos.y = Gdx.input.getY();
		camera.unproject(mousePos);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Filled);
		drawBoard(shapeRenderer);
		shapeRenderer.end();
		
		spriteBatch.begin();
		drawPieces(spriteBatch);
		spriteBatch.end();
	}
	
	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
	}
}
