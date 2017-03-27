package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.util.Hash;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import utils.JsonUtils;
import utils.RestAccess;

public class Messages {
	
    private static Stage exportStage;
	
    private static Client client;
	private static Commande commande;
	private static Map<String, String> oeuvreTraitee;
	private static OeuvreTraitee oeuvreTraiteeObj;
	
	private static List<Auteur> tous_les_auteurs;
	
	private static Auteur auteur;
	private static Produit produit;
	private static Fichier fichier;
	
	private static ObservableList<Produit> tous_les_produits;
	private static ObservableList<String> tous_les_noms_de_produits;
	
	private static ObservableList<Traitement> tous_les_traitements;
	
	private static TacheTraitement traitementSelectionne;
	
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

	public static ObservableList<Produit> getTous_les_produits() {
		
		if (tous_les_produits == null){
			tous_les_produits = FXCollections.observableArrayList();
			JsonUtils.JsonToListObj(RestAccess.requestAll("produit"), tous_les_produits, new TypeReference<List<Produit>>() {});
		}		
		return tous_les_produits;
	}
	
    public static ObservableList<String> getTous_les_nom_de_produits() {
    	
    	if (tous_les_noms_de_produits == null){
			tous_les_noms_de_produits = FXCollections.observableArrayList();
			getTous_les_produits().forEach(a -> tous_les_noms_de_produits.add(a.getNom()));
		}		
		return tous_les_noms_de_produits;
	}
    public static void resetTous_les_produits(){
		tous_les_produits = null;
		tous_les_noms_de_produits = null;
	}

	public static TacheTraitement getTraitementSelectionne() {
		return traitementSelectionne;
	}

	public static void setTraitementSelectionne(TacheTraitement traitementSelectionne) {
		Messages.traitementSelectionne = traitementSelectionne;
	}

	public static ObservableList<Traitement> getTous_les_traitements() {
		if (tous_les_traitements == null){
			tous_les_traitements = FXCollections.observableArrayList();
			JsonUtils.JsonToListObj(RestAccess.requestAll("traitement"), tous_les_traitements, new TypeReference<List<Traitement>>() {});
		}		
		return tous_les_traitements;
	}
	
	public static Map<String, Traitement> getTous_les_traitements_map_by_name() {
		
		Map<String, Traitement> tltm = new HashMap<>();
		
		getTous_les_traitements().forEach(a -> tltm.put(a.getNom(), a));
	
		return tltm;
	}
	
    public static Map<String, Traitement> getTous_les_traitements_map_by_id() {
		
		Map<String, Traitement> tltm = new HashMap<>();
		
		getTous_les_traitements().forEach(a -> tltm.put(a.get_id(), a));
	
		return tltm;
	}
	public static void resetTous_les_traitements(){
		tous_les_traitements = null;
	}

	public static List<Auteur> getTous_les_auteurs() {
		return tous_les_auteurs;
	}

	public static void setTous_les_auteurs(List<Auteur> tous_les_auteurs) {
		Messages.tous_les_auteurs = tous_les_auteurs;
	}
    
	
}
