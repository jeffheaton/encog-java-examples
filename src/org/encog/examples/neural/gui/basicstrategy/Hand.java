package org.encog.examples.neural.gui.basicstrategy;

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
