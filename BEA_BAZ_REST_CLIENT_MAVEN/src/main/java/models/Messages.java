package models;

import org.bson.types.ObjectId;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class Messages {
	
    private static Stage exportStage;
	
    private static Client client;
	private static Commande commande;
	private static Oeuvre oeuvre;
	private static OeuvreTraitee oeuvreTraitee;

	
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

	public static OeuvreTraitee getOeuvreTraitee() {
		return oeuvreTraitee;
	}

	public static void setOeuvreTraitee(OeuvreTraitee oeuvreTraitee) {
		Messages.oeuvreTraitee = oeuvreTraitee;
	}

	public static void setOeuvre(Oeuvre oeuvre) {
		Messages.oeuvre = oeuvre;
	}

	public static Oeuvre getOeuvre() {
		return oeuvre;
	}

}
