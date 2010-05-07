/*
 * Encog(tm) Examples v2.4
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 * 
 * Copyright 2008-2010 by Heaton Research Inc.
 * 
 * Released under the LGPL.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * 
 * Encog and Heaton Research are Trademarks of Heaton Research, Inc.
 * For information on Heaton Research trademarks, visit:
 * 
 * http://www.heatonresearch.com/copyright.html
 */

package org.encog.examples.nonlinear.basicstrategy.blackjack;

import java.util.ArrayList;
import java.util.List;


public abstract class Participant {
	
	public static final char ACTION_HIT = 'h';
	public static final char ACTION_STAND = 's';
	public static final char ACTION_DOUBLE = 'd';
	public static final char ACTION_SPLIT = 'l';
	public static final char ACTION_SURRENDER = 'r';
	private List<Hand> hands = new ArrayList<Hand>();
	private int currentHand;

	public void reset(double bet)
	{
		this.currentHand = 0;
		this.hands.clear();
		this.createHand(bet );
	}
	
	public Hand createHand(double bet)
	{
		Hand hand = new Hand(bet);
		this.hands.add(hand);
		return hand;
	}
	
	public void giveCard(int card)
	{
		hands.get(currentHand).giveCard(card);
	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		int handNo = 1;
		for(Hand hand: this.hands)
		{
			result.append("Hand #");
			result.append(handNo++);
			result.append(':');
			result.append(hand.toString());
			if(this.hands.size()>1)
				result.append("\n");
		}
		return result.toString();
	}
	
	public Hand getCurrentHand()
	{
		return this.hands.get(this.currentHand);
	}
	
	public List<Hand> getHands()
	{
		return this.hands;
	}
	
	public abstract char play(Table table);	
}
