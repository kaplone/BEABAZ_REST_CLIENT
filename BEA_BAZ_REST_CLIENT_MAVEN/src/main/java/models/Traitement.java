package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import utils.RestAccess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Traitement extends Commun{

	private String nom_complet;
	
	@JsonIgnore
	private List<Produit> liste_produits;
	private Map<String, String> produits;
	
	private static ObservableList<String> liste_str;
	private static Traitement[] c;
	
	public Traitement(){
		this.liste_produits = new ArrayList<>();
		this.produits = new HashMap<>();
	}
	
    public void addProduit(Produit p){
    	
    	if (! liste_produits.contains(p)){
    		liste_produits.add(p);
    	}
    	
    }
    
    public void deleteProduit(Produit p){
    	
    	for (Produit p_ : liste_produits){
    		if (p.equals(p_)){
    			liste_produits.remove(p_);
    			break;
    		}
    	} 	
    }

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String detail) {
		this.nom_complet = detail;
	}
    
	@JsonIgnore
	public Set<String> getProduits_names() {
		return liste_produits.stream()
				       .map(a -> a.getNom())
				       .collect(Collectors.toSet());
	}
	@JsonIgnore
	public List<Produit> getListe_produits() {
		return liste_produits;
	}
	@JsonIgnore
	public Set<String> getListe_produits_names() {
		return produits.keySet();
	}
	@JsonIgnore
	public void setListe_produits(List<Produit> produits) {
		this.liste_produits = produits;
	}
	
	public Map<String, String> getProduits() {
		return produits;
	}

	public void setProduits(Map<String, String> produits) {
		this.produits = produits;
	}
	
	public static ObjectId retrouveId(String traitementSelectionne){

		return new ObjectId(retrouveTraitement(traitementSelectionne).get_id());
	}
	
	public static Traitement retrouveTraitement(String traitementSelectionne){

        String traitement_str= RestAccess.request("traitement", "nom", traitementSelectionne);
        Traitement c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        if (traitement_str != null){
          try {			 
  			  c = mapper.readValue(traitement_str, Traitement.class);
  		  }
  		  catch (IOException e) {
  	      }
        }
	
		return c;
	}
	
	public static Traitement retrouveTraitement(ObjectId id){

        String traitement_str= RestAccess.request("traitement", id);
        Traitement c = null;
		
		try {
			  c = Commun.getMapper().readValue(traitement_str, Traitement.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static Traitement[] retrouveTraitements(){
		
		if (c == null){
			String traitement_str = RestAccess.request("traitement");
	        c = null ;
			
			try {
				c = Commun.getMapper().readValue(traitement_str, Traitement[].class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return c;
	}
	
    public static ObservableList<String> retrouveTraitementsStr(){
    	
    	if(liste_str == null){
    		liste_str = FXCollections.observableArrayList();
    		
    		for (Traitement c : retrouveTraitements()){
    			liste_str.add(c.getNom());
    		}
    	}
    	
		return liste_str;
	}
    
    @JsonIgnore
    public void setProduitsFromObservable(ObservableList<Produit> o){
    	o.forEach(a -> produits.put(a.getNom(), a.get_id()));
    }
	
}
