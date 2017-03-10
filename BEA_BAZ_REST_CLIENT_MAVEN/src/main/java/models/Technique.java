package models;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.RestAccess;

public class Technique extends Commun{
	
	private String nom_complet;

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}
	
	public static Technique retrouveTechnique(String techniqueSelectionne){
		
		System.out.println("------------ " + techniqueSelectionne);

        String technique_str= RestAccess.request("technique", "nom", techniqueSelectionne);
        Technique c = null;
        ObjectMapper mapper = new ObjectMapper();
        
        System.out.println("========== " + technique_str);
        
        if (technique_str != null){
          try {			 
  			  c = mapper.readValue(technique_str, Technique.class);
  		  }
  		  catch (IOException e) {
  	    }
        }
		
		
		
		return c;
	}
	
	public static Technique retrouveTechnique(ObjectId id){

        String technique_str= RestAccess.request("technique", id);
        Technique c = null;
		
		try {
			  c = Commun.getMapper().readValue(technique_str, Technique.class);
		  }
		  catch (IOException e) {
			  
	    }
		
		return c;
	}
	
	public static Technique[] retrouveTechniques(){
		
		String technique_str= RestAccess.request("technique");
        Technique[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(technique_str, Technique[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	
    public static ObservableList<String> retrouveTechniquesStr(){
    	
    	ObservableList<String> liste_str = FXCollections.observableArrayList();
		
		for (Technique c : retrouveTechniques()){
			liste_str.add(c.getNom());
		}
		return liste_str;
	}

}
