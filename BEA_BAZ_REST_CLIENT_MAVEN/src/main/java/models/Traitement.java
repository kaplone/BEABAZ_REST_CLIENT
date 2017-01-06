package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import utils.RestAccess;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Traitement extends Commun{

	private String nom_complet;
	
	private List<Produit> liste_produits;
	private Map<String, String> produits;
	
	public Traitement(){
		this.liste_produits = new ArrayList<Produit>();
	}
	
	public static void update(Traitement t){

		RestAccess.update("traitement", t);
	}
	
    public static void save(Traitement t){
		
		RestAccess.save("traitement", t);
		
	}
    
    public static void insert(Traitement t){
		
		RestAccess.insert("traitement", t);
		
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

	public Set<String> getProduits_names() {
		return liste_produits.stream()
				       .map(a -> a.getNom())
				       .collect(Collectors.toSet());
	}

	public List<Produit> getListe_produits() {
		return liste_produits;
	}

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

		return retrouveTraitement(traitementSelectionne).get_id();
	}
	
	public static Traitement retrouveTraitement(String traitementSelectionne){
		
		System.out.println("------------ " + traitementSelectionne);

        String traitement_str= RestAccess.request("traitement", "nom", traitementSelectionne);
        Traitement c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println("========== " + traitement_str);
        
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
		
		String traitement_str= RestAccess.request("traitement");
		System.out.println("traitement_str : " + traitement_str);
        Traitement[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(traitement_str, Traitement[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
    public static ObservableList<String> retrouveTraitementsStr(){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (Traitement c : retrouveTraitements()){
			liste_str.add(c.getNom());
		}
		return liste_str;
	}
	
}
