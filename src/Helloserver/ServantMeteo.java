package Helloserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

import org.omg.CORBA.ORB;
import MeteoApp.MeteoPOA;


public class ServantMeteo extends MeteoPOA{

	// On gere desormais l'historique des bulletins meteo:
	// une collection d'objets, instances de la classe BulletinMeteo
	private static ArrayList<BulletinMeteo> bulletinsMeteo = new ArrayList<BulletinMeteo>(); 
	private ORB orb;
	public ORB getORB() {
		return orb;
	}

	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	//Constructeur 
	
	public ServantMeteo(){
		ServantMeteo.genererUnHistorique();
	}

	// TP4 Premiere version
	//Génération du buletin aléa
	public static void genererUnHistorique() {

		String[] tempsQuilFait = {"Grand beau temps", "Pluie", "Quelques averses",
				"Brouillard givrant", "Vent fort", "Nuageux"};

		String[] temperatures = {"Doux", "Chaud", "Froid", "De saison"};

		String[] geoZones = {"Annecy", "Paris", "Lyon", "Chambery"};

		int randomTempsQuilFaitNum, randomTemperaturesNum, randomGeoZonesNum;

		BulletinMeteo bulletin;

		String avis;

		for (int i = 0; i < 10; i++) {

			randomTempsQuilFaitNum = ThreadLocalRandom.current().nextInt(0,
					tempsQuilFait.length);
			randomTemperaturesNum = ThreadLocalRandom.current().nextInt(0,
					temperatures.length);
			randomGeoZonesNum = ThreadLocalRandom.current().nextInt(0,
					geoZones.length);
			avis = tempsQuilFait[randomTempsQuilFaitNum] +
					" - " + temperatures[randomTemperaturesNum];
			bulletin = new BulletinMeteo(avis);
			bulletin.setZone_geo(geoZones[randomGeoZonesNum]);
			bulletinsMeteo.add(bulletin);
		}

	}


	//================================Méthode Meteo.IDL=================================================
	
	
	//-----------------------------------------Méthode Server+client IDL-----------------------------------
	public String afficherBulletincourant(){
		//this.setMessage("Bulletin actuel:\t"+	this.getBulletin_courant().toString() + "\n");
		String message = "Bulletin actuel:\n"+ this.getBulletin_courant().toString()+"\n";
		return message;
	}
	//----------------------------------------------------------------------------------------------------------------------

	// Utilisation des accesseurs, modificateurs !!!!
	// Modifications exigees
	public void afficherBulletins() {
		System.out.println("===== Historique des bulletins meteo =====\n");
		for (BulletinMeteo bulletin : this.bulletinsMeteo) { // this.bulletinsMeteo est la collection d'objets
			System.out.println(bulletin.toString()); // element joue
		}
	}



	// Modifications exigees
	public BulletinMeteo getBulletin_courant() {
		int nbBulletins = this.nbBulletins();
		if (nbBulletins >= 1)
			// le dernier se trouve a la place (la taille de la collection - 1)
			return this.bulletinsMeteo.get(nbBulletins-1);  
		else
			return null;
	}

	// apres question 15
	public void ajouterBulletin(BulletinMeteo bulletin) {
		if (!this.bulletinsMeteo.contains(bulletin))
			this.bulletinsMeteo.add(bulletin);
	}

	public int nbBulletins() {
		return this.bulletinsMeteo.size();
	}



	// Il faudrait nommer cette methode rechercherBulletin
	// Elle permet de retourner le premier bulletin trouve
	// dans l'historique dont la zone geo correspond a celle
	// passee en parametre.
	public BulletinMeteo rechercherBulletin(String zoneG) {
		BulletinMeteo trouve = null;
		int i = 0;
		while (trouve == null && i < this.bulletinsMeteo.size()) {
			trouve = this.bulletinsMeteo.get(i);
			if (trouve.getZone_geo() != zoneG)
				trouve = null;
			i++;
		}
		return trouve;
	}

	




}
