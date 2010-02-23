package org.encog.examples.neural.gui.basicstrategy;

public class Dealer extends Participant {

	@Override
	public char play(Table table) {
		
		Hand hand = getCurrentHand();
		int score = hand.calculateTotal();
		
		if( score>17 )
			return Participant.ACTION_STAND;
		else
			return Participant.ACTION_HIT;
	}

}
