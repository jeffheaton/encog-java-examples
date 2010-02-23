package org.encog.examples.neural.gui.basicstrategy;

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
