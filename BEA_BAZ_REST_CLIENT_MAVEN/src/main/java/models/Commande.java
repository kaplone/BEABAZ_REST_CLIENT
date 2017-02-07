package models;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;

import utils.RestAccess;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commande  extends Commun{
	
	private String nom_affichage;
	
	private String nom_complet;
	
	private String complement;
	
	private String remarques;

	private LocalDate dateCommande;

	private LocalDate dateDebutProjet;

	private LocalDate dateFinProjet;
	
	private Map<String,Model> modele;

	private Map<String, Auteur> auteur;
	
	private Model modeleObj;
	private Auteur auteurObj;
	
	private Map<String, String> oeuvresTraitees_map;
	
	private List<Map<String, String>> oeuvresTraitees;
	//private List<Map<String, Object>> oeuvresTraitees_id;
	private List<Map<String, Traitement>> traitements_attendus;
	
	final DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("E MMM d H:m:s z y", Locale.US);
	
	public Commande(){
		oeuvresTraitees_map = new HashMap<>();
	}
	
	public static void update(Commande c){

		RestAccess.update("commande", c);
	}
	
    public static void save(Commande c){
		
		RestAccess.save("commande", c);
		
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

	public LocalDate getDateCommande() {
		return dateCommande;
	}
	public void setDateCommande(String dateCommande) {
		this.dateCommande = LocalDate.parse(dateCommande, inputFormat);
	}
	public void setLocalDateCommande(LocalDate dateCommande) {   
		setDateCommande(dateCommande.toString());
	}

	public LocalDate getDateDebutProjet() {
		return dateDebutProjet;
	}

	public void setDateDebutProjet(String dateDebutProjet) {
		this.dateDebutProjet = LocalDate.parse(dateDebutProjet, inputFormat);

	}
	
	public void setLocalDateDebutProjet(LocalDate dateDebutProjet) {
		setDateDebutProjet(dateDebutProjet.toString());
		
	}

	public LocalDate  getDateFinProjet() {
		return dateFinProjet;
	}

	
	public void setDateFinProjet(String dateFinProjet) {
		this.dateFinProjet = LocalDate.parse(dateFinProjet, inputFormat);
	}
	
	public void setLocalDateFinProjet(LocalDate dateFinProjet) {
        
		setDateFinProjet(dateFinProjet.toString());
	}

	public Set<String> getOeuvresTraitees_names() {
		return oeuvresTraitees_map.keySet();
	}

	public void addOeuvreTraitee(OeuvreTraitee oeuvreTraitee) {
		this.oeuvresTraitees_map.put(oeuvreTraitee.toString(), oeuvreTraitee.get_id().toString());
	}

	public Set<String> getTraitements_attendus_names() {
		
		Set<String> a = new HashSet<>();
		
		if (traitements_attendus != null){
			for (Map<String, Traitement> m : this.getTraitements_attendus()){
				for (Entry<String, Traitement> e : m.entrySet()){
					if (e.getKey().equals("traitement_attendu_string")){
						a.add(e.getValue().toString());
					}
				}
			}
		}
		
				
        return a;
	}


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

	public Map<String, Model> getModele(){
		return modele;
	}
	
	public Model getModeleObj(){
		return modeleObj;
	}
	
	public Map<String, Auteur> getAuteur(){
		return auteur;
	}
	
	public Auteur getAuteurObj(){
		return auteurObj;
	}
	
	public void setAuteur(Map<String, Auteur> auteur){
		this.auteurObj = auteur.values().iterator().next();
		this.auteur = auteur;
	}
	
	public void setModele(Map<String, Model> modele){
		
		this.modeleObj = modele.values().iterator().next();
		this.modele = modele;
	}

	public void setOeuvresTraitees(List<Map<String, String>> oeuvresTraitees) {	
		this.oeuvresTraitees = oeuvresTraitees;
	}

	public List<Map<String, Traitement>> getTraitements_attendus() {
		return traitements_attendus;
	}

	public void setTraitements_attendus(List<Map<String, Traitement>> traitements_attendus) {
		this.traitements_attendus = traitements_attendus;
	}
	
	
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

		return retrouveCommande(commandeSelectionne).get_id();
	}
	
	public static Commande retrouveCommande(String commandeSelectionne){

        String commande_str= RestAccess.request("commande", "nom", commandeSelectionne);
        //System.out.println("commande from String : " + commande_str);
        
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
    
}
