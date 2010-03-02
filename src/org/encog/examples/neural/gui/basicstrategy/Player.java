package org.encog.examples.neural.gui.basicstrategy;

import org.encog.mathutil.randomize.RangeRandomizer;


public class Player extends Participant {
	
	// pair grid: 10x10
	// hard grid: 10x20
	// soft grid: 10x10
	
	private Character[] rules;
	
	private double money;
	
	public Player(double money)
	{
		this.money = money;
		randomize();
	}
	
	public Character[] getRules()
	{
		return this.rules;
	}
	
	public void takeMoney(double d)
	{
		this.money-=d;
	}
	

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
		
	@Override
	public char play(Table table) {
		
		Hand hand = getCurrentHand();
		Hand dealerHand = table.getDealer().getCurrentHand();
		int upCard = dealerHand.getHand()[1];
		int dealerCardIndex = Shoe.hardValue(upCard)-1;
		
		
		if( hand.isPair() )
		{
			int pairCard = getCurrentHand().getHand()[0];
			int pairCardIndex = Shoe.hardValue(pairCard)-1;
			return this.rules[(dealerCardIndex*10)+pairCardIndex];
		}
		else if( hand.isHardTotal() )
		{
			int hardIndex = getCurrentHand().calculateTotal()-1;
			return this.rules[100+(dealerCardIndex*20)+hardIndex];
		}
		else
		{		
			int softIndex = getCurrentHand().calculateTotal()-1;
			return this.rules[300+(dealerCardIndex*10)+softIndex];
		}
	}
	
	public void reset(double bet)
	{
		super.reset(bet);
	}
	
	public void randomize()
	{
		this.rules = new Character[400];
		
		// process pairs
		String pairChoices = "hsdlr";
		for(int i=0;i<100;i++)
		{
			int index = (int)RangeRandomizer.randomize(0,pairChoices.length());
			rules[i] = pairChoices.charAt(index);
		}		
		
		// process hard
		String hardChoices = "hsdr";
		for(int i=0;i<200;i++)
		{
			int index = (int)RangeRandomizer.randomize(0,hardChoices.length());
			rules[100+i] = hardChoices.charAt(index);
		}		
		
		// process soft
		String softChoices = "hsdr";
		for(int i=0;i<100;i++)
		{
			int index = (int)RangeRandomizer.randomize(0,softChoices.length());
			rules[300+i] = softChoices.charAt(index);
		}		

		
		

	}
	
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(super.toString());
		result.append(",money=");
		result.append(this.money);
		return result.toString();
	}

	public void giveMoney(double d) {
		this.money+=d;		
	}
	
	
}
