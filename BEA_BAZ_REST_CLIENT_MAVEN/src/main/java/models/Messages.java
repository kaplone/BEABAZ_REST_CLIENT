package models;

import java.util.Map;

import org.bson.types.ObjectId;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Messages {
	
    private static Stage exportStage;
	
    private static Client client;
	private static Commande commande;
	private static Map<String, String> oeuvreTraitee;
	private static OeuvreTraitee oeuvreTraiteeObj;
	
	private static Auteur auteur;
	private static TacheTraitement tacheTraitement;
	private static Produit produit;
	private static Fichier fichier;
	
	
	public static Stage getStage(){
		return exportStage;
	}
	
	
	public static Commande getCommande() {
		return commande;
	}

	public static void setCommande(Commande commandeSelectionne) {
		Messages.commande = commandeSelectionne;
	}
	
	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		Messages.client = client;
	}

	public static Stage getExportStage() {
		return exportStage;
	}


	public static void setExportStage(Stage exportStage) {
		Messages.exportStage = exportStage;
	}

	public static Map<String, String> getOeuvreTraitee() {
		return oeuvreTraitee;
	}

	public static void setOeuvreTraitee(Map<String, String> oeuvreTraiteeSelectionne) {
		Messages.oeuvreTraitee = oeuvreTraiteeSelectionne;
	}


	public static Auteur getAuteur() {
		return auteur;
	}


	public static void setAuteur(Auteur auteur) {
		Messages.auteur = auteur;
	}


	public static TacheTraitement getTacheTraitement() {
		return tacheTraitement;
	}


	public static void setTacheTraitement(TacheTraitement tacheTraitement) {
		Messages.tacheTraitement = tacheTraitement;
	}


	public static Produit getProduit() {
		return produit;
	}


	public static void setProduit(Produit produit) {
		Messages.produit = produit;
	}


	public static Fichier getFichier() {
		return fichier;
	}


	public static void setFichier(Fichier fichier) {
		Messages.fichier = fichier;
	}


	public static OeuvreTraitee getOeuvreTraiteeObj() {
		return oeuvreTraiteeObj;
	}


	public static void setOeuvreTraiteeObj(OeuvreTraitee oeuvreTraiteeObj) {
		Messages.oeuvreTraiteeObj = oeuvreTraiteeObj;
	}
	

}
