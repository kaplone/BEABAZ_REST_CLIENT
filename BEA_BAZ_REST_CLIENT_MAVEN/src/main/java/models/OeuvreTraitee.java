package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import enums.EtatFinal;
import enums.Progression;
import utils.RestAccess;
import utils.Normalize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OeuvreTraitee extends Commun {
	
	private String commande_id;
	
	private String oeuvre_id;
	
	private Map<String, String> traitementsAttendus_id;

	private EtatFinal etat;
	private String complement_etat;
	
	private ArrayList<String> alterations;
	
	private ArrayList<Fichier> fichiers;
	private Map<String,String> fichiers_id;
	
	private Progression progressionOeuvreTraitee;
	
    private String observations;
    
    private String key1;
    private String key2;
    
    private String cote;
    
    public OeuvreTraitee(){
    	
    	traitementsAttendus_id = new HashMap<>();
    	alterations = new ArrayList<>();
    	fichiers = new ArrayList<>();
    	
    }

	public static void update(OeuvreTraitee c){
		RestAccess.update("oeuvreTraitee", c);
	}
	
    public static void save(OeuvreTraitee c){		
		RestAccess.save("oeuvreTraitee", c);	
	}

	public void setEtat(EtatFinal etat) {
		this.etat = etat;
	}
	public ArrayList<Fichier> getFichiers() {
		
		ArrayList<Fichier> fichiers = new ArrayList<>();
		
		for (String s : fichiers_id.keySet()) {
			fichiers.add(Fichier.retrouveFichier(fichiers_id.get(s)));
		}
		
		return fichiers;
	}
	
	public Fichier getFichierAffiche () {
		
		Fichier fichier = null;
		
		for (String s : fichiers_id.keySet()) {
			
			if (Normalize.normalizeDenormStringField(s).endsWith(".PR.1.JPG")){
				fichier = Fichier.retrouveFichier(fichiers_id.get(s));
			}
		}
		
		return fichier;
		
	}
	
	public Map<String, String> getFichiers_id() {
		return fichiers_id;
	}
	public Set<String> getFichiers_names() {
		return fichiers_id.keySet();
	}
	public void setFichiers(ArrayList<Fichier> fichiers) {
		this.fichiers = fichiers;
	}

	public Progression getProgressionOeuvreTraitee() {
		return progressionOeuvreTraitee;
	}
	
	public void setProgressionOeuvreTraitee(Progression progressionOeuvreTraitee) {
		this.progressionOeuvreTraitee = progressionOeuvreTraitee;
	}
	public Set<String> getTraitementsAttendus_names() {
		return traitementsAttendus_id.keySet();
	}
	public Collection<String> getTraitementsAttendus_id() {
		return traitementsAttendus_id.values();
	}
	public void addTraitementAttendu(Traitement traitementAttendu) {
		this.traitementsAttendus_id.put(traitementAttendu.getNom(), traitementAttendu.get_id().toString());
	}
	public void addTraitementAttendu(String nom, ObjectId id) {
		this.traitementsAttendus_id.put(nom, id.toString());
	}

	public ImageView getIcone_progression() {
		
        Image image = new Image(progressionOeuvreTraitee.getUsedImage());
        
        ImageView usedImage = new ImageView();
        usedImage.setFitHeight(15);
        usedImage.setPreserveRatio(true);
        usedImage.setImage(image);
		
		return usedImage;
	}
	public ArrayList<String> getAlterations() {
		return alterations;
	}
	public void setAlterations(ArrayList<String> alterations) {
		this.alterations = alterations;
	}
	public EtatFinal getEtat() {
		return etat;
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
	
	public Oeuvre getOeuvre(){
		
		return Oeuvre.retrouveOeuvre(oeuvre_id);
	}

	public String getOeuvre_id() {
		return oeuvre_id;
	}

	public void setOeuvre_id(String oeuvre_id) {
		this.oeuvre_id = oeuvre_id;
	}

	public void setTraitementsAttendus_id(Map<String, String> traitementsAttendus_id) {
		this.traitementsAttendus_id = traitementsAttendus_id;
	}

	public void setFichiers_id(Map<String, String> fichiers_id) {
		this.fichiers_id = fichiers_id;
	}

	public String getCommande_id() {
		return commande_id;
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
	
	public static ObjectId retrouveId(String oeuvreTraiteeSelectionne){

		return retrouveOeuvreTraitee(oeuvreTraiteeSelectionne).get_id();
	}
	
	public static OeuvreTraitee retrouveOeuvreTraitee(String oeuvreTraiteeSelectionne){
		
		System.out.println("------------ " + oeuvreTraiteeSelectionne);

        String oeuvreTraitee_str= RestAccess.request("oeuvreTraitee", "nom", oeuvreTraiteeSelectionne);
        OeuvreTraitee c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println("========== " + oeuvreTraitee_str);
        
        if (oeuvreTraitee_str != null){
          try {			 
  			  c = mapper.readValue(oeuvreTraitee_str, OeuvreTraitee.class);
  		  }
  		  catch (IOException e) {
  	    }
        }
		
		
		
		return c;
	}
	
	public static OeuvreTraitee retrouveOeuvreTraitee(ObjectId id){

        String oeuvreTraitee_str= RestAccess.request("oeuvreTraitee", id);
        OeuvreTraitee c = null;
		
		try {
			  c = Commun.getMapper().readValue(oeuvreTraitee_str, OeuvreTraitee.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static OeuvreTraitee[] retrouveOeuvreTraitees(){
		
		String oeuvreTraitee_str= RestAccess.request("oeuvreTraitee");
        OeuvreTraitee[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(oeuvreTraitee_str, OeuvreTraitee[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
    public static ObservableList<String> retrouveOeuvreTraiteesStr(){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (OeuvreTraitee c : retrouveOeuvreTraitees()){
			liste_str.add(c.getNom());
		}
		return liste_str;
	}
    
    public static OeuvreTraitee[] retrouveOeuvreTraiteesCommande(Commande commande, boolean fait){
		
    	OeuvreTraitee[] c = null ;
    	String oeuvreTraitee_str = null;
    	
    	if (fait){
    		oeuvreTraitee_str = RestAccess.request("oeuvreTraitee", Progression.FAIT_, commande);
    	}
    	else {
    		oeuvreTraitee_str = RestAccess.request("oeuvreTraitee", commande);
    	}
		
        
		
		try {
			c = Commun.getMapper().readValue(oeuvreTraitee_str, OeuvreTraitee[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
}
