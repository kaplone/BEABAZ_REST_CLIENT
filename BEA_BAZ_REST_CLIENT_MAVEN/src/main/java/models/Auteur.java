package models;

import utils.RestAccess;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Auteur extends Commun{
	
	private String nom_complet;

	public String getNom_complet() {
		return nom_complet;
	}

	public void setNom_complet(String nom_complet) {
		this.nom_complet = nom_complet;
	}
	
	public static Auteur retrouveAuteur_by_id(ObjectId id){

        String auteur_str= RestAccess.request("auteur", id);
        Auteur a = null;
		
		try {
			  a = Commun.getMapper().readValue(auteur_str, Auteur.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
	public static Auteur retrouveAuteur_by_name(String key, String value){

        String auteur_str= RestAccess.request("auteur", key, value);
        Auteur a = null;
		
		try {
			  a = Commun.getMapper().readValue(auteur_str, Auteur.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}
	
    public static Auteur[] retrouveAuteurs(){
		
		String auteur_str= RestAccess.request("auteur");
        Auteur[] c = null ;
		
		try {
			c = Commun.getMapper().readValue(auteur_str, Auteur[].class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
    
    public static Auteur fromJson(String auteur_str){

        Auteur a = null;
		
		try {
			  a = Commun.getMapper().readValue(auteur_str, Auteur.class);
		  }
		  catch (IOException e) {
	    }
		
		return a;
	}

}
