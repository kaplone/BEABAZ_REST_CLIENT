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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import enums.Progression;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TacheTraitement extends Commun{
	
	private Progression fait_;
	private Date date;
	private ObjectId traitement_id;
	private String complement;
	private Map<String, ObjectId> produitsLies;
	private Set<String> produitsLies_names;
	private Set<String> produitsLies_id;
    
    private boolean supp; 
    
    public TacheTraitement(){
    	produitsLies = new HashMap<>();
    }
    
    public TacheTraitement(Traitement t){
    	produitsLies = new HashMap<>();
    	traitement_id = t.get_id();
    	fait_ = Progression.TODO_;
    	super.setNom(t.getNom());
    	  	
    }
    
//    public static TacheTraitement retrouveTacheTraitement(ObjectId id){
//
//        String tacheTraitement_str= RestAccess.request("tacheTraitement", id);
//        TacheTraitement c = null;
//		
//		try {
//			  c = Commun.getMapper().readValue(tacheTraitement_str, TacheTraitement.class);
//		  }
//		  catch (IOException e) {
//			  
//	    }
//		
//		return c;
//	}
//    
//    public static TacheTraitement retrouveTacheTraitement(String id){
//		
//		return retrouveTacheTraitement(new ObjectId(id));
//	}
    
    public static void update(TacheTraitement c){

		RestAccess.update("tacheTraitement", c);
	}
	
    public static void save(TacheTraitement c){
		
		RestAccess.save("tacheTraitement", c);
		
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

	public ObjectId getTraitement_id() {
		return traitement_id;
	}
	public void setTraitement_id(ObjectId traitement) {
		this.traitement_id = traitement;
	}
	
	public Traitement getTraitement(){
		return Traitement.retrouveTraitement(traitement_id);
	}
	
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
	
	public String getNom_complet(){
		return this.getTraitement().getNom_complet() + this.getComplement() != null ? " " + this.getComplement() : "";
	}
	
	public Progression getFait_(){
		return fait_;
	}
	public void setFait_(Progression p){
		fait_ = p;
	}

	public Set<String> getProduitsLies_names() {
		return produitsLies_names;
	}
	public void setProduitsLies_names(Set<String> produitsLies_names) {
		this.produitsLies_names = produitsLies_names;
	}
	
	public  Collection<ObjectId> getProduitsLies_id() {
		return produitsLies.values();
	}
	public void setProduitsLies_id(Set<String> produitsLies_id) {
		this.produitsLies_id = produitsLies_id;
	}

	public void addProduitLie(Produit produitLie) {
		this.produitsLies.put(produitLie.getNom(), produitLie.get_id());
	}

	public Map<String, ObjectId> getProduitsLies() {
		return produitsLies;
	}

	public void setProduitsLies(Map<String, ObjectId> produitsLies) {
		this.produitsLies = produitsLies;
	}
}
