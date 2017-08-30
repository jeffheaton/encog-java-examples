/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
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
package org.encog.examples.neural.lunar;

import java.text.NumberFormat;

public class LanderSimulator {

	public static final double GRAVITY = 1.62;
	public static final double THRUST = 10;
	public static final double TERMINAL_VELOCITY = 40;

	private int fuel;
	private int seconds;
	private double altitude;
	private double velocity;

	public LanderSimulator() {
		this.fuel = 200;
		this.seconds = 0;
		this.altitude = 10000;
		this.velocity = 0;
	}

	public void turn(boolean thrust) {
		this.seconds++;
		this.velocity -= GRAVITY;
		this.altitude += this.velocity;

		if (thrust && this.fuel > 0) {
			this.fuel--;
			this.velocity += THRUST;
		}

		this.velocity = Math.max(-TERMINAL_VELOCITY, this.velocity);
		this.velocity = Math.min(TERMINAL_VELOCITY, this.velocity);

		if (this.altitude < 0)
			this.altitude = 0;
	}

	public String telemetry() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(4);
		nf.setMaximumFractionDigits(4);
		StringBuilder result = new StringBuilder();
		result.append("Elapsed: ");
		result.append(seconds);
		result.append(" s, Fuel: ");
		result.append(this.fuel);
		result.append(" l, Velocity: ");
		result.append(nf.format(velocity));
		result.append(" m/s, ");
		result.append((int) altitude);
		result.append(" m");
		return result.toString();
	}

	public int score() {
		return (int) ((this.fuel * 10) + this.seconds + (this.velocity * 1000));
	}

	public int getFuel() {
		return fuel;
	}

	public int getSeconds() {
		return seconds;
	}

	public double getAltitude() {
		return altitude;
	}

	public double getVelocity() {
		return velocity;
	}

	public boolean flying() {
		return (this.altitude > 0);
	}
}
