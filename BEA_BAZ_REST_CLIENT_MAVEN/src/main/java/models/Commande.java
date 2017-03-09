package models;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;

import utils.RestAccess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javafx.collections.ObservableList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commande  extends Commun{
	
	private String nom_affichage;
	
	private String nom_complet;
	
	private String complement;
	
	private String remarques;

	private LocalDate dateCommande;
	private LocalDate dateDebutProjet;
	private LocalDate dateFinProjet;
	

	private Map<String, Model> modele_obj;
	private Map<String, Auteur> auteur_obj;
	private Map<String, String> modele_map;
	private Map<String, String> auteur_map;
	
	@JsonIgnore
	private Map<String, Model> map_modeles_obj;
	@JsonIgnore
	private Map<String, Auteur> map_auteurs_obj;
	
	@JsonIgnore
	private ObservableList<String> liste_traitements;
	@JsonIgnore
	private Map<String, Traitement> map_traitements;
	
	@JsonIgnore
	private Model modeleObj;
	@JsonIgnore
	private Auteur auteurObj;
	
	private Map<String, String> oeuvresTraitees_map;
	
	private List<Map<String, String>> oeuvresTraitees;
	//private List<Map<String, Object>> oeuvresTraitees_id;
	private List<Map<String, Traitement>> traitements_attendus;
	
	final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("E MMM d H:m:s z y", Locale.US);
	final DateTimeFormatter simpleInputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US);
	
	public Commande(){
		oeuvresTraitees_map = new HashMap<>();
		modele_map = new HashMap<>();
		auteur_map = new HashMap<>();
		map_auteurs_obj = new HashMap<>();
		map_modeles_obj = new HashMap<>();
	}

    public Commande get(){
		
		return this;
		
	}
    
    public String toString(){
    	
    	return this.getNom();
    }

	public String getRemarques() {
		return remarques;
	}

	public void setRemarques(String remarques) {
		this.remarques = remarques;
	}

	public String getDateCommande() {
		return dateCommande.toString();
	}
	@JsonIgnore
	public LocalDate getDateCommande_LocalDate() {
		return dateCommande;
	}
	public void setDateCommande(String dateCommande) {
		if (dateCommande.split("-").length == 3){
			this.dateCommande = LocalDate.parse(dateCommande, simpleInputFormat);
		}
		else {
			this.dateCommande = LocalDate.parse(dateCommande, inputFormat);
		}	
	}
	public void setDateCommandePicker(String dateCommande) {
		this.dateCommande = LocalDate.parse(dateCommande, simpleInputFormat);
	}
	public void setLocalDateCommande(LocalDate dateCommande) {   
		setDateCommandePicker(dateCommande.toString());
	}

	public String getDateDebutProjet() {
		return dateDebutProjet.toString();
	}
	@JsonIgnore
	public LocalDate getDateDebutProjet_LocalDate() {
		return dateDebutProjet;
	}

	public void setDateDebutProjet(String dateDebutProjet) {
		if (dateDebutProjet.split("-").length == 3){
			this.dateDebutProjet = LocalDate.parse(dateDebutProjet, simpleInputFormat);
		}
		else {
			this.dateDebutProjet = LocalDate.parse(dateDebutProjet, inputFormat);
		}
	}
	public void setDateDebutProjetPicker(String dateDebutProjet) {
		this.dateDebutProjet = LocalDate.parse(dateDebutProjet, simpleInputFormat);

	}
	public void setLocalDateDebutProjet(LocalDate dateDebutProjet) {
		setDateDebutProjetPicker(dateDebutProjet.toString());
		
	}

	public String  getDateFinProjet() {
		return dateFinProjet.toString();
	}
	@JsonIgnore
	public LocalDate getDateFinProjet_LocalDate() {
		return dateFinProjet;
	}

	
	public void setDateFinProjet(String dateFinProjet) {
		if (dateFinProjet.split("-").length == 3){
			this.dateFinProjet = LocalDate.parse(dateFinProjet, simpleInputFormat);
		}
		else {
			this.dateFinProjet = LocalDate.parse(dateFinProjet, inputFormat);
		}
	}
	public void setDateFinProjetPicker(String dateFinProjet) {
		this.dateFinProjet = LocalDate.parse(dateFinProjet, simpleInputFormat);
	}
	public void setLocalDateFinProjet(LocalDate dateFinProjet) {
        
		setDateFinProjetPicker(dateFinProjet.toString());
	}
    
	@JsonIgnore
	public Set<String> getOeuvresTraitees_names() {
		return oeuvresTraitees_map.keySet();
	}

	public void addOeuvreTraitee(OeuvreTraitee oeuvreTraitee) {
		this.oeuvresTraitees_map.put(oeuvreTraitee.toString(), oeuvreTraitee.get_id().toString());
	}
    
	@JsonIgnore
	public void setListe_traitements(ObservableList<String> liste){
		liste_traitements = liste;
	}
	
	@JsonIgnore
	public void setMap_traitements(Map<String, Traitement> map){
		map_traitements = map;
	}
	
	@JsonIgnore
	public Set<String> getTraitements_attendus_names() {
		
		Set<String> a = new HashSet<>();
		
		if (traitements_attendus != null){
			for (Map<String, Traitement> m : traitements_attendus){
				for (Entry<String, Traitement> e : m.entrySet()){
					a.add(e.getKey());
				}
			}
		}			
        return a;
	}
    
	@JsonIgnore
	public String getNom_affichage() {
		return nom_affichage;
	}

	public void setNom_affichage(String nom_affichage) {
		this.nom_affichage = nom_affichage;
	}

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public Map<String, String> getModele(){
		return modele_map;
	}
	
	@JsonIgnore
	public Model getModeleObj(){
		return modeleObj;
	}
	
	public Map<String, String> getAuteur(){
		return auteur_map;
	}
	
	@JsonIgnore
	public void setAuteur_map(String nom){
		auteur_map.put("auteur_string", nom);
		if (nom != null){
			auteur_map.put("auteur_id", map_auteurs_obj.get(nom).get_id());
		}
		else {
			auteur_map.put("auteur_id", null);
		}
		
	}
	@JsonIgnore
	public Auteur getAuteurObj(){
		return auteurObj;
	}
	
	public void setAuteur_obj(Map<String, Auteur> auteur){
		if(auteur_obj != null){
			this.auteurObj = auteur.values().iterator().next();		
		}
		this.auteur_obj = auteur;
	}
	
	public void setModele_obj(Map<String, Model> modele){
		if (modele_obj != null){
			this.modeleObj = modele.values().iterator().next();
		}		
		this.modele_obj = modele;
	}
	
	@JsonIgnore
	public void setModele_map(String nom){
		
		modele_map.put("modele_string", nom);
		if (nom != null){
			modele_map.put("modele_id", map_modeles_obj.get(nom).get_id());
		}
		else {
			modele_map.put("modele_id", null);
		}
		
	}
	public void setOeuvresTraitees(List<Map<String, String>> oeuvresTraitees) {	
		this.oeuvresTraitees = oeuvresTraitees;
	}

	public List<Map<String, String>> getTraitements_attendus() {
		
		List<Map<String, String>> traitements_attendus_liste = new ArrayList<>();		
		liste_traitements.forEach(a -> {
			Map<String, String> entree = new HashMap<>();
			entree.put("traitement_attendu_string", a);
			if (a.contains(": ")){
				entree.put("traitement_attendu_id", map_traitements.get(a.split(": ")[1]).get_id());
			}
			else{
				entree.put("traitement_attendu_id", map_traitements.get(a).get_id());
			}
			
			traitements_attendus_liste.add(entree);
		});
		
		return traitements_attendus_liste;
	}

	public void setTraitements_attendus(List<Map<String, Traitement>> traitements_attendus) {
		this.traitements_attendus = traitements_attendus;
	}
	
	@JsonIgnore
	public Map<String, String> getOeuvresTraitees_map() {
		return oeuvresTraitees_map;
	}

	public void setOeuvresTraitees_map(Map<String, String> oeuvresTraitees_map) {
		this.oeuvresTraitees_map = oeuvresTraitees_map;
	}


	public List<Map<String, String>> getOeuvresTraitees() {
		return oeuvresTraitees;
	}
	
	public static ObjectId retrouveId(String commandeSelectionne){

		return new ObjectId(retrouveCommande(commandeSelectionne).get_id());
	}
	
	public static Commande retrouveCommande(String commandeSelectionne){

        String commande_str= RestAccess.request("commande", "nom", commandeSelectionne);
        
        Commande c = null;
		
		try {
			  c = Commun.getMapper().readValue(commande_str, Commande.class);
		  }
		  catch (IOException e) {
	    }
		
		return c;
	}
	
	public static Commande retrouveCommande(ObjectId id){

        String commande_str= RestAccess.request("commande", id);
        System.out.println(commande_str);
        
        Commande c = null;
		
		try {
			  c = Commun.getMapper().readValue(commande_str, Commande.class);
		  }
		  catch (IOException e) {
			  e.printStackTrace();
	    }
		
		return c;
	}
	
	public static Commande[] retrouveCommandes(){
		
		String commande_str= RestAccess.request("commande");
        Commande[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(commande_str, Commande[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
	public void addModels(List<Model> m){
		m.forEach(a -> map_modeles_obj.put(a.getNom(), a));
	}
	
	public void addAuteurs(List<Auteur> m){
		m.forEach(a -> map_auteurs_obj.put(a.getNom(), a));
	}
    
}
