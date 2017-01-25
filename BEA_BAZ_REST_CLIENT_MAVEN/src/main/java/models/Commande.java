package models;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import utils.RestAccess;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import enums.Progression;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Commande  extends Commun{
	
	private String nom_affichage;
	
	private String nom_complet;
	
	private String complement;
	
	private String remarques;

	private LocalDateTime dateCommande;

	private LocalDateTime dateDebutProjet;

	private LocalDateTime dateFinProjet;
	
	private Map<String, Object> modele;
	private String modele_string;
	private String modele_id;
	private Entry<String,String> modele_map;
	
	private Map<String, Object> auteur;
	private String auteur_string;
	private String auteur_id;
	private Entry<String,String> auteur_map;
	
	private Map<String, String> oeuvresTraitees_map;
	private Map<String, String> traitements_attendus_map;
	
	private List<Map<String, String>> oeuvresTraitees;
	private List<Map<String, Object>> oeuvresTraitees_id;
	private List<Map<String, Object>> traitements_attendus_id;
	
	final DateFormat inputFormat = new SimpleDateFormat("E MMM d H:m:s z y", Locale.US);
	
	public Commande(){
		oeuvresTraitees_map = new HashMap<>();
		traitements_attendus_map = new HashMap<>();
		
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
		Instant instant = Instant.ofEpochMilli(dateCommande.toEpochSecond(ZoneOffset.UTC));
		LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		return res;
		//return dateCommande;
	}
	public void setDateCommande(String dateCommande) {

		Date res = null;
		try {
			res = inputFormat.parse(dateCommande);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.dateCommande = LocalDateTime.ofInstant(res.toInstant(), ZoneId.systemDefault() );
	}

	public LocalDate getDateDebutProjet() {
		Instant instant = Instant.ofEpochMilli(dateDebutProjet.toEpochSecond(ZoneOffset.UTC));
		LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		return res;
		//return dateDebutProjet;
	}

	public void setDateDebutProjet(String dateDebutProjet) {
		Date res = null;
		try {
			res = inputFormat.parse(dateDebutProjet);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.dateDebutProjet = LocalDateTime.ofInstant(res.toInstant(), ZoneId.systemDefault() );
	}

	public LocalDate  getDateFinProjet() {
		Instant instant = Instant.ofEpochMilli(dateFinProjet.toEpochSecond(ZoneOffset.UTC));
		LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		return res;
		//return dateFinProjet;
	}

	
	public void setDateFinProjet(String dateFinProjet) {
		Date res = null;
		try {
			res = inputFormat.parse(dateFinProjet);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.dateFinProjet = LocalDateTime.ofInstant(res.toInstant(), ZoneId.systemDefault() );
	}

	public Set<String> getOeuvresTraitees_names() {
		return oeuvresTraitees_map.keySet();
	}

	public void addOeuvreTraitee(OeuvreTraitee oeuvreTraitee) {
		this.oeuvresTraitees_map.put(oeuvreTraitee.toString(), oeuvreTraitee.get_id().toString());
	}

	public Set<String> getTraitements_attendus_names() {
		
		Set<String> a = new HashSet<>();
		
		for (Map<String, Object> m : this.getTraitements_attendus_id()){
			for (Entry<String, Object> e : m.entrySet()){
				if (e.getKey().equals("traitement_attendu_string")){
					a.add(e.getValue().toString());
				}
			}
		}		
        return a;
	}

	public void addTraitement_attendu_map(String traitement_attendu, String id) {
		this.traitements_attendus_map.put(traitement_attendu, id.toString());
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

	public Entry<String,String> getModele_map() {
		return modele_map;
	}

	public void setModele_map(Entry<String,String> modele) {
		this.modele_map = modele;
	}
	
	public String getModele_id() {
		return modele_id;
	}
	
	public String getModele_name() {
		return modele_string;
	}

	public String getAuteur_id() {
		return auteur_id;
	}
	
	public String getAuteur_name() {
		return auteur_string;
	}

	public Model getModel(){
		return Model.retrouveModel(getModele_id());
	}
	
	public Auteur getAuteur(){
		return Auteur.retrouveAuteur(getAuteur_id());
	}

	public void setOeuvresTraitees(List<Map<String, String>> oeuvresTraitees) {	
		this.oeuvresTraitees = oeuvresTraitees;
	}

	public List<Map<String, Object>> getTraitements_attendus_id() {
		return traitements_attendus_id;
	}

	public void setTraitements_attendus_id(List<Map<String, Object>> traitements_attendus_id) {
		this.traitements_attendus_id = traitements_attendus_id;
	}
	
	
	public Map<String, String> getOeuvresTraitees_map() {
		return oeuvresTraitees_map;
	}

	public void setOeuvresTraitees_map(Map<String, String> oeuvresTraitees_map) {
		this.oeuvresTraitees_map = oeuvresTraitees_map;
	}

	public Map<String, String> getTraitements_attendus_map() {
		return traitements_attendus_map;
	}

	public void setTraitements_attendus_map(Map<String, String> traitements_attendus_map) {
		this.traitements_attendus_map = traitements_attendus_map;
	}

//	public void setDateCommande(LocalDateTime dateCommande) {
//		this.dateCommande = dateCommande;
//	}
//
//	public void setDateDebutProjet(LocalDateTime dateDebutProjet) {
//		this.dateDebutProjet = dateDebutProjet;
//	}
//
//	public void setDateFinProjet(LocalDateTime dateFinProjet) {
//		this.dateFinProjet = dateFinProjet;
//	}

	public Map<String, Object> getModele() {
		return modele;
	}

	public void setModele(Map<String, Object> modele) {
		this.modele = modele;
	}

	public String getModele_string() {
		return modele_string;
	}

	public void setModele_string(String modele_string) {
		this.modele_string = modele_string;
	}

	public String getAuteur_string() {
		return auteur_string;
	}

	public void setAuteur_string(String auteur_string) {
		this.auteur_string = auteur_string;
	}

	public Entry<String, String> getAuteur_map() {
		return auteur_map;
	}

	public void setAuteur_map(Entry<String, String> auteur_map) {
		this.auteur_map = auteur_map;
	}

	public void setModele_id(String modele_id) {
		this.modele_id = modele_id;
	}

	public void setAuteur(Map<String, Object> auteur) {
		this.auteur = auteur;
	}

	public void setAuteur_id(String auteur_id) {
		this.auteur_id = auteur_id;
	}
	
	public void setOeuvresTraitees_id(List<Map<String, Object>> oeuvresTraitees_id) {
		this.oeuvresTraitees_id = oeuvresTraitees_id;
	}
	

	public List<Map<String, String>> getOeuvresTraitees() {
		return oeuvresTraitees;
	}

	public List<Map<String, Object>> getOeuvresTraitees_id() {
		return oeuvresTraitees_id;
	}

	public static ObjectId retrouveId(String commandeSelectionne){

		return retrouveCommande(commandeSelectionne).get_id();
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
