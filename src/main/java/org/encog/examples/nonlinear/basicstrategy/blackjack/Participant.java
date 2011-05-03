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
