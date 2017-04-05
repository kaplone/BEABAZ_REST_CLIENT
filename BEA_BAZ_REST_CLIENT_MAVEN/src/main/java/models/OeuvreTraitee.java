package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;

import enums.EtatFinal;
import enums.Progression;
import utils.RestAccess;
import utils.Normalize;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OeuvreTraitee extends Commun {
	
	private String commande_id;
	
	private String oeuvre_id;
	
	private List<Map<String, String>> traitementsAttendus;

	private EtatFinal etat;
	private String complement_etat;
	
	private List<String> alterations;
	
	private List<Map<String, String>> fichiers;
	
	private Progression progressionOeuvreTraitee;
	
    private String observations;
    
    private String key1;
    private String key2;
    
    private String cote;
    
    private Oeuvre oeuvre_obj;
    
    //private List<String> traitementsAttendus_id;
    private List<String> traitementsAttendus_names;
    private List<TacheTraitement> traitementsAttendus_obj;
    
    @JsonIgnore
    private static Commande commande;
    
    public OeuvreTraitee(){
    	
    	traitementsAttendus = new ArrayList<>();
    	alterations = new ArrayList<>();
    	fichiers = new ArrayList<>();	
    }
    
	public void setEtat(EtatFinal etat) {
		this.etat = etat;
	}
	
	@JsonIgnore
	public Fichier getFichierAffiche () {

    	String fichier_id = null;

    	for (Map<String, String> file : fichiers){
    		if (file.get("fichier_string").endsWith("_PR_1_JPG")){
				fichier_id = file.get("fichier_id");
			}
		}
		
    	ObjectMapper mapper = new ObjectMapper();
		Fichier fichier = null;
		try {
			fichier = mapper.readValue(RestAccess.request("fichier", new ObjectId(fichier_id)), Fichier.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return fichier;
		
	}
	
	public List<Fichier> getFichiers_obj() {
		
		ObjectMapper mapper = new ObjectMapper();

		return fichiers.stream()
				       .map(a ->{
				    	   Fichier fichier = null;
				    	   try {
				   			   fichier = mapper.readValue(RestAccess.request("fichier", new ObjectId(a.get("fichier_id"))), Fichier.class);
				   		   } catch (IOException e) {
				   			   e.printStackTrace();
				   		   }
				    	   return fichier;})
				       .collect(Collectors.toList());
	}
	
	public List<Map<String, String>> getFichiers() {
		return fichiers;
	}
	@JsonIgnore
	public Set<String> getFichiers_names() {
		
		return fichiers.stream()
                .map(a -> a.get("fichier_string"))
                .collect(Collectors.toSet());
	}

	public Progression getProgressionOeuvreTraitee() {
		return progressionOeuvreTraitee;
	}
	
	public void setProgressionOeuvreTraitee(Progression progressionOeuvreTraitee) {
		this.progressionOeuvreTraitee = progressionOeuvreTraitee;
	}
	
	public List<String> accesseurTraitementsAttendus_names() {

    	return traitementsAttendus_names;
	}
//	@JsonIgnore
//	public List<String> getTraitementsAttendus_id() {
//
//    	return traitementsAttendus_id;
//	}
//	
	public List<Map<String,String>> getTraitementsAttendus() {
    	return traitementsAttendus;
	}
	
	public List<TacheTraitement> accesseurTraitementsAttendus_obj() {
	
    	return traitementsAttendus_obj;
	}
	
	public void setTraitementsAttendus_names(List<String> traitementsAttendus_names) {

    	this.traitementsAttendus_names = traitementsAttendus_names;
	}
	
//	public void setTraitementsAttendus_id(List<String> traitementsAttendus_id) {
//
//    	this.traitementsAttendus_id = traitementsAttendus_id;
//	}
//	
	public void setTraitementsAttendus_obj(List<TacheTraitement> traitementsAttendus_obj) {
    	this.traitementsAttendus_obj = traitementsAttendus_obj;
	}

	public void addTraitementAttendu(Traitement traitementAttendu) {
		this.addTraitementAttendu(traitementAttendu.getNom(), traitementAttendu.get_id().toString());
	}
	public void addTraitementAttendu(String nom, String id) {

    	Map<String, String> map = new HashMap<>();
    	map.put("traitementAttendu_id", id);
    	map.put("traitementAttendu_string", nom);

    	this.traitementsAttendus.add(map);
	}

	@JsonIgnore
	public ImageView getIcone_progression() {
		
        Image image = new Image(progressionOeuvreTraitee.getUsedImage());
        
        ImageView usedImage = new ImageView();
        usedImage.setFitHeight(15);
        usedImage.setPreserveRatio(true);
        usedImage.setImage(image);
		
		return usedImage;
	}
	public List<String> getAlterations() {
		return alterations;
	}
	public void setAlterations(List<String> alterations) {
		this.alterations = alterations;
	}
	public void setAlterations_string(List<String> alterations) {
		this.alterations = alterations;
	}
	public EtatFinal getEtat() {
		return etat;
	}
	
	public String getEtat_current() {
		return etat.toString();
	}
	
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	} 

	public String getComplement_etat() {
		return complement_etat;
	}
	public void setComplement_etat(String complement_etat) {
		this.complement_etat = complement_etat;
	}
	
	@JsonIgnore
	public Oeuvre getOeuvre(){
		//return this.oeuvre_obj;
		return Oeuvre.retrouveOeuvre(new ObjectId(oeuvre_id));
	}
	
//	public void setOeuvre(Map<String, Object> oeuvre){
//		this.oeuvre_obj = (Oeuvre) JSON.parse(oeuvre.toString());
//		System.out.println("oeuvre_obj.get_id() = " + oeuvre_obj.get_id());
//	}

	public String getOeuvre_id() {
		return oeuvre_id;
	}

	public void setOeuvre_id(String oeuvre_id) {
		this.oeuvre_id = oeuvre_id;
	}

	public void setTraitementsAttendus(List<Map<String, String>> traitementsAttendus) {
		this.traitementsAttendus = traitementsAttendus;
	}

	public void setFichiers(List<Map<String, String>> fichiers) {
		this.fichiers = fichiers;
	}

	public String getCommande_id() {
		return commande_id;
	}
    
	@JsonIgnore
	public Commande getCommande() {
		if(commande == null){
			commande = Commande.retrouveCommande(new ObjectId(commande_id));
		}
		return commande;
	}
	
	public void setCommande_id(String commande_id) {
		this.commande_id = commande_id;
	}

	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getCote() {
		return cote;
	}

	public void setCote(String cote) {
		this.cote = cote;
	}	
	
	public static OeuvreTraitee retrouveOeuvreTraitee(String oeuvreSelectionne){

        String oeuvre_str= RestAccess.request("oeuvreTraitee", "nom", oeuvreSelectionne);
        OeuvreTraitee c = null;
		
		try {
			  c = Commun.getMapper().readValue(oeuvre_str, OeuvreTraitee.class);
		  }
		  catch (IOException e) {
			  e.printStackTrace();
	    }
		
		return c;
	}
	
	public static OeuvreTraitee retrouveOeuvreTraitee(ObjectId id){

        String oeuvre_str= RestAccess.request("oeuvreTraitee", id);
        OeuvreTraitee c = null;
		
		try {
			  c = Commun.getMapper().readValue(oeuvre_str, OeuvreTraitee.class);
		  }
		  catch (IOException e) {
			  System.out.println("erreur dans retrouveOeuvre(ObjectId id)");
			  System.out.println();
			  System.out.println(oeuvre_str);
			  System.out.println();
			  e.printStackTrace();
	    }
		
		return c;
	}
}
