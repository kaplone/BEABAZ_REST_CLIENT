package models;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import utils.RestAccess;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import enums.Progression;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TacheTraitement extends Commun{
	
	private Progression fait_;
	private Date date;
	private String traitement_id;
	private String complement;
	private Map<String, String> produitsLies;
	private Set<String> produitsLies_names;
	private Set<String> produitsLies_id;
	@JsonIgnore
	private Traitement traitementOriginal;
    
    private boolean supp; 
    
    public TacheTraitement(){
    	produitsLies = new HashMap<>();
    }
    
    public TacheTraitement(Traitement t){
    	produitsLies = new HashMap<>();
    	traitement_id = t.get_id();
    	traitementOriginal = t;
    	fait_ = Progression.TODO_;
    	setNom(t.getNom());
    	  	
    }

    public void addProduit(Produit p){
    	
    	if (! produitsLies.keySet().contains(p.getNom())){
    		produitsLies.put(p.getNom(), p.get_id());
    	}
    	
    }
    
    public void addProduit(String p){
    	
    	Produit p_ = Produit.retrouveProduit(p);
    	this.addProduit(p_);
    	
    }
    
    public void addProduit(ObjectId id){
    	Produit p_ = Produit.retrouveProduit(id);
    	this.addProduit(p_);
    }
    
    public void deleteProduit(Produit p){

    	for (String p_ : produitsLies.keySet()){
    		if (p.getNom().equals(p_)){
    			produitsLies.remove(p_);
    			break;
    		}
    	} 	
    }
    
    public void deleteProduit(String p){

    	for (String p_ : produitsLies.keySet()){
    		if (p_.equals(p)){
    			produitsLies.remove(p_);
    			break;
    		}
    	} 	
    }

	public String getFait() {
		return fait_.toString();
		
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
	}
    
	@JsonIgnore
	public Traitement getTraitement(){
		
		if (traitement_id != null && traitement_id != "null"){
			System.out.println("traitement_id : " + traitement_id);
			
			if (traitementOriginal == null){
				traitementOriginal = Messages.getTous_les_traitements_map_by_id().get(traitement_id);
			}
		}
		else {
            System.out.println("traitement_name : " + getNom());
			
			if (traitementOriginal == null){
				traitementOriginal = Messages.getTous_les_traitements_map_by_name().get(getNom());
			}
		}	
		return traitementOriginal;
	}
	
	@JsonIgnore
	public ImageView getIcone_progression() {
		
		
        Image image = new Image(fait_.getUsedImage());
        
        ImageView usedImage = new ImageView();
        usedImage.setFitHeight(15);
        usedImage.setPreserveRatio(true);
        usedImage.setImage(image);
		
		return usedImage;
	}

	public boolean isSupp() {
		return supp;
	}

	public void setSupp(boolean supp) {
		this.supp = supp;
	}
	
	@JsonIgnore
	public String getNom_complet(){
		if (this.getTraitement() != null){
			return this.getTraitement().getNom_complet() + this.getComplement() != null ? " " + this.getComplement() : "";
		}
		else {
			return getNom();
		}
		
	}
	
	public Progression getFait_(){
		return fait_;
	}
	public void setFait_(Progression p){
		fait_ = p;
	}
    
	@JsonIgnore
	public Set<String> getProduitsLies_names() {
		return produitsLies_names;
	}
	public void setProduitsLies_names(Set<String> produitsLies_names) {
		this.produitsLies_names = produitsLies_names;
	}
	
	@JsonIgnore
	public  Collection<String> getProduitsLies_id() {
		return produitsLies.values();
	}
	public void setProduitsLies_id(Set<String> produitsLies_id) {
		this.produitsLies_id = produitsLies_id;
	}

	public void addProduitLie(Produit produitLie) {
		this.produitsLies.put(produitLie.getNom(), produitLie.get_id());
	}

	public Map<String, String> getProduitsLies() {
		return produitsLies;
	}

	public void setProduitsLies(Map<String, String> produitsLies) {
		this.produitsLies = produitsLies;
	}
}
