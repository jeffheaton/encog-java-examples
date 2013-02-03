package org.encog.examples.neural.neat.boxes;

import java.util.Random;

import org.encog.mathutil.IntPair;

public class BoxTrialCase {

	private int resolution = 11;
	private int boundIdx = resolution - 1;

	private IntPair smallBoxTopLeft;
	private IntPair largeBoxTopLeft;

	private Random rnd;
	
	public BoxTrialCase(Random theRnd) {
		this.rnd = theRnd;
	}

	public IntPair initTestCase(int largeBoxRelativePos) {
		IntPair[] loc = generateRandomTestCase(largeBoxRelativePos);
		smallBoxTopLeft = loc[0];
		largeBoxTopLeft = loc[1];
		largeBoxTopLeft.add(-1);
		return loc[1];
	}

	public double getPixel(double x, double y) {
		int pixelX = (int) (((x + 1.0) * resolution) / 2.0);
		int pixelY = (int) (((y + 1.0) * resolution) / 2.0);

		if (smallBoxTopLeft.getX() == pixelX
				&& smallBoxTopLeft.getY() == pixelY) {
			return 1.0;
		}

		int deltaX = (int) (pixelX - largeBoxTopLeft.getX());
		int deltaY = (int) (pixelY - largeBoxTopLeft.getY());
		return (deltaX > -1 && deltaX < 3 && deltaY > -1 && deltaY < 3) ? 1.0
				: 0.0;
	}

	private IntPair[] generateRandomTestCase(int largeBoxRelativePos) {
		IntPair smallBoxPos = new IntPair(rnd.nextInt(resolution),
				rnd.nextInt(resolution));

		IntPair largeBoxPos = (IntPair)smallBoxPos.clone();
		switch (largeBoxRelativePos) {
		case 0:
			largeBoxPos.addX(5);
			break;
		case 1:
			largeBoxPos.addY(5);
			break;
		case 2:
			if (rnd.nextBoolean()) {
				largeBoxPos.add(3, 4);
			} else {
				largeBoxPos.add(4, 3);
			}
			break;
		}

		if (largeBoxPos.getX() > boundIdx) {
			largeBoxPos.addX(-resolution);

			if (0 == largeBoxPos.getX()) {
				largeBoxPos.add(1);
			}
		} else if (boundIdx == largeBoxPos.getX()) {
			largeBoxPos.addX(-1);
		} else if (largeBoxPos.getX() == 0) {
			largeBoxPos.addX(1);
		}

		if (largeBoxPos.getY() > boundIdx) {
			largeBoxPos.addY(-resolution);

			if (0 == largeBoxPos.getY()) {
				largeBoxPos.addY(1);
			}
		} else if (boundIdx == largeBoxPos.getY()) {
			largeBoxPos.addY(-1);
		} else if (0 == largeBoxPos.getY()) {
			largeBoxPos.addY(1);
		}
		return new IntPair[] { smallBoxPos, largeBoxPos };
	}
}
