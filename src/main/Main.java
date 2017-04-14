package main;

import game.controller.Game;
import game.io.ResourceLoader;
import game.io.ResourceLoaderCSV;
import game.io.ResourceLoaderSQL;

public class Main {
	
	/**
	 * Dette laster inn en ResourceLoader som kan hente ut brett og slikt.
	 * Deretter lager det et nytt Game-objekt som det kjører start på.
	 * @param args blir ignorert
	 * @throws Throwable Vi kaster alle exceptions. Dette er egentlig en dum ting.
	 */
	public static void main(String[] args) throws Throwable {
		/* 
		 * Når dere er ferdig med ResourceLoaderSQL-klassen, kan dere gjøre:
		 * ResourceLoader loader = new ResourceLoaderSQL() istedenfor.
		 * Det blir den eneste forskjellen for resten av spillet.
		 */
		ResourceLoader loader = new ResourceLoaderSQL(); 
		
		Game game = new Game(loader);
		game.start();
		
		System.out.println("Spill over, avslutter");
		game.shutdown();
		System.exit(0); // Fordi ordentlig kode for å slå av ting er for vanskelig... :)
	}
}
