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


public class Hand {
	private int[] hand = new int[21];
	private int handIndex;
	private double bet;
	private boolean done;
	
	public Hand(final double bet)
	{
		this.bet = bet;
		this.done = false;
	}
		
	public void giveCard(int card)
	{
		this.hand[handIndex++] = card;
	}
	
	public int calculateTotal()
	{
		int total = 0;
		
		// first add up all the "hard number" cards
		for(int i=0;i<handIndex;i++)
		{
			if( !Shoe.hasSoftValue(hand[i]) )
			{
				total+=Shoe.hardValue(hand[i]);
			}
		}
		
		// now add up soft values, maximize score, but don't bust!
		for(int i=0;i<handIndex;i++)
		{
			if( Shoe.hasSoftValue(hand[i]) )
			{
				if( total<=10 )
					total+=Shoe.hardValue(hand[i]);
				else
					total+=Shoe.softValue(hand[i]);
			}
		}
			
		return total;
	}
	
	public int[] getHand()
	{
		return this.hand;
	}
	
	public int getHandIndex()
	{
		return this.handIndex;
	}	
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		for(int i=0;i<this.handIndex;i++)
		{
			if( i>0 )
				result.append(',');
			result.append(Shoe.identifyCard(hand[i]));
		}
		
		result.append(",points:");
		result.append(calculateTotal());
		
		return result.toString();
	}

	public boolean isPair() {
		if( handIndex !=2 )
			return false;
		return Shoe.identifyType(hand[0])==Shoe.identifyType(hand[1]);
	}

	public boolean isHardTotal() {
		boolean hardTotal = true;
		int total = 0;
		
		// first add up all the "hard number" cards
		for(int i=0;i<handIndex;i++)
		{
			if( !Shoe.hasSoftValue(hand[i]) )
			{
				total+=Shoe.hardValue(hand[i]);
			}
		}
		
		// now add up soft values, maximize score, but don't bust!
		for(int i=0;i<handIndex;i++)
		{
			if( Shoe.hasSoftValue(hand[i]) )
			{
				if( total<=10 )
					total+=Shoe.hardValue(hand[i]);
				else
				{
					total+=Shoe.softValue(hand[i]);
					hardTotal = false;
				}
			}
		}
			
		return hardTotal || (total>10);	
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void setHandIndex(int handIndex) {
		this.handIndex = handIndex;
	}
	
	public boolean isBlackJack()
	{
		if( this.handIndex!=2 )
			return false;
		return this.calculateTotal()==21;
			
	}
	
	
}
