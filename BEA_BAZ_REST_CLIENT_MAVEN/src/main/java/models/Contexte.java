package models;

import controleurs.Fiche_commande_controller;

public class Contexte {
	
	public static final int largeurFenetre = 1366;
	public static final int hauteurFenetre = 720;
	
	public static Fiche_commande_controller fiche_commande_controller;
	
	

	public static Fiche_commande_controller getFiche_commande_controller() {
		return fiche_commande_controller;
	}
	public static void setFiche_commande_controller(Fiche_commande_controller fiche_commande_controller) {
		Contexte.fiche_commande_controller = fiche_commande_controller;
	}	
}
