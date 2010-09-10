package org.encog.examples.neural.concurrent;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This example is still a work in progress.
 *
 */
public class ConcurrentDJIA {
	
	public static final String[] DJIA = 
	{
		"MMM", // 3M
		"AA",// Alcoa
		"AXP",// American Express
		"T", // AT&T 
		"BAC", // Bank of America
		"BA", // Boeing
		"CAT", // Caterpillar
		"CVX", // Chevron Corporation
		"CSCO", // Cisco Systems
		"KO", // Coca-Cola 	
		"DD", // DuPont
		"XOM", // ExxonMobil
		"GE", // General Electric
		"HPQ", // Hewlett-Packard
		"HD", // The Home Depot 
		"INTC", // Intel
		"IBM", // IBM
		"JNJ", // Johnson & Johnson
		"JPM", // JPMorgan Chase
		"KFT", // Kraft Foods
		"MCD", // McDonald's
		"MRK", // Merck
		"MSFT", // Microsoft
		"PFE", // Pfizer
		"PG", // Procter & Gamble 	
		"TRV", // Travelers 	
		"UTX", // United Technologies Corporation 	UTX 	Conglomerate 	1939-03-14 1939-03-14 (as United Aircraft)
		"VZ", // Verizon Communications
		"WMT", // Wal-Mart
		"DIS" // Walt Disney
	};
	
	public static final int HIDDEN1_COUNT = 20;
	public static final int HIDDEN2_COUNT = 0;
	public static final Calendar TRAIN_BEGIN = new GregorianCalendar(2000, 0, 1);
	public static final Calendar TRAIN_END = new GregorianCalendar(2008, 12, 31);
	public static final int INPUT_WINDOW = 10;
	public static final int PREDICT_WINDOW = 1;
	
	
	public static void main(String[] args)
	{
		
	}
}
