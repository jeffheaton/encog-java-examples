/*
 * Encog(tm) Examples v2.6 
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/
 
 * Copyright 2008-2010 Heaton Research, Inc.
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
package org.encog.examples.unfinished.pole;

import org.encog.mathutil.randomize.RangeRandomizer;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.simple.EncogUtility;

public class PoleSimulator {

	/**
	 * acceleration due to gravity [m/s�]
	 */
	public static final double G = 9.81;

	/**
	 * length of pole [m]
	 */
	final double poleLength;

	/**
	 * mass of cart [kg]
	 */
	final double cartMass;

	/**
	 * mass of pole [kg]
	 */
	final double poleMass;

	/**
	 * position of cart [m]
	 */
	double x;

	/**
	 * velocity of cart [m/s]
	 */
	double xVelocity;

	/**
	 * angle of pole [�]
	 */
	double poleAngle;

	/**
	 * angular velocity of pole [�/s]
	 */
	double poleAngleVelocity;

	/**
	 * force applied to cart
	 */
	double force;

	public PoleSimulator()
	{
		this(1,1,1);
	}
	
	public PoleSimulator(double poleLength, double cartMass, double poleMass) {
		this.poleLength = poleLength;
		this.cartMass = cartMass;
		this.poleMass = poleMass;

		this.x = 0;
		this.xVelocity = 0;
		this.poleAngleVelocity = 0;
		this.force = 0;

		do {
			this.poleAngle = RangeRandomizer.randomize(-10, 10);
		} while ((int) this.poleAngle == 0);
	}
	
	void simulate()
	{
		simulate(10,0.1);
	}

	void simulate(int steps, double timePerStep) {

		double w = (this.poleAngle / 180.0) * Math.PI;
		double wDot = (this.poleAngleVelocity / 180.0) * Math.PI;

		for (int s = 0; s < steps; s++) {

			double wDotDot = (G * Math.sin(w) + Math.cos(w)
					* ((-this.force - this.poleMass * this.poleLength
							* Math.sqrt(wDot) * Math.sin(w)) / (this.cartMass + this.poleMass)))
					/ (this.poleLength * ((double) 4 / 3 - (this.poleMass * Math
							.sqrt(Math.cos(w)))
							/ (this.cartMass + this.poleMass)));

			double xDotDot = (this.force + this.poleMass * this.poleLength
					* (Math.sqrt(wDot) * Math.sin(w) - wDotDot * Math.cos(w)))
					/ (this.cartMass + this.poleMass);

			this.x += (timePerStep / steps) * xVelocity;
			xVelocity += (timePerStep / steps) * xDotDot;
			w += (timePerStep / steps) * wDot;
			wDot += (timePerStep / steps) * wDotDot;
		}

		this.poleAngle = (w / Math.PI) * 180;
		this.poleAngleVelocity = (wDot / Math.PI) * 180;
	}

	boolean balanced() {
		return (this.poleAngle >= -60) && (this.poleAngle <= 60);
	}

	double scorePole() {
		return -Math.sqrt(this.poleAngle);
	}
	
	public void setForce(double force)
	{
		this.force = force;
	}
	
	public double getForce()
	{
		return this.force;
	}
	
	public double getPoleAngle()
	{
		return this.poleAngle;
	}
	
	public double getPoleAngleVelocity()
	{
		return this.poleAngleVelocity;
	}
	
	public static void main(String[] args)
	{
		BasicNetwork network = EncogUtility.simpleFeedForward(2, 10, 0, 1, true);
		network.reset();
		ScorePole score = new ScorePole();
		System.out.println(score.score(network));
	}

}
