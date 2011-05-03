/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2011 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.nonlinear.basicstrategy.blackjack;

import org.encog.mathutil.randomize.RangeRandomizer;


public class Shoe {
	
	public static final int SUIT_HEARTS = 0;
	public static final int SUIT_DIAMONDS = 1;
	public static final int SUIT_SPADES = 2;
	public static final int SUIT_CLUBS = 3;
	
	public static final int TYPE_TWO = 0;
	public static final int TYPE_THREE = 1;
	public static final int TYPE_FOUR = 2;
	public static final int TYPE_FIVE = 3;
	public static final int TYPE_SIX = 4;
	public static final int TYPE_SEVEN = 5;
	public static final int TYPE_EIGHT = 6;
	public static final int TYPE_NINE = 7;
	public static final int TYPE_TEN = 8;
	public static final int TYPE_JACK = 9;
	public static final int TYPE_QUEEN = 10;
	public static final int TYPE_KING = 11;
	public static final int TYPE_ACE = 12;
	
	public static final String[] SUITS = { "Hearts", "Diamonds", "Spades", "Clubs" };
	public static final String[] TYPE = { "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"  };
	public static final int[] HARD_VALUE = { 2,3,4,5,6,7,8,9,10,10,10,10,11 };
	public static final int[] SOFT_VALUE = { 2,3,4,5,6,7,8,9,10,10,10,10,1 };
	
	public static final int CARDS_INA_DECK = 52;
	
	private int[] cards;
	
	private int currentCard;
	
	public Shoe(int decks)
	{
		this.cards = new int[decks*CARDS_INA_DECK];				
		shuffle();		
	}
	
	public void shuffle()
	{
		// mark all as unused
		for(int i = 0;i<cards.length;i++)
		{
			this.cards[i] = -1;
		}
		
		// randomize them
		for(int card = 0;card<cards.length;card++)		
		{
			boolean done = false;
			
			while(!done)
			{
				int index = (int)RangeRandomizer.randomize(0, cards.length);
				if( this.cards[index]==-1 )
				{
					this.cards[index] = card;
					done = true;
				}
			}
		}
		
		this.currentCard = 0;
	}
	
	public int nextCard()
	{
		return cards[currentCard++];
	}
	
	public int decks()
	{
		return cards.length/CARDS_INA_DECK;
	}
	
	
	public static String identifyCard(int card)
	{
		if( card>=52 )
			card = card % 52;
		
		
		StringBuilder result = new StringBuilder();
		
		int suit = identifySuit(card);
		int type = identifyType(card);
		
		result.append(TYPE[type]);
		result.append(" of ");
		result.append(SUITS[suit]);
		return result.toString();		
	}
	
	public static int identifySuit(int card)
	{
		if( card>=52 )
			card = card % 52;
		
		return card%4;
	}
	
	public static int identifyType(int card)
	{
		if( card>=52 )
			card = card % 52;
		
		return card/4;
	}
	
	public static boolean hasSoftValue(int card)
	{
		int type = identifyType(card);
		return( type==TYPE_ACE );
	}
	
	public static int hardValue(int card)
	{
		int suit = identifyType(card);
		return HARD_VALUE[suit];
	}
	
	public static int softValue(int card)
	{
		int suit = identifyType(card);
		return SOFT_VALUE[suit];
	}
	
	public double remainingPercent() {
		double cardsLeft = this.cards.length -  this.currentCard;
		double percent = cardsLeft/this.cards.length;
		return percent;
	}
}
