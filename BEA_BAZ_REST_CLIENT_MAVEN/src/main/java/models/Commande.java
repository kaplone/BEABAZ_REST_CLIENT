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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

	private LocalDateTime dateCommande;

	private LocalDateTime dateDebutProjet;

	private LocalDateTime dateFinProjet;
	
	private String modele_id;
	
	private String auteur_id;
	
	private Map<String, String> oeuvresTraitees_id;
	private Map<String, String> traitements_attendus_id;
	
	final DateFormat inputFormat = new SimpleDateFormat("E MMM d H:m:s z y", Locale.US);
	
	public Commande(){
		oeuvresTraitees_id = new HashMap<>();
		traitements_attendus_id = new HashMap<>();
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
		return oeuvresTraitees_id.keySet();
	}

	public void addOeuvreTraitee(OeuvreTraitee oeuvreTraitee) {
		this.oeuvresTraitees_id.put(oeuvreTraitee.toString(), oeuvreTraitee.get_id().toString());
	}

	public Set<String> getTraitements_attendus_names() {
		return traitements_attendus_id.keySet();
	}

	public void addTraitement_attendu_id(String traitement_attendu, String id) {
		this.traitements_attendus_id.put(traitement_attendu, id.toString());
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

	public String getModele_id() {
		return modele_id;
	}

	public void setModele_id(String modele) {
		this.modele_id = modele;
	}

	public String getAuteur_id() {
		return auteur_id;
	}

	public void setAuteur_id(String auteur) {
		this.auteur_id = auteur;
	}
	
	public Model getModel(){
		return Model.retrouveModel(modele_id);
	}
	
	public Auteur getAuteur(){
		return Auteur.retrouveAuteur(auteur_id);
	}

	public Map<String, String> getOeuvresTraitees_id() {
		return oeuvresTraitees_id;
	}

	public void setOeuvresTraitees_id(Map<String, String> oeuvresTraitees_id) {
		this.oeuvresTraitees_id = oeuvresTraitees_id;
	}

	public Map<String, String> getTraitements_attendus_id() {
		return traitements_attendus_id;
	}

	public void setTraitements_attendus_id(Map<String, String> traitements_attendus_id) {
		this.traitements_attendus_id = traitements_attendus_id;
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
        System.out.println("commande_str : " + commande_str);
        
        Commande c = null;
		
		try {
			  System.out.println("debut try");
			  c = Commun.getMapper().readValue(commande_str, Commande.class);
			  System.out.println("c dans retrouveCommande .. : " + c);
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
