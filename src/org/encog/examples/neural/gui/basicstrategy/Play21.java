package org.encog.examples.neural.gui.basicstrategy;

public class Play21 {
	public static void main(String[] args)
	{
		/*for(int i=0;i<52;i++) {
			System.out.print(identifyCard(i));
			System.out.print(" ");
			System.out.print(hasSoftValue(i));
			System.out.print(" ");
			System.out.print(softValue(i));
			System.out.print(" ");
			System.out.print(hardValue(i));
			
			System.out.println();
		}*/
		
		Table table = new Table(1, new Dealer());
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		table.addPlayer(new Player(1000));
		for(int i=0;i<10;i++) {
			table.play();
		}
		
	}

}
