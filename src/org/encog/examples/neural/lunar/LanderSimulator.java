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
	
	public LanderSimulator()
	{
		this.fuel = 200;
		this.seconds = 0;
		this.altitude = 10000;
		this.velocity = 0;	
	}
	
	public void turn(boolean thrust)
	{
		this.seconds++;
		this.velocity-=GRAVITY;
		this.altitude+=this.velocity;				
		
		if( thrust && this.fuel>0 )
		{
			this.fuel--;
			this.velocity+=THRUST;
		}
		
		this.velocity = Math.max(-TERMINAL_VELOCITY, this.velocity);
		this.velocity = Math.min(TERMINAL_VELOCITY, this.velocity);
		
		if( this.altitude<0)
			this.altitude = 0;
	}
	
	public String telemetry()
	{
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
		result.append((int)altitude);
		result.append(" m");
		return result.toString();
	}
	
	public int cost()
	{
		return (int)((this.fuel*10) + this.seconds + (this.velocity*1000));
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

	public boolean flying()
	{
		return(this.altitude>0);
	}	
}
