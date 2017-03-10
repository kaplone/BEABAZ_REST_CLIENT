package models;

import utils.RestAccess;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Produit extends Commun {
	
	private String nom_complet;

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String detail) {
		this.nom_complet = detail;
	}
	
	public static Produit retrouveProduit(String produitSelectionne){
		
		System.out.println("------------ " + produitSelectionne);

        String produit_str= RestAccess.request("produit", "nom", produitSelectionne);
        Produit c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println("========== " + produit_str);
        
        if (produit_str != null){
          try {			 
  			  c = mapper.readValue(produit_str, Produit.class);
  		  }
  		  catch (IOException e) {
  	      }
        }
			
		return c;
	}
	
	public static Produit retrouveProduit(ObjectId id){

        String produit_str= RestAccess.request("produit", id);
        Produit c = null;
		
		try {
			  c = Commun.getMapper().readValue(produit_str, Produit.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static Produit[] retrouveProduits(){
		
		String produit_str= RestAccess.request("produit");
        Produit[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(produit_str, Produit[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
    public static ObservableList<String> retrouveProduitsStr(){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (Produit c : retrouveProduits()){
			liste_str.add(c.getNom());
		}
		return liste_str;
	}
}
