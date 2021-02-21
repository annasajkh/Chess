package com.github.annasajkh;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Pieces
{
	static enum Type
	{
		BISHOP,
		KING,
		KNIGHT,
		PAWN,
		QUEEN,
		ROOK,
		EMPTY
	}
	
	Sprite sprite;
	Type type;
	float x;
	float y;
	int i = 0;
	int j = 0;
	boolean isBlack;
	
	public Pieces(Type type, boolean isBlack,int i ,int j)
	{
		this.type = type;
		this.isBlack = isBlack;
		this.i = i;
		this.j = j;
		switch(type)
		{
			case BISHOP:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackBishop.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whiteBishop.png"));
				}
				break;
			
			case KING:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackKing.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whiteKing.png"));
				}
				break;
			
			case KNIGHT:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackKnight.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whiteKnight.png"));
				}
				break;
			
			case PAWN:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackPawn.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whitePawn.png"));
				}
				break;
			
			case QUEEN:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackQueen.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whiteQueen.png"));
				}
				break;
				
			case ROOK:
				if(isBlack)
				{
					sprite = new Sprite(new Texture("blackRook.png"));
				}
				else
				{
					sprite = new Sprite(new Texture("whiteRook.png"));
				}
			case EMPTY:
			
		}
	}
	
	public void move()
	{

		out:
		for (int i = 0; i < Game.pieces.length; i++)
		{
			for (int j = 0; j < Game.pieces[i].length; j++)
			{
				if(new Rectangle(j * 75 , i * 75, 75, 75).contains(Game.mousePos.x,Game.mousePos.y))
				{
					
					if(Game.LegalMoves.contains(Game.pieces[i][j]) && (!isFriend(Game.pieces[i][j]) || Game.pieces[i][j].type == Type.EMPTY))
					{
						Game.pieces[i][j] = this;
						this.i = i;
						this.j = j;
						
						if(isBlack)
						{
							if(getLegalMoves().contains(Game.whiteKing))
							{
								Game.whiteCheck = true;
							}
							else
							{
								Game.whiteCheck = false;
							}

						}
						else
						{
							if(getLegalMoves().contains(Game.blackKing))
							{
								Game.blackCheck = true;
							}
							else
							{
								Game.whiteCheck = false;
							}
						}
						
						
						if(Game.isFirstTime > 0 && type == Type.BISHOP)
						{
							Game.isFirstTime--;
						}
						Game.whiteTurn = !Game.whiteTurn;
						Game.carring = null;
					}
					Game.LegalMoves.clear();
					break out;
				}
			}
		}
	}
	
	public boolean isNotOutOfBound(int i, int j)
	{
		return i > -1 && i < Game.pieces.length && j > -1 && j < Game.pieces[0].length;
	}
	
	public boolean isFriend(Pieces otherPieces)
	{
		return isBlack == otherPieces.isBlack;
	}
	
	public List<Pieces> getHorizontalandVerticalMoves(int limit)
	{
		int index = 1;
		
		List<Pieces> horizontalAndVerticalMoves = new ArrayList<>();
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i + index,j))
			{
				break;
			}
			if(Game.pieces[i + index][j].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i + index][j]))
				{
					horizontalAndVerticalMoves.add(Game.pieces[i + index][j]);
				}
				break;
			}
			horizontalAndVerticalMoves.add(Game.pieces[i + index][j]);
			index++;
		}
		
		index = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i - index,j))
			{
				break;
			}
			if(Game.pieces[i - index][j].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i - index][j]))
				{
					horizontalAndVerticalMoves.add(Game.pieces[i - index][j]);
				}
				break;
			}
			horizontalAndVerticalMoves.add(Game.pieces[i - index][j]);
			index++;
		}
		index = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i ,j + index))
			{
				break;
			}
			if(Game.pieces[i][j + index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i][j + index]))
				{
					horizontalAndVerticalMoves.add(Game.pieces[i][j + index]);
				}
				break;
			}
			horizontalAndVerticalMoves.add(Game.pieces[i][j + index]);
			index++;
		}
		
		index  = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i ,j - index))
			{
				break;
			}
			if(Game.pieces[i][j - index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i][j - index]))
				{
					horizontalAndVerticalMoves.add(Game.pieces[i][j - index]);
				}
				break;
			}
			horizontalAndVerticalMoves.add(Game.pieces[i][j - index]);
			index++;
		}
		
		return horizontalAndVerticalMoves;
	}

	public List<Pieces> getDiagonalMoves(int limit)
	{
		int index = 1;
		List<Pieces> diagonalMoves = new ArrayList<>();
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i + index,j + index))
			{
				break;
			}
			if(Game.pieces[i + index][j + index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i + index][j + index]))
				{
					diagonalMoves.add(Game.pieces[i + index][j + index]);
				}
				break;
			}
			diagonalMoves.add(Game.pieces[i + index][j + index]);
			index++;
		}
		
		index = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i - index,j - index))
			{
				break;
			}
			if(Game.pieces[i - index][j - index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i - index][j - index]))
				{
					diagonalMoves.add(Game.pieces[i - index][j - index]);
				}
				break;
			}
			diagonalMoves.add(Game.pieces[i - index][j - index]);
			index++;
		}
		index = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i - index ,j + index))
			{
				break;
			}
			if(Game.pieces[i - index][j + index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i - index][j + index]))
				{
					diagonalMoves.add(Game.pieces[i - index][j + index]);
				}
				break;
			}
			diagonalMoves.add(Game.pieces[i - index][j + index]);
			index++;
		}
		
		index  = 1;
		
		for(int n = 0; n < limit; n++)
		{
			if(!isNotOutOfBound(i  + index,j - index))
			{
				break;
			}
			if(Game.pieces[i + index][j - index].type != Type.EMPTY)
			{
				if(!isFriend(Game.pieces[i + index][j - index]))
				{
					diagonalMoves.add(Game.pieces[i + index][j - index]);
				}
				break;
			}
			diagonalMoves.add(Game.pieces[i + index][j - index]);
			index++;
		}
		
		return diagonalMoves;
	}
	
	public List<Pieces> getBishopMoves()
	{
		List<Pieces> legalMoves = new ArrayList<>();
		
		int flip = isBlack ? -1 : 1;

		if(isNotOutOfBound(i + 1 * flip,j + 1 * flip))
		{
			legalMoves.add(Game.pieces[i + 1 * flip][j + 1 * flip]);
		}
		
		if(isNotOutOfBound(i + 1 * flip,j - 1 * flip))
		{
			legalMoves.add(Game.pieces[i + 1 * flip][j - 1 * flip]);
		}
		
		return legalMoves;
	}

	public List<Pieces> getLegalMoves()
	{
		List<Pieces> legalMoves = new ArrayList<>();
		
		switch(type)
		{
			case BISHOP:
				int flip = isBlack ? -1 : 1;

				if(isNotOutOfBound(i + 1 * flip,j + 1 * flip))
				{
					if(Game.pieces[i + 1 * flip][j + 1 * flip].type != Type.EMPTY
							&& !isFriend(Game.pieces[i + 1 * flip][j + 1 * flip]))
					{
						legalMoves.add(Game.pieces[i + 1 * flip][j + 1 * flip]);
					}
				}
				
				if(isNotOutOfBound(i + 1 * flip,j - 1 * flip))
				{
					if(Game.pieces[i + 1 * flip][j - 1 * flip].type != Type.EMPTY 
							&& !isFriend(Game.pieces[i + 1 * flip][j - 1 * flip]))
					{
						legalMoves.add(Game.pieces[i + 1 * flip][j - 1 * flip]);
					}
				}
				
				if(Game.isFirstTime != 0)
				{
					for (int n = 1; n < 3; n++)
					{
						if(isNotOutOfBound(i + n * flip,j))
						{
							if(Game.pieces[i + n * flip][j].type == Type.EMPTY)
							{
								legalMoves.add(Game.pieces[i + n * flip][j]);
							}
							else
							{
								break;
							}
						}
					}
				}
				else
				{
					if(isNotOutOfBound(i + flip,j))
					{
						if(Game.pieces[i + flip][j].type == Type.EMPTY)
						{
							legalMoves.add(Game.pieces[i + flip][j]);
						}
					}
				}
				break;
			
			case KING:
				legalMoves.addAll(getHorizontalandVerticalMoves(1));
				legalMoves.addAll(getDiagonalMoves(1));
				break;
			
			case KNIGHT:
				//up down
				if(isNotOutOfBound(i + 2,j - 1))
				{
					if(!isFriend(Game.pieces[i + 2][j - 1]) || Game.pieces[i + 2][j - 1].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i + 2][j - 1]);
					}
				}
				if(isNotOutOfBound(i + 2,j + 1))
				{
					if(!isFriend(Game.pieces[i + 2][j + 1]) || Game.pieces[i + 2][j + 1].type == Type.EMPTY )
					{
						legalMoves.add(Game.pieces[i + 2][j + 1]);
					}
				}
				
				if(isNotOutOfBound(i - 2,j - 1))
				{
					if(!isFriend(Game.pieces[i - 2][j - 1]) || Game.pieces[i - 2][j - 1].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i - 2][j - 1]);
					}
				}
				if(isNotOutOfBound(i - 2,j + 1))
				{
					if(!isFriend(Game.pieces[i - 2][j + 1]) || Game.pieces[i - 2][j + 1].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i - 2][j + 1]);
					}
				}
				
				
				//left right
				if(isNotOutOfBound(i - 1 ,j + 2))
				{
					if(!isFriend(Game.pieces[i - 1][j + 2]) || Game.pieces[i - 1][j + 2].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i - 1][j + 2]);
					}
				}
				
				if(isNotOutOfBound(i + 1,j + 2))
				{
					if(!isFriend(Game.pieces[i + 1][j + 2]) || Game.pieces[i + 1][j + 2].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i + 1][j + 2]);
					}
				}
				
				if(isNotOutOfBound(i - 1,j - 2))
				{
					if(!isFriend(Game.pieces[i - 1][j - 2]) || Game.pieces[i - 1][j - 2].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i - 1][j - 2]);
					}
				}
				if(isNotOutOfBound(i + 1,j - 2))
				{
					if(!isFriend(Game.pieces[i + 1][j - 2]) || Game.pieces[i + 1][j - 2].type == Type.EMPTY)
					{
						legalMoves.add(Game.pieces[i + 1][j - 2]);
					}
				}
				break;
			
			case PAWN:
				legalMoves.addAll(getDiagonalMoves(100));
				break;
			
			case QUEEN:
				legalMoves.addAll(getHorizontalandVerticalMoves(100));
				legalMoves.addAll(getDiagonalMoves(100));
				break;
				
			case ROOK:
				legalMoves.addAll(getHorizontalandVerticalMoves(100));
			case EMPTY:
			
		}
		
		return legalMoves;
	}
	
	public void draw(SpriteBatch spriteBatch,float x, float y)
	{

		if(type != Type.EMPTY)
		{
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
				new Rectangle(this.x , this.y, 75, 75).contains(Game.mousePos.x,Game.mousePos.y))
			{
				if(Game.carring == null)
				{
					Game.pieces[this.i][this.j] = new Pieces(Type.EMPTY,false,this.i,this.j);
					
					boolean illegalMove = false;
					
					if(type != Type.KING)
					{
						if(this.isBlack)
						{
							for(Pieces pieces : Game.whitePieces)
							{
								if(pieces.getLegalMoves().contains(Game.blackKing))
								{
									illegalMove = true;
									break;
								}
							}
	
						}
						else
						{
							for(Pieces pieces : Game.blackPieces)
							{
								if(pieces.getLegalMoves().contains(Game.whiteKing))
								{
									illegalMove = true;
									break;
								}
							}
						}
					}
					Game.LegalMoves = getLegalMoves();
					
					if(Game.whiteTurn == isBlack)
					{

						System.out.println("is not your turn!!!");
						Game.LegalMoves.clear();
					}
					
					if(type == Type.KING)
					{
						if(this.isBlack)
						{
							List<Pieces> remove = new ArrayList<>();
							
							for(Pieces pieces : Game.whitePieces)
							{
								for(Pieces kingMove : Game.LegalMoves)
								{
									if(pieces.type == Type.BISHOP)
									{
										if(pieces.getBishopMoves().contains(kingMove) && !remove.contains(kingMove))
										{
											remove.add(kingMove);
										}
									}
									else
									{
										if(pieces.getLegalMoves().contains(kingMove) && !remove.contains(kingMove))
										{
											remove.add(kingMove);
										}
									}
								}
							}
							Game.LegalMoves.removeAll(remove);
						}
						else
						{
							List<Pieces> remove = new ArrayList<>();
							
							for(Pieces pieces : Game.blackPieces)
							{
								for(Pieces kingMove : Game.LegalMoves)
								{
									if(pieces.type == Type.BISHOP)
									{
										if(pieces.getBishopMoves().contains(kingMove))
										{
											remove.add(kingMove);
										}
									}
									else
									{
										if(pieces.getLegalMoves().contains(kingMove))
										{
											remove.add(kingMove);
										}
									}
								}
							}
							Game.LegalMoves.removeAll(remove);
						}
					}
					
					if(this.isBlack && Game.blackCheck && type != Type.KING)
					{
						Game.LegalMoves.clear();
					}
					
					if(!this.isBlack && Game.whiteCheck && type != Type.KING)
					{
						Game.LegalMoves.clear();
					}
					
					if(illegalMove)
					{
						System.out.println("illegal move!!");
						Game.LegalMoves.clear();
					}
					
					Game.carring = this;
				}
				
				if(Game.carring != null)
				{
					if(Game.carring.equals(this))
					{
						this.x = Game.mousePos.x - 75 * 0.5f;
						this.y = Game.mousePos.y - 75 * 0.5f;
					}
				}
				
			}
			else
			{
				this.x = x;
				this.y = y;
			}
			spriteBatch.draw(sprite, (this.x + 75 * 0.5f) - sprite.getWidth() * 0.5f, (this.y + 75 * 0.5f) - sprite.getHeight() * 0.5f);
		}
	}
	
}
